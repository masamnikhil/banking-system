package com.bankingsys.account_service.service;

import com.bankingsys.account_service.dto.BranchRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BranchService {

    boolean addNewBranch(List<BranchRequest> branchList);
}
