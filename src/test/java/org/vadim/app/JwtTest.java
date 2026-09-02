package org.vadim.app;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.vadim.config.security.JwtServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtTest {

    private final String PUBLIC_ENDPOINT = "/health";
    private final String SECURED_ENDPOINT = "/health/secured";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtServiceImpl jwtService;

    @Test
    void callPublicEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PUBLIC_ENDPOINT))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void callSecuredEndpointWithoutJWT() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SECURED_ENDPOINT))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void callSecuredEndpointWithJWT() throws Exception{
        var token = jwtService.generateToken("1");
        mockMvc.perform(MockMvcRequestBuilders.get(SECURED_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //TODO: Поправить тест, после добавления глобального обработчика ошибок
    @Test
    void expiredJwt() throws Exception {
        var token = jwtService.generateToken("1");
        Thread.sleep(jwtService.getJwtExpMs() + 1);
        Assertions.assertThrows(ExpiredJwtException.class, () -> mockMvc.perform(MockMvcRequestBuilders.get(SECURED_ENDPOINT)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isForbidden()));
    }

    //TODO: Подумать как грамотно реализовть игнор просроченного Jwt на публичных ручках
    @Disabled
    @Test
    void ignoreExpiredJwtInPublicEndpoints() throws Exception {
        var token = jwtService.generateToken("1");
        Thread.sleep(jwtService.getJwtExpMs() + 1);
        mockMvc.perform(MockMvcRequestBuilders.get(PUBLIC_ENDPOINT)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                        .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
