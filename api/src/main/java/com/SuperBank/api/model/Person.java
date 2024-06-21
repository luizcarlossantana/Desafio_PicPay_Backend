package com.SuperBank.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Person  {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private String cpf;
    private String name;
    private String email;

    @ManyToMany
    @JoinTable
    private List<Company> companyList;


}
