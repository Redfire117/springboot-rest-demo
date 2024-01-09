package org.grostarin.springboot.demorest.tests;

import org.grostarin.springboot.demorest.domain.DeniedBook;
import org.grostarin.springboot.demorest.services.DeniedBookServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class DeniedBookServiceTests {
    @Autowired
    private DeniedBookServices deniedbookService;
    
    @Test
    public void testCreationNoAttributes() {
        DeniedBook toCreate = new DeniedBook();
        assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy( () -> deniedbookService.create(toCreate));
    }
}
