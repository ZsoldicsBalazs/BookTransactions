package org.ubb.repository;

import org.ubb.domain.Client;
import org.ubb.domain.validators.BookStoreException;
import org.ubb.domain.validators.RepositoryException;
import org.ubb.domain.validators.Validator;
import org.ubb.domain.validators.ValidatorException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public class ClientFileRepository extends BookStoreRepositoryImpl<Integer, Client> {


    private String fileName;
    public ClientFileRepository(Validator<Client> validator, String filePath) {
        super(validator);
        this.fileName = filePath;
        readFile();
    }


    @Override
    public Optional<Client> save(Client client) {

            Optional<Client> optionalClient = super.save(client);
            if (optionalClient.isEmpty()) {
                saveToFile(client);
                return Optional.of(client);
            }else {
                throw new RepositoryException("This client already exists");
            }
    }


    @Override
    public Optional<Client> delete(Integer id) {

            Optional<Client> deletedClinet = super.delete(id);
            if (deletedClinet.isPresent()) {
                deleteAllDataFromFile();
                super.findAll().forEach(this::saveToFile);
            }
            return deletedClinet;

    }

    @Override
    public Optional<Client> update(Client client){
        try {
            Optional<Client> optionalClient = super.update(client);
            if (optionalClient.isPresent()) {
                deleteAllDataFromFile();
                super.findAll().forEach(this::saveToFile);
            }
            return optionalClient;
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage(), e);
        }

    }

    private void deleteAllDataFromFile() {
        Path path = Path.of(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new RepositoryException("----------> Error opening or creating the file",e);
        }
    }




    private void readFile() {
        Path path = Path.of(fileName);
        try (Stream<String> fileLinesStream = Files.lines(path)) {
            fileLinesStream
                    .forEach(line -> {
                        if (line.isEmpty()) {
                            return;
                        }
                        List<String> lineData = List.of(line.split(";"));
                        int id = Integer.parseInt(lineData.get(0));
                        String firstName = lineData.get(1);
                        String lastName = lineData.get(2);
                        int age = Integer.parseInt(lineData.get(3));
                        String address = lineData.get(4);
                        String email = lineData.get(5);

                        Client client = new Client(id, firstName, lastName, age, address, email);
                        super.save(client);
                    });
        } catch (IOException | ValidatorException exception) {
            throw new RepositoryException(exception);
        }
    }

    private void saveToFile (Client client) {
        Path path = Path.of(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)){

            String clientString =
                    client.getId() +";" + client.getFirstName() +";" + client.getLastName() + ";" + client.getAge() + ";" + client.getAddress() + ";" + client.getEmail();
            bufferedWriter.newLine();
            bufferedWriter.write(clientString);

        } catch (IOException e) {
            throw new RuntimeException(e);


        }
    }


}
