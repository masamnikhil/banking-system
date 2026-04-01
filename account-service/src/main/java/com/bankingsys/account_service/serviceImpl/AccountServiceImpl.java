package com.bankingsys.account_service.serviceImpl;

import com.bankingsys.account_service.config.JwtUtil;
import com.bankingsys.account_service.dto.*;
import com.bankingsys.account_service.entity.*;
import com.bankingsys.account_service.exception.AuthServiceException;
import com.bankingsys.account_service.exception.BranchNotFoundException;
import com.bankingsys.account_service.exception.InsufficientBalanceException;
import com.bankingsys.account_service.feignclient.AuthClient;
import com.bankingsys.account_service.repository.AccountRepository;
import com.bankingsys.account_service.repository.OutboxRepository;
import com.bankingsys.account_service.service.AccountService;
import com.bankingsys.account_service.service.BranchService;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final BranchService branchService;

    private final AccountRepository accountRepository;

    private final AuthClient authClient;

    private final JwtUtil jwtUtil;

    private final OutboxRepository outBoxRepository;


    @Override
    public boolean createAccount(AccountRequest request) {

        Branch branch = branchService.getBranchByName(request.getBranchName());

        if(branch == null) throw new BranchNotFoundException("branch not found");

        String accountNumber = generateAccountNumber(branch.getBranchCode());

        try{
            Account account = Account.builder().accountNumber(accountNumber).accountType(request.getAccountType())
                    .branchId(branch.getId()).userId(UUID.fromString(request.getUserId())).build();
            accountRepository.save(account);
            return true;
        }
        catch(RuntimeException ex){
            throw new RuntimeException("error in creating account");
        }

    }

    @Override
    @Transactional
    public boolean withDrawAmount(WithdrawRequest request, String token) {

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new EntityNotFoundException("account not exist" + request.getAccountNumber()));

        String userIdFromToken = jwtUtil.extractUserId(token.substring(7));
        if(!userIdFromToken.equals(account.getUserId().toString())) {
            MetaDataRequest metaDataRequest = MetaDataRequest.builder().fromAccount(account.getAccountNumber())
                    .transactionType(TransactionType.WITHDRAWAL).amount(request.getAmount()).status(TransactionStatus.FAILED)
                    .transactionTime(LocalDateTime.now()).build();
            saveTransaction(metaDataRequest);
            throw new RuntimeException("withdrawal failed");
        }
        if(account.getBalance().compareTo(request.getAmount()) < 0) {
            MetaDataRequest metaDataRequest = MetaDataRequest.builder().fromAccount(account.getAccountNumber())
                    .transactionType(TransactionType.WITHDRAWAL).amount(request.getAmount()).status(TransactionStatus.FAILED)
                    .transactionTime(LocalDateTime.now()).build();
            saveTransaction(metaDataRequest);
            throw new InsufficientBalanceException("Insufficient funds");
        }

        try{
            ResponseEntity<HttpStatus> response = authClient.verifyProfilePassword(UserRequest.builder().userId(String.valueOf(account.getUserId())).profilePassword(request.getProfilePassword()).build());
            if(response.getStatusCode().is2xxSuccessful()){
                account.setBalance(account.getBalance().subtract(request.getAmount()));
                accountRepository.save(account);
                MetaDataRequest metaDataRequest = MetaDataRequest.builder().fromAccount(account.getAccountNumber())
                        .transactionType(TransactionType.WITHDRAWAL).amount(request.getAmount()).status(TransactionStatus.SUCCESS)
                        .transactionTime(LocalDateTime.now()).build();
                saveTransaction(metaDataRequest);
                return true;
            }

        }
        catch (FeignException ex) {
            if (ex.status() == 500) {
                MetaDataRequest metaDataRequest = MetaDataRequest.builder().fromAccount(account.getAccountNumber())
                        .transactionType(TransactionType.WITHDRAWAL).amount(request.getAmount()).status(TransactionStatus.FAILED)
                        .transactionTime(LocalDateTime.now()).build();
                saveTransaction(metaDataRequest);
                throw new AuthServiceException("withdraw failed");
            }
            if (ex.status() == 404) {
                throw new AuthServiceException("no profile password, please create profile password");
            }
            if(ex.status() == 400) {
                MetaDataRequest metaDataRequest = MetaDataRequest.builder().fromAccount(account.getAccountNumber())
                        .transactionType(TransactionType.WITHDRAWAL).amount(request.getAmount()).status(TransactionStatus.FAILED)
                        .transactionTime(LocalDateTime.now()).build();
                saveTransaction(metaDataRequest);
                throw new AuthServiceException("Incorrect profile password");
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean depositAmount(DepositRequest request, String token) {

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new EntityNotFoundException("account not exist" + request.getAccountNumber()));

        String userIdFromToken = jwtUtil.extractUserId(token.substring(7));
        if(!userIdFromToken.equals(account.getUserId().toString())) {
            MetaDataRequest metaDataRequest = MetaDataRequest.builder().toAccount(account.getAccountNumber())
                    .transactionType(TransactionType.DEPOSIT).amount(request.getAmount()).status(TransactionStatus.FAILED)
                    .transactionTime(LocalDateTime.now()).build();
            saveTransaction(metaDataRequest);
            throw new RuntimeException("deposit failed");
        }
        try{
            account.setBalance(account.getBalance().add(request.getAmount()));
            accountRepository.save(account);
            MetaDataRequest metaDataRequest = MetaDataRequest.builder().toAccount(account.getAccountNumber())
                    .transactionType(TransactionType.DEPOSIT).amount(request.getAmount()).status(TransactionStatus.SUCCESS)
                    .transactionTime(LocalDateTime.now()).build();
            saveTransaction(metaDataRequest);
            return true;
        } catch (RuntimeException e) {
            MetaDataRequest metaDataRequest = MetaDataRequest.builder().toAccount(account.getAccountNumber())
                    .transactionType(TransactionType.DEPOSIT).amount(request.getAmount()).status(TransactionStatus.FAILED)
                    .transactionTime(LocalDateTime.now()).build();
            saveTransaction(metaDataRequest);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean transferAmount(TransferRequest request, String token) {

        String from = request.getFromAccount();
        String to = request.getToAccount();

        Account sender;
        Account receiver;

        if(from.compareTo(to) < 0){
            sender = accountRepository.findByAccountNumber(from).orElseThrow(() -> new EntityNotFoundException("account not exist" + from));
            receiver = accountRepository.findByAccountNumber(to).orElseThrow(() -> new EntityNotFoundException("account not exist" + to));
        }else{
            receiver = accountRepository.findByAccountNumber(to).orElseThrow(() -> new EntityNotFoundException("account not exist" + to));
            sender = accountRepository.findByAccountNumber(from).orElseThrow(() -> new EntityNotFoundException("account not exist" + from));
        }

        String userIdFromToken = jwtUtil.extractUserId(token.substring(7));
        if(!userIdFromToken.equals(sender.getUserId().toString())) {
            MetaDataRequest metaDataRequest = MetaDataRequest.builder().fromAccount(sender.getAccountNumber()).toAccount(receiver.getAccountNumber())
                    .userId(receiver.getUserId().toString())
                    .transactionType(TransactionType.TRANSFER).amount(request.getAmount()).status(TransactionStatus.FAILED)
                    .transactionTime(LocalDateTime.now()).build();
            saveTransaction(metaDataRequest);
            throw new RuntimeException("transfer failed");
        }
        if(sender.getBalance().compareTo(request.getAmount()) < 0){
            MetaDataRequest metaDataRequest = MetaDataRequest.builder().fromAccount(sender.getAccountNumber()).toAccount(receiver.getAccountNumber())
                    .userId(receiver.getUserId().toString())
                    .transactionType(TransactionType.TRANSFER).amount(request.getAmount()).status(TransactionStatus.FAILED)
                    .transactionTime(LocalDateTime.now()).build();
            saveTransaction(metaDataRequest);
            throw new InsufficientBalanceException("Insufficient balance");
        }

        try{
            ResponseEntity<HttpStatus> response = authClient.verifyProfilePassword(UserRequest.builder().userId(String.valueOf(sender.getUserId())).profilePassword(request.getProfilePassword()).build());
            if(response.getStatusCode().is2xxSuccessful()){
                sender.setBalance(sender.getBalance().subtract(request.getAmount()));
                receiver.setBalance(receiver.getBalance().add(request.getAmount()));
                accountRepository.save(sender);
                accountRepository.save(receiver);
                MetaDataRequest metaDataRequest = MetaDataRequest.builder().fromAccount(sender.getAccountNumber()).toAccount(receiver.getAccountNumber())
                        .userId(receiver.getUserId().toString())
                        .transactionType(TransactionType.TRANSFER).amount(request.getAmount()).status(TransactionStatus.SUCCESS)
                        .transactionTime(LocalDateTime.now()).build();
                saveTransaction(metaDataRequest);
                return true;
            }

        }
        catch (FeignException ex) {
            if (ex.status() == 500) {
                MetaDataRequest metaDataRequest = MetaDataRequest.builder().fromAccount(sender.getAccountNumber()).toAccount(receiver.getAccountNumber())
                        .userId(receiver.getUserId().toString())
                        .transactionType(TransactionType.TRANSFER).amount(request.getAmount()).status(TransactionStatus.FAILED)
                        .transactionTime(LocalDateTime.now()).build();
                saveTransaction(metaDataRequest);
                throw new AuthServiceException("transfer failed");
            }
            if (ex.status() == 404) {
                throw new AuthServiceException("no profile password, please create profile password");
            }
            if(ex.status() == 400) {
                MetaDataRequest metaDataRequest = MetaDataRequest.builder().fromAccount(sender.getAccountNumber()).toAccount(receiver.getAccountNumber())
                        .userId(receiver.getUserId().toString())
                        .transactionType(TransactionType.TRANSFER).amount(request.getAmount()).status(TransactionStatus.FAILED)
                        .transactionTime(LocalDateTime.now()).build();
                saveTransaction(metaDataRequest);
                throw new AuthServiceException("Incorrect profile password");
            }
        }
        return false;
    }

    public static String generateAccountNumber(String branchCode) {
        int random = new Random().nextInt(900000) + 100000;
        return branchCode + random;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void saveTransaction(MetaDataRequest request) {
        TransactionEvent event = TransactionEvent.builder()
                .transactionId(UUID.randomUUID().toString())
                .transactionType(request.getTransactionType().toString())
                .fromAccount(request.getFromAccount())
                .toAccount(request.getToAccount())
                .amount(request.getAmount())
                .transactionTime(request.getTransactionTime())
                .status(request.getStatus().toString())
                .userId(request.getUserId())
                .build();

        OutboxEvent outboxEvent = OutboxEvent.builder().key(event.getTransactionId()).topic("transaction-event")
                .payload(event).sent(false).build();

        outBoxRepository.saveAndFlush(outboxEvent);
    }


}
