package com.SuperBank.api.controller;

import com.SuperBank.api.model.Person;
import com.SuperBank.api.model.dtos.PersonDTO;
import com.SuperBank.api.repository.PersonRepository;
import com.SuperBank.api.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    PersonService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PersonDTO> creatPerson(@RequestBody PersonDTO personDTO){

        PersonDTO createdPerson = service.craetPerson(personDTO);

        return ResponseEntity.status(200).body(createdPerson);
    }
}
