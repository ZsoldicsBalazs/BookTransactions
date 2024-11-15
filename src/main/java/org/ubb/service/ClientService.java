package org.ubb.service;

import org.ubb.domain.Client;
import org.ubb.domain.validators.BookStoreException;
import org.ubb.domain.validators.ResourceNotFound;
import org.ubb.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService {

    private final Repository<Integer, Client> clientRepository;


    public ClientService(Repository<Integer, Client> clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void addClient(Client clientToSave) {
        Optional<Client> client = clientRepository.save(clientToSave);
        if (client.isPresent()) {
            throw new BookStoreException("Client wasn't saved");
        }

    }

    public Client getClient(int id) {
        return clientRepository
                .findOne(id)
                .orElseThrow(() -> new ResourceNotFound("Client with id " + id + " not found"));
    }

    public List<Client> getAll() {
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void updateClient(Client newClientRequest) {
        if(clientRepository.update(newClientRequest).isPresent()) {
            throw new ResourceNotFound("Client with id " + newClientRequest.getId() + " isn't updated");
        }
    }

    public void deleteClient(int id) {
        clientRepository
                .delete(id)
                .orElseThrow(() -> new ResourceNotFound("Client with id " + id + " not found, cannot be deleted"));
    }



    public List<Client> filterClientByAge(int fromAge, int toAge){
        List<Client> allClients = getAll();

        return allClients.stream().filter(client -> client.getAge() >= fromAge && client.getAge() <= toAge).collect(Collectors.toList());
    }

}
