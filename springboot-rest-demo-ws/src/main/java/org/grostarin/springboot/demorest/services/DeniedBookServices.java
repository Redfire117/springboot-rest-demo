package org.grostarin.springboot.demorest.services;

import org.grostarin.springboot.demorest.domain.DeniedBook;
import org.grostarin.springboot.demorest.dto.BookSearch;
import org.grostarin.springboot.demorest.exceptions.BookIdMismatchException;
import org.grostarin.springboot.demorest.exceptions.BookNotFoundException;
import org.grostarin.springboot.demorest.repositories.DeniedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DeniedBookServices {

    @Autowired
    private DeniedBookRepository DeniedbookRepository;
    
    public Iterable<DeniedBook> findAll(BookSearch bookSearchDTO) {
        if(bookSearchDTO!=null && StringUtils.hasText(bookSearchDTO.title())) {
            return DeniedbookRepository.findByTitle(bookSearchDTO.title());
        }
        return DeniedbookRepository.findAll();
    }

    public DeniedBook findOne(long id) {
        return DeniedbookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
    }

    public DeniedBook create(DeniedBook Deniedbook) {
        DeniedBook Deniedbook1 = DeniedbookRepository.save(Deniedbook);
        return Deniedbook1;
    }

    public void delete(long id) {
        DeniedbookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
        DeniedbookRepository.deleteById(id);
    }

    public DeniedBook updateBook(DeniedBook Deniedbook, long id) {
        if (Deniedbook.getId() != id) {
            throw new BookIdMismatchException();
        }
        DeniedbookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
        return DeniedbookRepository.save(Deniedbook);
    }
}
