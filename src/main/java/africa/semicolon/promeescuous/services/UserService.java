package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.LoginRequest;
import africa.semicolon.promeescuous.dtos.requests.RegisterUserRequest;
import africa.semicolon.promeescuous.dtos.requests.UpdateUserRequest;
import africa.semicolon.promeescuous.dtos.responses.*;
import africa.semicolon.promeescuous.models.Location;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface UserService {
    RegisterUserResponse register(RegisterUserRequest registerUserRequest) throws URISyntaxException, IOException;
    
    LoginResponse login(LoginRequest loginRequest);
    
    ApiResponse<?> activateUserAccount(String token);
    
    GetUserResponse getUserById(Long id);
    
    List<GetUserResponse> getAllUsers(int page, int pageSize);
    
    UpdateUserResponse updateProfile(UpdateUserRequest updateUserRequest, Long id);
    List<GetUserResponse> suggestFriendsBasedOn(Location location);
    void deleteAll();
}
