package com.SuperBank.api.controller;

import com.SuperBank.api.model.Person;
import com.SuperBank.api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    PersonRepository repository;

    public ResponseEntity<Person> creatPerson(@RequestBody Person person){

        Person createdPerson = repository.save(person);

        return ResponseEntity.status(200).body(createdPerson);
    }
}
