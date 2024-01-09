package org.grostarin.springboot.demorest.controllers;

import org.grostarin.springboot.demorest.annotations.LogExecutionTime;
import org.grostarin.springboot.demorest.domain.DeniedBook;
import org.grostarin.springboot.demorest.dto.BookSearch;
import org.grostarin.springboot.demorest.services.DeniedBookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/deniedbooks")
public class DeniedBookController {

    @Autowired
    private DeniedBookServices deniedbookServices;

    @GetMapping
    @LogExecutionTime
    public Iterable<DeniedBook> findAll(@Valid BookSearch bookSearchDTO) {
        return deniedbookServices.findAll(bookSearchDTO);
    }

    @GetMapping("/{id}")
    public DeniedBook findOne(@PathVariable long id) {
        return deniedbookServices.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeniedBook create(@RequestBody DeniedBook deniedbook) {
        return deniedbookServices.create(deniedbook);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        deniedbookServices.delete(id);
    }

    @PutMapping("/{id}")
    public DeniedBook updateBook(@RequestBody DeniedBook deniedbook, @PathVariable long id) {
        return deniedbookServices.updateBook(deniedbook, id);
    }
}
