package org.grostarin.springboot.demorest.services;

import org.grostarin.springboot.demorest.domain.Book;
import org.grostarin.springboot.demorest.domain.DeniedBook;
import org.grostarin.springboot.demorest.dto.BookSearch;
import org.grostarin.springboot.demorest.exceptions.BookIdMismatchException;
import org.grostarin.springboot.demorest.exceptions.BookNotFoundException;
import org.grostarin.springboot.demorest.repositories.BookRepository;
import org.grostarin.springboot.demorest.repositories.DeniedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class BookServices {    

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DeniedBookRepository deniedBookRepository;
    
    public Iterable<Book> findAll(BookSearch bookSearchDTO) {
        if(bookSearchDTO!=null && StringUtils.hasText(bookSearchDTO.title())) {
            return bookRepository.findByTitle(bookSearchDTO.title());  
        }
        return bookRepository.findAll();
    }

    public Book findOne(long id) {
        return bookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
    }

    public Boolean create(Book book) {
        List<DeniedBook> deniedBooksList = new LinkedList<>();
        deniedBooksList = deniedBookRepository.findAll();

        for(DeniedBook bookIterate : deniedBooksList) {
            if(book.getTitle().equals(bookIterate.getTitle()) && book.getAuthor().equals(bookIterate.getAuthor())) {
                return false;
            }
        }
        bookRepository.save(book);
        return true;
    }

    public void delete(long id) {
        bookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    public Book updateBook(Book book, long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }
}
