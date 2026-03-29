package com.bankingsys.account_service.service;

import com.bankingsys.account_service.dto.BranchRequest;
import com.bankingsys.account_service.entity.Branch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BranchService {

    boolean addNewBranch(List<BranchRequest> branchList);
    Branch getBranchByName(String name);
}
