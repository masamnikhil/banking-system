package com.bankingsys.account_service.serviceImpl;

import com.bankingsys.account_service.dto.BranchRequest;
import com.bankingsys.account_service.entity.Branch;
import com.bankingsys.account_service.repository.BranchRepository;
import com.bankingsys.account_service.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Override
    public boolean addNewBranch(List<BranchRequest> branchList) {

        try {
            List<Branch> branches = branchList.stream().map(branchRequest -> Branch.builder()
                    .branchCode(branchRequest.getBranchCode()).branchName(branchRequest.getBranchName())
                    .city(branchRequest.getCity()).ifsCode(branchRequest.getIfsCode()).build()).toList();
            branchRepository.saveAll(branches);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }
}
