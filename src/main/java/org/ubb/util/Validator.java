package org.ubb.util;

import org.ubb.domain.Book;

import java.util.List;

public class Validator {

    public static boolean isStringOk(String string)throws Exception{
        boolean flag = string.matches("[a-zA-Z ]+");
        if (flag == false){
            throw new Exception("String invalid");
        }else{
            return flag;
        }
    }
    public static boolean isNumericOk(int n) throws Exception{
    if (n<0){
        throw new Exception("Can't be negativie value");
    }else{
        return true;
        }
    }
    public static boolean isDoubleOk(double n) throws Exception{
        if(n<0.0){
            throw new Exception("Can't be negativie value");
        }else{
            return true;
        }
    }
    public static boolean isListOk(List<String> authors) throws Exception{
        for(String author : authors){
            if (!isStringOk(author)){
                throw new Exception("Can't be null");
            }
        }
        return true;
    }
    public static void validateBook(Book book) throws Exception{
        if (!isStringOk(book.getTitle())){
            throw new Exception("Can't be null");
        }
        if (!isListOk(book.getAuthor())){
            throw new Exception("Can't be null");
        }
        if (!isStringOk(book.getPublisher())){
            throw new Exception("Can't be null");
        }
        int year=book.getYear();
        if (!isNumericOk(year)){
            throw new Exception("Can't be negative");
        }
        double price=book.getPrice();
        if(!isDoubleOk(price)){
            throw new Exception("Cen't be negative");
        }

    }
}
