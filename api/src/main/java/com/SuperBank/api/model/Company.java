package com.SuperBank.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private String cnpj;
    private String name;
    private String email;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(cnpj, company.cnpj) && Objects.equals(email, company.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnpj, email);
    }
}
