package com.jarubert.threatValidator.service;

import com.jarubert.threatValidator.model.dto.ResponseMessage;
import com.jarubert.threatValidator.model.dto.RuleMessage;
import com.jarubert.threatValidator.model.dto.ValidationMessage;
import com.jarubert.threatValidator.model.entity.Rule;
import com.jarubert.threatValidator.repository.RuleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class RuleServiceTest {

    private static final String CLIENT_NAME = "testClient";
    private static final String REGEX = "service/v1/[^/?#]+";
    private static final String INVALID_REGEX = "service/v1/[^/?#+";
    private static final String VALID_URL = "service/v1/test232";
    private static final String INVALID_URL = "service/v1";
    private static final Integer CORRELATION_ID = 123;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleRepository ruleRepository;

    @Test
    public void createRuleFromMessageIfNotPresent_test_nullClient() {
        RuleMessage newRule = new RuleMessage();
        newRule.setClient(null);
        newRule.setRegex(REGEX);

        ruleService.createRuleFromMessageIfNotPresent(newRule);

        List<Rule> ruleList = ruleRepository.findAll();
        assertFalse(CollectionUtils.isEmpty(ruleList));
        assertEquals(1, ruleList.size());

        compareRules(newRule, ruleList.get(0));
    }

    private void compareRules(RuleMessage newRule, Rule rule) {
        assertEquals(newRule.getRegex(), rule.getExpression());
        assertEquals(newRule.getClient(), rule.getClient());
    }

    @Test
    public void createRuleFromMessageIfNotPresent_test_insert() {
        RuleMessage newRule = new RuleMessage();
        newRule.setClient(null);
        newRule.setRegex(REGEX);

        ruleService.createRuleFromMessageIfNotPresent(newRule);
        List<Rule> ruleList = ruleRepository.findAll();
        assertFalse(CollectionUtils.isEmpty(ruleList));
        assertEquals(1, ruleList.size());
        compareRules(newRule, ruleList.get(0));
    }


    @Test
    public void createRuleFromMessageIfNotPresent_test_noDuplication() {
        RuleMessage newRule = new RuleMessage();
        newRule.setClient(null);
        newRule.setRegex(REGEX);

        //null client
        ruleService.createRuleFromMessageIfNotPresent(newRule);
        ruleService.createRuleFromMessageIfNotPresent(newRule);

        //guarantee no rules Exist
        List<Rule> ruleList = ruleRepository.findAll();
        assertFalse(CollectionUtils.isEmpty(ruleList));
        assertEquals(1, ruleList.size());
        compareRules(newRule, ruleList.get(0));

        //name client
        newRule.setClient(CLIENT_NAME);
        ruleService.createRuleFromMessageIfNotPresent(newRule);
        ruleService.createRuleFromMessageIfNotPresent(newRule);
        ruleList = ruleRepository.findAll();
        assertFalse(CollectionUtils.isEmpty(ruleList));
        assertEquals(2, ruleList.size());

    }

    @Test
    public void isValidRule_test() {
        RuleMessage newRule = new RuleMessage();
        newRule.setClient(null);

        newRule.setRegex(INVALID_REGEX);
        assertFalse(ruleService.isValidRule(newRule));

        newRule.setRegex(REGEX);
        assertTrue(ruleService.isValidRule(newRule));
    }

    @Test
    public void isWhiteListed_test_notWhiteListed() {
        ValidationMessage validationMessage = new ValidationMessage();
        validationMessage.setClient(CLIENT_NAME);
        validationMessage.setCorrelationId(CORRELATION_ID);
        validationMessage.setUrl(VALID_URL);

        ResponseMessage expectedResponse = new ResponseMessage();
        expectedResponse.setMatch(Boolean.FALSE);
        expectedResponse.setCorrelationId(CORRELATION_ID);

        ResponseMessage response = ruleService.isWhiteListed(validationMessage);
        compareResponseMessage(expectedResponse, response);
    }

    @Test
    public void isWhiteListed_test_whiteListedByClient() {
        ValidationMessage validationMessage = new ValidationMessage();
        validationMessage.setClient(CLIENT_NAME);
        validationMessage.setCorrelationId(CORRELATION_ID);
        validationMessage.setUrl(VALID_URL);

        //create rule
        RuleMessage newRule = new RuleMessage();
        newRule.setClient(CLIENT_NAME);
        newRule.setRegex(REGEX);
        Rule rule = ruleService.createRuleFromMessageIfNotPresent(newRule);
        assertNotNull(rule);

        ResponseMessage expectedResponse = new ResponseMessage();
        expectedResponse.setMatch(Boolean.TRUE);
        expectedResponse.setRegex(REGEX);
        expectedResponse.setCorrelationId(CORRELATION_ID);

        ResponseMessage response = ruleService.isWhiteListed(validationMessage);
        compareResponseMessage(expectedResponse, response);
    }

    @Test
    public void isWhiteListed_test_whiteListedGlobal() {
        ValidationMessage validationMessage = new ValidationMessage();
        validationMessage.setClient(CLIENT_NAME);
        validationMessage.setCorrelationId(CORRELATION_ID);
        validationMessage.setUrl(VALID_URL);

        //create rule
        RuleMessage newRule = new RuleMessage();
        newRule.setClient(null);
        newRule.setRegex(REGEX);
        Rule rule = ruleService.createRuleFromMessageIfNotPresent(newRule);
        assertNotNull(rule);

        ResponseMessage expectedResponse = new ResponseMessage();
        expectedResponse.setMatch(Boolean.TRUE);
        expectedResponse.setRegex(REGEX);
        expectedResponse.setCorrelationId(CORRELATION_ID);

        ResponseMessage response = ruleService.isWhiteListed(validationMessage);
        compareResponseMessage(expectedResponse, response);

        validationMessage.setClient(null);
        compareResponseMessage(expectedResponse, response);
    }

    @Test
    public void isWhiteListed_test_invalidURL() {
        ValidationMessage validationMessage = new ValidationMessage();
        validationMessage.setClient(CLIENT_NAME);
        validationMessage.setCorrelationId(CORRELATION_ID);
        validationMessage.setUrl(INVALID_URL);

        //create rule
        RuleMessage newRule = new RuleMessage();
        newRule.setClient(null);
        newRule.setRegex(REGEX);
        Rule rule = ruleService.createRuleFromMessageIfNotPresent(newRule);
        assertNotNull(rule);

        ResponseMessage expectedResponse = new ResponseMessage();
        expectedResponse.setMatch(Boolean.FALSE);
        expectedResponse.setCorrelationId(CORRELATION_ID);

        ResponseMessage response = ruleService.isWhiteListed(validationMessage);
        compareResponseMessage(expectedResponse, response);
    }


    private void compareResponseMessage(ResponseMessage expectedResponse, ResponseMessage response) {
        assertEquals(expectedResponse.getCorrelationId(), response.getCorrelationId());
        assertEquals(expectedResponse.getMatch(), response.getMatch());
        assertEquals(expectedResponse.getRegex(), response.getRegex());
    }

}
