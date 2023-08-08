package africa.semicolon.promeescuous.services;


import africa.semicolon.promeescuous.dtos.requests.RegisterUserRequest;
import africa.semicolon.promeescuous.dtos.responses.RegisterUserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testThatUserCanRegister() throws URISyntaxException, IOException {
        //user fills registration form
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("test@email.com");
        registerUserRequest.setPassword("password");
        //user submits form by calling register method
        RegisterUserResponse registerUserResponse = userService.register(registerUserRequest);

        assertNotNull(registerUserResponse);
        assertNotNull(registerUserResponse.getMessage());
    }
}
