package br.com.rsanme.fullreference.auth.controllers;

import br.com.rsanme.fullreference.auth.dtos.Login;
import br.com.rsanme.fullreference.auth.jwt.TokenService;
import br.com.rsanme.fullreference.auth.services.UserAppService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 01/12/2023
 * Hora: 14:51
 */
@SpringBootTest
class AuthControllerTestIT {

    @Autowired
    WebApplicationContext context;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    TokenService tokenService;

    @Mock
    UserAppService userAppService;

    @InjectMocks
    AuthController controller;

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    Login login;
    String json;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders
//                .standaloneSetup(controller)
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        createInstances();
    }

    @Test
    void login() throws Exception {

        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(json)

        ).andDo(print()).andExpect(status().isOk());
    }

    private void createInstances() throws JsonProcessingException {
        login = new Login("rsanme", "subsanme");
        json = objectMapper.writeValueAsString(login);
    }
}