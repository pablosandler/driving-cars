package com.mytaxi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytaxi.datatransferobject.JwtAuthenticationRequest;
import com.mytaxi.datatransferobject.JwtAuthenticationResponse;
import com.mytaxi.security.JwtTokenUtil;
import com.mytaxi.security.JwtUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    private MediaType contentType = new MediaType(APPLICATION_JSON.getType(),
            APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @Before
    public void setup() {
        AuthenticationController  authenticationController = new AuthenticationController(
                authenticationManager, jwtTokenUtil, userDetailsService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void whenCreatingAuthenticationTokenReturnTokenStringIfAuthenticationIsSuccessful() throws Exception {
        JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest("name", "password");

        Authentication authentication = new UsernamePasswordAuthenticationToken("name","password");
        when(authenticationManager.authenticate(anyObject())).thenReturn(authentication);

        JwtUser jwtUser = new JwtUser("name", "password", true);
        when(userDetailsService.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(jwtUser);

        when(jwtTokenUtil.generateToken(jwtUser)).thenReturn("mytoken");

        MvcResult result = this.mockMvc.perform(post("/v1/auth")
                .accept(contentType).contentType(contentType).content(asJsonString(authenticationRequest)))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        JwtAuthenticationResponse response = asObject(content);

        assertEquals("mytoken", response.getToken());
    }

    public static String asJsonString(final JwtAuthenticationRequest request) {
        try {
            return new ObjectMapper().writeValueAsString(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JwtAuthenticationResponse asObject(String response) {
        try {
            return new ObjectMapper().readValue(response, JwtAuthenticationResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}