package com.jarubert.threatValidator.repository;

import com.jarubert.threatValidator.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByName(String name);
}
