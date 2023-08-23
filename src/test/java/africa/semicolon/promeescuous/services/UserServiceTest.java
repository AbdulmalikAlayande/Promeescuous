package africa.semicolon.promeescuous.services;


import africa.semicolon.promeescuous.dtos.requests.LoginRequest;
import africa.semicolon.promeescuous.dtos.requests.RegisterUserRequest;
import africa.semicolon.promeescuous.dtos.requests.UpdateUserRequest;
import africa.semicolon.promeescuous.dtos.responses.*;
import africa.semicolon.promeescuous.exceptions.BadCredentialsException;
import africa.semicolon.promeescuous.exceptions.PromiscuousBaseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
    
    @Test
    public void testActivateUserAccount(){
        ApiResponse<?> activateUserAccountResponse =
                userService.activateUserAccount("abc1234.erytuuoi.67t75646");
        assertThat(activateUserAccountResponse).isNotNull();
    }
    
    @Test
    public void testThatExceptionIsThrownWhenUserAuthenticatesWithBadCredentials(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("bad_password");
        
        assertThatThrownBy(()->userService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class);
    }
    
    @Test
    public void testThatUsersCanLogin(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("password");
        
        LoginResponse response = userService.login(loginRequest);
        assertThat(response).isNotNull();
        String accessToken = response.getAccessToken();
        assertThat(accessToken).isNotNull();
    }
    
    @Test
    public void getUserByIdTest(){
        GetUserResponse response = userService.getUserById(500L);
        assertThat(response).isNotNull();
    }
    
    @Test
    public void getAllUsers(){
        List<GetUserResponse> users = userService.getAllUsers(1, 5);
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(5);
    }
    
    @Test
    public void testThatUserCanUpdateAccount(){
        UpdateUserRequest updateUserRequest = buildUpdateRequest();
        UpdateUserResponse response = userService.updateProfile(updateUserRequest, 500L);
        assertThat(response).isNotNull();
        GetUserResponse userResponse = userService.getUserById(500L);
        
        String fullName = userResponse.getFullName();
        String expectedFullName = updateUserRequest.getFirstName()+" "+updateUserRequest.getLastName();
        assertThat(fullName).isEqualTo(expectedFullName);
    }
    
    private UpdateUserRequest buildUpdateRequest() {
        Set<String> interests = Set.of("swimming", "sports", "cooking");
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setDateOfBirth(LocalDate.of(2005, Month.NOVEMBER.ordinal(), 25));
        updateUserRequest.setFirstName("Sheriff");
        updateUserRequest.setLastName("Awofiranye");
        updateUserRequest.setPassword("password");
        MultipartFile testImage = getTestImage();
        updateUserRequest.setProfileImage(testImage);
        updateUserRequest.setInterests(interests);
        updateUserRequest.setCountry("Ghana");
        return updateUserRequest;
    }
    
    private MultipartFile getTestImage(){
        //obtain a path that points to test image
        Path path = Paths.get("C:\\Users\\semicolon\\Documents\\spring_projects\\prom-scuous\\src\\test\\resources\\images\\puppy_flex.jpg");
        //create stream that can read from file pointed to by path
        try(InputStream inputStream = Files.newInputStream(path)) {
            //create a MultipartFile using bytes from file pointed to by path
	        return new MockMultipartFile("test_image", inputStream);
        }catch (Exception exception){
            throw new PromiscuousBaseException(exception.getMessage());
        }
    }
}
