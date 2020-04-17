package com.jarubert.threatValidator.service;

import com.jarubert.threatValidator.model.entity.Client;
import com.jarubert.threatValidator.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    /**
     *
     * gets the Client from the database in case there exists a client with the same name, otherwise creates a client
     * and return it
     *
     * @param clientName
     * @return
     */
    public Client getOrCreateClient(String clientName) {
        Client client = clientRepository.findByName(clientName);
        if (client != null) {
            logger.info("Found client: " + client.toString());
            return client;
        }

        return createClient(clientName);
    }

    /**
     *
     * Create a new client with the requested name and returns it
     *
     * @param clientName
     * @return
     */
    private Client createClient(String clientName) {
        Client client = new Client();
        client.setName(clientName);
        client = clientRepository.save(client);
        logger.info("Added new client: " + client.toString());

        return client;
    }

}
