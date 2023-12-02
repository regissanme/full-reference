package br.com.rsanme.fullreference.controllers;

import br.com.rsanme.fullreference.auth.models.UserApp;
import br.com.rsanme.fullreference.auth.repository.UserAppRepository;
import br.com.rsanme.fullreference.dtos.ProjectCreateDto;
import br.com.rsanme.fullreference.utils.MockProject;
import br.com.rsanme.fullreference.utils.MockUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 01/12/2023
 * Hora: 16:11
 */
@SpringBootTest
class ProjectControllerTestIT {

    @Autowired
    WebApplicationContext context;

    @Autowired
    private UserAppRepository repository;

    MockMvc mockMvc;

    ProjectCreateDto projectCreateDto;
    UserApp user;
    UserApp adminUser;


    @BeforeEach
    void setUp() {

        createInstances();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithUserDetails("teste")
    @Test
    void whenFindAllWithUserThenSuccess() throws Exception {

        mockMvc.perform(
                get("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")

        ).andDo(print()).andExpect(status().isOk());
    }

    private void createInstances() {
        projectCreateDto = MockProject.getProjectCreateDto();
    }

}