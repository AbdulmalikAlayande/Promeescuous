package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.RegisterUserRequest;
import africa.semicolon.promeescuous.dtos.responses.RegisterUserResponse;
import africa.semicolon.promeescuous.models.User;
import africa.semicolon.promeescuous.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URISyntaxException;

@Repository
@AllArgsConstructor
@Slf4j
public class PromiscuousUserService implements UserService{
    private final UserRepository userRepository;
    private final MailService mailService;
    private final ModelMapper mapper;

    @Override
    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) throws URISyntaxException, IOException {
        String email = registerUserRequest.getEmail();
        String password = registerUserRequest.getPassword();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        User savedUser = userRepository.save(user);
        log.info("saved guy-->{}", savedUser);
        String emailResponse = mailService.send(savedUser);
        log.info("email sending response->{}", emailResponse);
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();
        registerUserResponse.setMessage("Registration Successful, check your email inbox for verification token");
        
        return registerUserResponse;
    }
}
