package com.mytaxi.service.userDetails;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.service.userDetails.JwtUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class JwtUserDetailsServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Test
    public void whenUserWithGivenNameDoesNotExistThrowException() {
        JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService(driverRepository);

        try {
            jwtUserDetailsService.loadUserByUsername("myname");
        } catch(UsernameNotFoundException e){
            assertNotNull("Could not find user with name: myname", e.getMessage());
        }
    }

    @Test
    public void whenUserWithGivenNameExistsReturnHisDetails() {
        JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService(driverRepository);

        DriverDO driver = new DriverDO("myname", "password");
        when(driverRepository.findByUsername("myname")).thenReturn(driver);

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("myname");

        assertEquals("myname",userDetails.getUsername());
        assertEquals("password",userDetails.getPassword());
        assertEquals(Collections.EMPTY_LIST,userDetails.getAuthorities());
        assertEquals(true,userDetails.isAccountNonExpired());
        assertEquals(true,userDetails.isAccountNonLocked());
        assertEquals(true,userDetails.isCredentialsNonExpired());
        assertEquals(true,userDetails.isEnabled());
    }

}