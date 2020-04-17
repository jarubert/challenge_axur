package com.jarubert.threatValidator.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(
        name="Rule",
        indexes = {@Index(name = "idx_id",  columnList="id", unique = true),
                @Index(name = "idx_regex", columnList="expression")},
        uniqueConstraints = @UniqueConstraint(columnNames={"client_id", "expression"}))
public class Rule {
    private @Id
    @GeneratedValue
    Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "client_id")
    private Client client;

    private String expression;

    public Rule() { }

    public Rule(Client client, String expression) {
        this.client = client;
        this.expression = expression;
    }
}
