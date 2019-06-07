package com.erick.whitecode;

import com.erick.whitecode.repository.UserRepository;
import com.erick.whitecode.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class DataInit implements CommandLineRunner {


    @Autowired
    UserRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final Logger log = LoggerFactory.getLogger(DataInit.class);

    @Override
    public void run(String... args) throws Exception {

        log.info("DataInitializer starting.");

        createUserIfNotFound("admin", "admin@projuris.com.br", true, "admin", "admin", Arrays.asList( "ROLE_USER", "ROLE_ADMIN"));

        createUserIfNotFound("testUser", "teste_user@projuris.com.br", true, "testuser", "testuser", Arrays.asList( "ROLE_USER"));

        log.info("Printing all users...");
        this.users.findAll().forEach(v -> log.info(" User :" + v.toString()));
    }
    private void createUserIfNotFound(String name, String email, boolean active, String username, String rawPassword, List<String> roles) {
        User user;
        try {
            user = this.users.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
        } catch (UsernameNotFoundException e) {
            log.info(e.getLocalizedMessage());
            log.info("Creating user ".concat(username));
            user = new User(name, email, active, username, this.passwordEncoder.encode(rawPassword), roles);
            this.users.save(user);
            log.info("User ".concat(username).concat(" created.") );
        }
    }

}
