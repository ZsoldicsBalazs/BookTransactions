package org.ubb.service;

import org.ubb.domain.Client;
import org.ubb.repository.BookStoreRepository;

import java.util.List;

public class ClientService {

    private final BookStoreRepository<Client> clientBookStoreRepository;


    public ClientService(BookStoreRepository<Client> clientBookStoreRepository) {
        this.clientBookStoreRepository = clientBookStoreRepository;
    }

    public Client addClient(Client client) {
        return clientBookStoreRepository.save(client);
    }

    public List<Client> getAll() {
        return this.clientBookStoreRepository.findAll();
    }
}
