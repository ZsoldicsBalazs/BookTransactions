package org.ubb.service;

import org.ubb.domain.Client;
import org.ubb.repository.Repository;

import java.util.List;

public class ClientService {

    private final Repository<Integer, Client> clientBookStoreRepository;


    public ClientService(Repository<Integer, Client> clientBookStoreRepository) {
        this.clientBookStoreRepository = clientBookStoreRepository;
    }

    public Client addClient(Client client) {
        return clientBookStoreRepository.save(client);
    }

    public List<Client> getAll() {
        return this.clientBookStoreRepository.findAll();
    }
}
