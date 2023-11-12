package com.SuperBank.api.controller;

import com.SuperBank.api.model.Company;
import com.SuperBank.api.model.dtos.CompanyDTO;
import com.SuperBank.api.repository.CompanyRepository;
import com.SuperBank.api.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CompanyDTO> creatCompany(@RequestBody CompanyDTO companyDTO){

        CompanyDTO createdCompany = service.creatCompany(companyDTO);

        return ResponseEntity.status(200).body(createdCompany);

    }

    }
