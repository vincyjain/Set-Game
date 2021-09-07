package stacs.starcade.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main Method starts a Spring Boot Application that acts as the server for the Set Game model and
 * handles any API requests that are made to the server.
 */
@SpringBootApplication
@RestController
public class StartSetGameServer {

    public static void main(String[] args) {
        SpringApplication.run(stacs.starcade.api.StartSetGameServer.class, args);
    }
}