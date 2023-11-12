package com.SuperBank.api.service;

import com.SuperBank.api.model.Person;
import com.SuperBank.api.model.dtos.PersonDTO;
import com.SuperBank.api.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    PersonRepository repository;

    @Autowired
    ModelMapper modelMapper;

    public PersonDTO craetPerson(PersonDTO personDTO){

        Person person = modelMapper.map(personDTO, Person.class);
        Person createdPerson = repository.save(person);
        personDTO = modelMapper.map(createdPerson, PersonDTO.class);

        return personDTO;
    }

}
