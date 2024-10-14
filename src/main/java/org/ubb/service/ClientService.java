package org.ubb.service;

import org.ubb.domain.Client;
import org.ubb.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService {

    private final Repository<Integer, Client> clientBookStoreRepository;


    public ClientService(Repository<Integer, Client> clientBookStoreRepository) {
        this.clientBookStoreRepository = clientBookStoreRepository;
    }

    public Optional<Client> addClient(Client client) {
        return clientBookStoreRepository.save(client);
    }

    public List<Client> getAll() {
        return StreamSupport.stream(clientBookStoreRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
