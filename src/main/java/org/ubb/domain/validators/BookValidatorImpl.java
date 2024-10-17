package org.ubb.domain.validators;

import org.ubb.domain.Book;

import java.time.LocalDateTime;

public class BookValidatorImpl implements Validator<Book> {

    @Override
    public void validate(Book book) throws ValidatorException {
        isTitleValid(book);
        isAuthorValid(book);
        isYearValid(book);
        isPriceValid(book);
        isPublisherValid(book);
    }

    private void isTitleValid(Book book) {
        if (!book.getTitle().matches("^[a-zA-Z0-9]*$") && book.getTitle().length()>0){
            throw new ValidatorException("The title is invalid");
        }
    }
    private void isAuthorValid(Book book){
    if(book.getAuthor().isEmpty()){
        throw new ValidatorException("The author is invalid");
    }
    }

    private void isYearValid(Book book){
        if( book.getYear() > LocalDateTime.now().getYear() && book.getYear()<0){
            throw new ValidatorException("The year is invalid");
        }

    }
    private void isPublisherValid(Book book){
        if ( !(book.getPublisher().matches("^[a-zA-Z0-9]*$"))
                && !(book.getPublisher().length()>0)
                && !(book.getPublisher().length()<100)){
            throw new ValidatorException("The publisher is invalid");
        }
    }
    private void isPriceValid(Book book){
      if(!(book.getPrice()>0.00) && book.getPrice()>10000.00){
          throw new ValidatorException("The price is invalid");
      }
    }
}
