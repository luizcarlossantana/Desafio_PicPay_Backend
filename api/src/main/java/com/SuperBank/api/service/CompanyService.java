package com.SuperBank.api.service;

import com.SuperBank.api.exception.DuplicateCompanyExceptiion;
import com.SuperBank.api.model.Company;
import com.SuperBank.api.model.dtos.CompanyDTO;
import com.SuperBank.api.repository.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CompanyService {

    @Autowired
    CompanyRepository repository;

    @Autowired
    ModelMapper modelMapper;


    public CompanyDTO creatCompany(CompanyDTO companyDTO){

        validateCompany(companyDTO);

        Company company = modelMapper.map(companyDTO, Company.class);
        Company createdCompany = repository.save(company);
        companyDTO = modelMapper.map(createdCompany, CompanyDTO.class);

        return companyDTO;


    }

    private void validateCompany(CompanyDTO companyDTO){

        List<Company> existingCompany = repository.findAll();

        for (Company company:existingCompany) {

        CompanyDTO creatCompanyDTO = modelMapper.map(company, CompanyDTO.class);

        if (creatCompanyDTO.equals(companyDTO)){
            throw new DuplicateCompanyExceptiion("There's already a company matching these specifications.");
        }
        }

    }
}
