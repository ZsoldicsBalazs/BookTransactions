package org.ubb.repository;

import org.ubb.domain.Client;
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
        if (client == null) {
            throw new RuntimeException("The given client is empty");
        }
        saveToFile(client);
        return super.save(client);
    }

//    Optional<Client> delete(Integer id) {
//
//    }
//
//    Optional<Client> update(Client entity) throws ValidatorException {
//
//    }



    private void readFile() {
        Path path = Path.of(fileName);
        try (Stream<String> fileLinesStream = Files.lines(path)) {
            fileLinesStream
                    .forEach(line -> {
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
            throw new RuntimeException(exception);
        }
    }

    private void saveToFile (Client client) {
        Path path = Path.of(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)){

            String clientString =
                    client.getId() +";" + client.getFirstName() +";" + client.getLastName() + ";" + client.getAge() + ";" + client.getAddress() + ";" + client.getEmail();
            bufferedWriter.newLine();
            bufferedWriter.write(clientString);
            bufferedWriter.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);


        }
    }


}
