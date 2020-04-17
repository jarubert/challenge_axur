package com.jarubert.threatValidator.repository;

import com.jarubert.threatValidator.model.entity.Client;
import com.jarubert.threatValidator.model.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    @Query(nativeQuery=true, value="SELECT r.* " +
            "   FROM rule r " +
            "   LEFT OUTER JOIN client c ON r.client_id = c.id " +
            "WHERE (c.name = ?1 OR r.client_id is null) " +
            "   AND ?2 regexp r.expression " +
            "LIMIT 1")
    Rule findMatch(String clientName, String url);

    Rule findByExpressionAndClient(String expression, Client client);
}
