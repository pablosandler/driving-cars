package com.mytaxi.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(SpringJUnit4ClassRunner.class)
public class JwtAuthenticationEntryPointTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    AuthenticationException authException;


    @Test
    public void whenCallingCommenceSendErrorToTheAsResponse() {
        JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();

        try {
            entryPoint.commence(request, response, authException);

            verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            verifyZeroInteractions(request);
            verifyZeroInteractions(authException);
        } catch (IOException e) {
            fail();
        }
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

}