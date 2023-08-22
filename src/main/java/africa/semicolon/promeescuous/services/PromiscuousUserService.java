package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.*;
import africa.semicolon.promeescuous.dtos.responses.*;
import africa.semicolon.promeescuous.models.Interests;
import africa.semicolon.promeescuous.models.User;
import africa.semicolon.promeescuous.repositories.UserRepository;

import africa.semicolon.promeescuous.exceptions.AccountActivationFailedException;
import africa.semicolon.promeescuous.exceptions.BadCredentialsException;
import africa.semicolon.promeescuous.exceptions.UserNotFoundException;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

import static africa.semicolon.promeescuous.dtos.responses.ResponseMessages.ACCOUNT_ACTIVATION_SUCCESSFUL;
import static africa.semicolon.promeescuous.exceptions.ExceptionMessage.*;
import static africa.semicolon.promeescuous.utils.AppUtil.*;
import static java.util.regex.Pattern.matches;

import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

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
        String emailResponse = mailService.send(savedUser);
        log.info("email sending response->{}", emailResponse);
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();
        registerUserResponse.setMessage("Registration Successful, check your email inbox for verification token");
        
        return registerUserResponse;
    }
    
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Optional<User> foundUser = userRepository.readByEmail(email);
        User user = foundUser.orElseThrow(()->new UserNotFoundException(
                String.format(USER_WITH_EMAIL_NOT_FOUND_EXCEPTION.getMessage(), email)
        ));
        boolean isValidPassword = matches(user.getPassword(), password);
        if (isValidPassword) return buildLoginResponse(email);
        throw new BadCredentialsException(INVALID_CREDENTIALS_EXCEPTION.getMessage());
    }
    
    private static LoginResponse buildLoginResponse(String email) {
        String accessToken = generateValidationToken(email);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        return loginResponse;
    }
    
    
    @Override
    public ApiResponse<?> activateUserAccount(String token) {
        boolean isTestToken = token.equals(appConfig.getTestToken());
        if (isTestToken) return activateTestAccount();
        boolean isValidJwt = isValidJwt(token);
        if (isValidJwt) return activateAccount(token);
        throw new AccountActivationFailedException(
                ACCOUNT_ACTIVATION_FAILED_EXCEPTION.getMessage());
    }
    
    @Override
    public GetUserResponse getUserById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        User user = foundUser.orElseThrow(
                ()->new UserNotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage())
        );
        System.out.println(userRepository.findAll());
	    return buildGetUserResponse(user);
    }
    
    @Override
    public List<GetUserResponse> getAllUsers(int page, int pageSize) {
        List<GetUserResponse> users = new ArrayList<>();
        Pageable pageable = buildPageRequest(page, pageSize);
        Page<User> usersPage = userRepository.findAll(pageable);
        List<User> foundUsers = usersPage.getContent();
//        for (User user:foundUsers) {
//            GetUserResponse getUserResponse = buildGetUserResponse(user);
//            users.add(getUserResponse);
//        }
//      return users;
        return foundUsers.stream()
                       .map(PromiscuousUserService::buildGetUserResponse)
                       .toList();
    }
    
    
    
    @Override
    public UpdateUserResponse updateProfile(UpdateUserRequest updateUserRequest, Long id) {
        User user = findUserById(id);
        Set<String> userInterests = updateUserRequest.getInterests();
        Set<Interests> interests = parseInterestsFrom(userInterests);
        user.setInterests(interests);
        JsonPatch updatePatch = buildUpdatePatch(updateUserRequest);
        return applyPatch(updatePatch, user);
    }
    
    private UpdateUserResponse applyPatch(JsonPatch updatePatch, User user) {
        return null;
    }
    
    private Set<Interests> parseInterestsFrom(Set<String> userInterests) {
	    return userInterests
                       .stream()
                       .map(interest-> Interests.valueOf(interest.toUpperCase()))
                       .collect(Collectors.toSet());
    }
    
    private JsonPatch buildUpdatePatch(UpdateUserRequest updateUserRequest) {
        JsonPatch patch;
        Field[] fields = updateUserRequest.getClass().getDeclaredFields();
        
        List<ReplaceOperation> operations=Arrays.stream(fields)
                                                  .filter(field -> isFieldWithValue(field, updateUserRequest))
                                                  .map(field->{
                                                      try {
                                                          String path = "/"+field.getName();
                                                          JsonPointer pointer = new JsonPointer(path);
                                                          String value = field.get(field.getName()).toString();
                                                          TextNode node = new TextNode(value);
	                                                      return new ReplaceOperation(pointer, node);
                                                      } catch (Exception exception) {
                                                          throw new RuntimeException(exception);
                                                      }
                                                  }).toList();
        
        List<JsonPatchOperation> patchOperations = new ArrayList<>(operations);
        return new JsonPatch(patchOperations);
    }
    
    private boolean isFieldWithValue(Field field, UpdateUserRequest updateUserRequest) {
        field.setAccessible(true);
        try {
            return field.get(updateUserRequest) != null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    private User findUserById(Long id){
        Optional<User> foundUser = userRepository.findById(id);
        User user = foundUser.orElseThrow(()->
                                                  new UserNotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage()));
        return user;
    }
    
    
    private Pageable buildPageRequest(int page, int pageSize) {
        if (page<1&&pageSize<1)return PageRequest.of(0, 10);
        if (page<1)return PageRequest.of(0, pageSize);
        if (pageSize<1) return PageRequest.of(page, pageSize);
        return PageRequest.of(page-1, pageSize);
    }
    
    
    private ApiResponse<?> activateAccount(String token) {
        String email = extractEmailFrom(token);
        Optional<User> user = userRepository.readByEmail(email);
        User foundUser = user.orElseThrow(()->new UserNotFoundException(
                String.format(USER_WITH_EMAIL_NOT_FOUND_EXCEPTION.getMessage(), email)
        ));
        foundUser.setActive(true);
        User savedUser = userRepository.save(foundUser);
        GetUserResponse userResponse = buildGetUserResponse(savedUser);
        var activateUserResponse = buildActivateUserResponse(userResponse);
        return ApiResponse.builder().data(activateUserResponse).build();
    }
    
    private static ActivateAccountResponse buildActivateUserResponse(GetUserResponse userResponse) {
        return ActivateAccountResponse.builder()
                       .message(ACCOUNT_ACTIVATION_SUCCESSFUL.name())
                       .user(userResponse)
                       .build();
    }
    
    private static GetUserResponse buildGetUserResponse(User savedUser) {
        return GetUserResponse.builder()
                       .id(savedUser.getId())
                       .address(savedUser.getAddress().toString())
                       .fullName(getFullName(savedUser))
                       .phoneNumber(savedUser.getPhoneNumber())
                       .email(savedUser.getEmail())
                       .build();
    }
    
    private static String getFullName(User savedUser) {
        return savedUser.getFirstName() + " " + savedUser.getLastName();
    }
    
    private static ApiResponse<?> activateTestAccount() {
        return ApiResponse.builder()
                       .build();
    }
}
