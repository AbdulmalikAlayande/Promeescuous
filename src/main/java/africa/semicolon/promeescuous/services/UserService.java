package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.RegisterUserRequest;
import africa.semicolon.promeescuous.dtos.responses.RegisterUserResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public interface UserService {
    RegisterUserResponse register(RegisterUserRequest registerUserRequest) throws URISyntaxException, IOException;
}
