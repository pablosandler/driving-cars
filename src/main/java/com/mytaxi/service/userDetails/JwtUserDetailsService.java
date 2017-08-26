package com.mytaxi.service.userDetails;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.security.JwtUser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(JwtUserDetailsService.class);

    private final DriverRepository driverRepository;

    @Autowired
    public JwtUserDetailsService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<DriverDO> driverOpt = Optional.ofNullable(driverRepository.findByUsername(name));

        DriverDO driver = driverOpt.orElseThrow( () -> {
            LOG.warn("Could not find user with name: " + name);
            return new UsernameNotFoundException("Could not find user with name: " + name);
        });

        return new JwtUser(driver.getUsername(), driver.getPassword(), !driver.getDeleted());
    }
}
