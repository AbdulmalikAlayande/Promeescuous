package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.config.AppConfig;
import africa.semicolon.promeescuous.dtos.requests.*;
import africa.semicolon.promeescuous.dtos.responses.*;
import africa.semicolon.promeescuous.exceptions.PromiscuousBaseException;
import africa.semicolon.promeescuous.models.Address;
import africa.semicolon.promeescuous.models.Interests;
import africa.semicolon.promeescuous.models.Location;
import africa.semicolon.promeescuous.models.User;
import africa.semicolon.promeescuous.repositories.UserRepository;

import africa.semicolon.promeescuous.exceptions.AccountActivationFailedException;
import africa.semicolon.promeescuous.exceptions.BadCredentialsException;
import africa.semicolon.promeescuous.exceptions.UserNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
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
import static africa.semicolon.promeescuous.dtos.responses.ResponseMessages.USER_REGISTRATION_SUCCESSFUL;
import static africa.semicolon.promeescuous.dtos.responses.SuccessResponse.UPDATE_SUCCESSFUL;
import static africa.semicolon.promeescuous.exceptions.ExceptionMessage.*;
import static africa.semicolon.promeescuous.utils.AppUtil.*;
import static java.util.regex.Pattern.matches;

import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

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
    private AppConfig appConfig;
    private CloudService cloudService;

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
        registerUserResponse.setMessage(USER_REGISTRATION_SUCCESSFUL.getName());
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
        Pageable pageable = buildPageRequest(page, pageSize);
        Page<User> usersPage = userRepository.findAll(pageable);
        List<User> foundUsers = usersPage.getContent();
        return foundUsers.stream()
                       .map(user-> buildGetUserResponse(user))
                       .toList();
    }
    
    
    
    @Override
    public UpdateUserResponse updateProfile(UpdateUserRequest updateUserRequest, Long id) {
        User user = findUserById(id);
        
        ApiResponse<String> url = uploadImage(updateUserRequest.getProfileImage());
        Set<String> userInterests = updateUserRequest.getInterests();
        Set<Interests> interests = parseInterestsFrom(userInterests);
        user.setInterests(interests);
        
        Address userAddress = user.getAddress();
        mapper.map(updateUserRequest, userAddress);
        user.setAddress(userAddress);
        JsonPatch updatePatch = buildUpdatePatch(updateUserRequest);
        return applyPatch(updatePatch, user);
    }
    
    private ApiResponse<String> uploadImage(MultipartFile profileImage) {
        boolean isFormWithProfileImage = profileImage !=null;
        if (isFormWithProfileImage) return cloudService.upload(profileImage);
        throw new RuntimeException(UPLOAD_FAILED_EXCEPTION.getMessage());
    }
    
    private UpdateUserResponse applyPatch(JsonPatch updatePatch, User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        //1. Convert user to JsonNode
        JsonNode userNode = objectMapper.convertValue(user, JsonNode.class);
        try {
            //2. Apply patch to JsonNode from step 1
            JsonNode updatedNode = updatePatch.apply(userNode);
            //3. Convert updatedNode to user
            user = objectMapper.convertValue(updatedNode, User.class);
            log.info("user-->{}", user);
            //4. Save updatedUser from step 3 in the DB
            var savedUser=userRepository.save(user);
            log.info("user-->{}", savedUser);
            return new UpdateUserResponse(UPDATE_SUCCESSFUL.getName());
        }catch (JsonPatchException exception){
            throw new PromiscuousBaseException(exception.getMessage());
        }
    }
    
    private Set<Interests> parseInterestsFrom(Set<String> userInterests) {
	    return userInterests
                       .stream()
                       .map(interest-> Interests.valueOf(interest.toUpperCase()))
                       .collect(Collectors.toSet());
    }
    
    private JsonPatch buildUpdatePatch(UpdateUserRequest updateUserRequest) {
        Field[] fields = updateUserRequest.getClass().getDeclaredFields();
        
        List<ReplaceOperation> operations=Arrays.stream(fields)
                                                .filter(field -> isFieldWithValue(field, updateUserRequest))
                                                .map(field-> buildReplaceOperation(updateUserRequest, field))
                                                .toList();
        
        List<JsonPatchOperation> patchOperations = new ArrayList<>(operations);
        return new JsonPatch(patchOperations);
    }
    
    
    private static ReplaceOperation buildReplaceOperation(UpdateUserRequest updateUserRequest, Field field) {
        field.setAccessible(true);
        try {
            log.info("field::{}", field);
            String path = "/"+field.getName();
            JsonPointer pointer = new JsonPointer(path);
            Object value = field.get(updateUserRequest);
            TextNode node = new TextNode(value.toString());
            return new ReplaceOperation(pointer, node);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    
    private boolean isFieldWithValue(Field field, UpdateUserRequest updateUserRequest) {
        field.setAccessible(true);
        try {
            return field.get(updateUserRequest) != null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<GetUserResponse> suggestFriendsBasedOn(Location location) {
        
        return null;
    }
    
    private User findUserById(Long id){
        Optional<User> foundUser = userRepository.findById(id);
	    return foundUser.orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage()));
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
    
    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
    
    private static ActivateAccountResponse buildActivateUserResponse(GetUserResponse userResponse) {
        return ActivateAccountResponse.builder()
                                      .message(ACCOUNT_ACTIVATION_SUCCESSFUL.name())
                                      .user(userResponse)
                                      .build();
    }
    
    private GetUserResponse buildGetUserResponse(User savedUser) {
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
