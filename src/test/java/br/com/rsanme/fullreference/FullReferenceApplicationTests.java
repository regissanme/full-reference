package br.com.rsanme.fullreference;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FullReferenceApplicationTests {

    @Test
    void whenStartApplicationTheContextLoads() {
        FullReferenceApplication.main(new String[]{});
    }

}
