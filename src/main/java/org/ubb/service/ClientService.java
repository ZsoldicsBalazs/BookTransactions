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
//        TODO: Always throws an error for new client.
    }

    public List<Client> getAll() {
        return StreamSupport.stream(clientBookStoreRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void updateClient(Client newClientRequest) {
        int existingClientsId = newClientRequest.getId();
        clientBookStoreRepository
                .findOne(existingClientsId)
                .map(client -> {
                    client.setFirstName(newClientRequest.getFirstName());
                    client.setLastName(newClientRequest.getLastName());
                    client.setEmail(newClientRequest.getEmail());
                    client.setAge(newClientRequest.getAge());
                    client.setAddress(newClientRequest.getAddress());

                    clientBookStoreRepository.update(client);
                    return client;
                }).orElseThrow(() -> new ResourceNotFound("Existing client with id " + existingClientsId  + " not found"));
    }

    public void deleteClient(int id) {
        clientBookStoreRepository
                .delete(id).orElseThrow(() -> new ResourceNotFound("Client with id " + id + " not found, cannot be deleted"));
    }

}
