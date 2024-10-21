package org.ubb.service;

import org.ubb.domain.Client;
import org.ubb.domain.validators.ResourceNotFound;
import org.ubb.repository.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService {

    private final Repository<Integer, Client> clientBookStoreRepository;


    public ClientService(Repository<Integer, Client> clientBookStoreRepository) {
        this.clientBookStoreRepository = clientBookStoreRepository;
    }

    public Client addClient(Client client) {
        return clientBookStoreRepository.save(client)
                .orElseThrow(() -> new ResourceNotFound("Client not found"));
    }

    public List<Client> getAll() {
        return StreamSupport.stream(clientBookStoreRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void updateClient(Client existingClient, Client newClientRequest) {
        clientBookStoreRepository
                .findOne(existingClient.getId())
                .map(client -> {
                    client.setFirstName(newClientRequest.getFirstName());
                    client.setLastName(newClientRequest.getLastName());
                    client.setEmail(newClientRequest.getEmail());
                    client.setAddress(newClientRequest.getAddress());

                    clientBookStoreRepository.update(client);
                    return client;
                }).orElseThrow(() -> new ResourceNotFound("Existing client with id " + existingClient.getId()  + " not found"));
    }



}
