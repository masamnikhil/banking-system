package com.bankingsys.account_service.serviceImpl;

import com.bankingsys.account_service.dto.AccountRequest;
import com.bankingsys.account_service.entity.Account;
import com.bankingsys.account_service.entity.Branch;
import com.bankingsys.account_service.exception.BranchNotFoundException;
import com.bankingsys.account_service.repository.AccountRepository;
import com.bankingsys.account_service.service.AccountService;
import com.bankingsys.account_service.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final BranchService branchService;

    private final AccountRepository accountRepository;


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

    public static String generateAccountNumber(String branchCode) {
        int random = new Random().nextInt(900000) + 100000;
        return branchCode + random;
    }
}
