package com.SuperBank.api.service;

import com.SuperBank.api.model.Company;
import com.SuperBank.api.model.dtos.CompanyDTO;
import com.SuperBank.api.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CompanyService {

    @Autowired
    CompanyRepository repository;

    @Autowired
    ModelMapper modelMapper;


    public CompanyDTO creatCompany(CompanyDTO companyDTO){

        Company company = modelMapper.map(companyDTO, Company.class);
        Company createdCompany = repository.save(company);
        companyDTO = modelMapper.map(createdCompany, CompanyDTO.class);

        return companyDTO;


    }
}
