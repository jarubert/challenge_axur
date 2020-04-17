package com.jarubert.threatValidator.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(
        name="Client",
        indexes = {@Index(name = "idx_id",  columnList="id", unique = true),
                @Index(name = "idx_name", columnList="name")},
        uniqueConstraints = @UniqueConstraint(columnNames={"name"}))
public class Client {
    private @Id
    @GeneratedValue
    Long id;

    private String name;
}
