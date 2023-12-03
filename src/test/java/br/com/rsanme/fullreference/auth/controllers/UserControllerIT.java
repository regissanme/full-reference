package br.com.rsanme.fullreference.auth.controllers;

import br.com.rsanme.fullreference.auth.jwt.TokenService;
import br.com.rsanme.fullreference.utils.MockUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 03/12/2023
 * Hora: 09:13
 */
@SpringBootTest
class UserControllerIT {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TokenService tokenService;

    MockMvc mockMvc;

    private String json;
    private String token;

    @BeforeEach
    void setUp() throws JsonProcessingException {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        createInstances();
    }

    @Test
    void whenCreateUserThenSuccess() throws Exception {

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(json)
                ).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void whenFindAllUsersThenForbidden() throws Exception {

        mockMvc.perform(
                        get("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                ).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("rsanme")
    void whenFindAllUsersThenSuccess() throws Exception {

        mockMvc.perform(
                        get("/users")
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                ).andDo(print())
                .andExpect(status().isOk());
    }

    private void createInstances() throws JsonProcessingException {
        json = objectMapper.writeValueAsString(MockUser.getUserRequest());
        token = "Bearer " + tokenService.generateToken(MockUser.getAdminUser());
    }
}