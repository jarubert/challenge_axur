package com.jarubert.threatValidator.service;

import com.jarubert.threatValidator.model.entity.Client;
import com.jarubert.threatValidator.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class ClientServiceTest {

    private static final String CLIENT_NAME = "testClient";

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void getOrCreateClient_test() {
        //guarantee no client exists
        List<Client> clientList = clientRepository.findAll();
        assertTrue(CollectionUtils.isEmpty(clientList));
        Client client = clientRepository.findByName(CLIENT_NAME);
        assertNull(client);

        //tries to get client
        client = clientService.getOrCreateClient(CLIENT_NAME);
        assertNotNull(client);
        //fetch the client just created
        Client validationClient = clientRepository.findByName(CLIENT_NAME);
        assertNotNull(validationClient);

        compareClients(client, validationClient);

        //validate that if we try to get a client with a already existing name, it doesnt create another
        client = clientService.getOrCreateClient(CLIENT_NAME);
        compareClients(client, validationClient);
        clientList = clientRepository.findAll();
        assertFalse(CollectionUtils.isEmpty(clientList));
        assertEquals(1, clientList.size());
    }

    private void compareClients(Client client, Client validationClient) {
        assertEquals(client.getId(), validationClient.getId());
        assertEquals(client.getName(), validationClient.getName());
    }
}
