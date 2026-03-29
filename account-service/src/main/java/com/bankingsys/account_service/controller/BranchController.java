package com.bankingsys.account_service.controller;

import com.bankingsys.account_service.dto.BranchRequest;
import com.bankingsys.account_service.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping("/addbranch")
    public ResponseEntity<String> addBranch(@RequestBody List<BranchRequest> requestList){

        if(branchService.addNewBranch(requestList))
            return new ResponseEntity<>("Branch created Successfully", HttpStatus.CREATED);
        else
            return new ResponseEntity<>("error saving branch", HttpStatus.FORBIDDEN);
    }
}
