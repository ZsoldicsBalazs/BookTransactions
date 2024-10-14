package org.ubb.repository;

import org.ubb.domain.Book;
import org.ubb.domain.validators.Validator;
import org.ubb.domain.validators.ValidatorException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookFileRepository extends BookStoreRepositoryImpl<Integer, Book>{

    private String fileName;

    public BookFileRepository(Validator<Book> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }


    private void loadData(){
        Path path = Paths.get(fileName);

        try{
            Files.lines(path).forEach(line -> {
                Pattern pattern = Pattern.compile(",(?=(?:[^\\[]*\\[[^\\]]*\\])*[^\\[]*$)");
                List<String> items = List.of(pattern.split(line));
                Pattern authorsPattern = Pattern.compile("\\[(.*?)\\]");
                Matcher matcher = authorsPattern.matcher(items.get(1));
                List<String> authorList = null;
                if (matcher.find()) {
                    String authors = matcher.group(1);
                    authorList = Arrays.asList(authors.split(","));
                }
                Integer id = Integer.parseInt(items.get(0));
                String title = items.get(1);
                String publisher = items.get(3);
                int year = Integer.parseInt(items.get(4));
                double price = Double.parseDouble(items.get(5));

                Book book = new Book(id,title, authorList,publisher,year,price);

                try{
                    super.save(book);
                }catch (ValidatorException e){
                    e.printStackTrace();
                }
            });

        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }


    @Override
    public Optional<Book> save(Book entity) throws ValidatorException {
        Optional<Book> book = super.save(entity);
        if (book.isPresent()){
            return book;
        }
        saveToFile(entity);
        return Optional.empty();

    }

    private void saveToFile(Book book) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    book.getId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getPublisher() + "," + book.getYear() + "," + book.getPrice());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
