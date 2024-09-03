package com.SuperBank.api;

import com.SuperBank.api.model.Company;
import com.SuperBank.api.model.Person;
import com.SuperBank.api.repository.CompanyRepository;
import com.SuperBank.api.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CompanyTest {

    @Mock
    PersonRepository personRepository;

    @Mock
    CompanyRepository companyRepository;

@BeforeEach
void setup() {
    MockitoAnnotations.initMocks(this);
}
    @Test
    public void criandoCompany() {
        Person person = new Person();
        List<Person> personList = new ArrayList<>();
        person.setName("fulana");
        person.setCpf("22222222");
        person.setEmail("luiz@gamil.com");

        personRepository.save(person);


        Company company = new Company();
        company.setCnpj("2222222222");
        company.setName("Empresa Tal");
        company.setEmail("email@tal.com");
        company.setPersonList(personList);
        personList.add(person);


        companyRepository.save(company);
    }
}
