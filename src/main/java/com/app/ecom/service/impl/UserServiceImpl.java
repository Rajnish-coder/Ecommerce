package com.app.ecom.service.impl;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.entity.Address;
import com.app.ecom.entity.User;
import com.app.ecom.repository.UserRepository;
import com.app.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger("userServiceImpl");

    private final UserRepository userRepository;
    @Override
    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll()
                .stream().map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromRequest(user,userRequest);
        User savedUser = userRepository.save(user);
        UserResponse response = new UserResponse();
        if(savedUser != null){
            response = mapToUserResponse(savedUser);
        }
        return response;
    }

    @Override
    public Optional<UserResponse> fetchUser(Long id) {
        logger.log(Level.INFO,"Inside fetch user!");
        Optional<UserResponse> fetchedUser = userRepository.findById(id)
                .map(this::mapToUserResponse);
        logger.log(Level.INFO,"fetched User: " + fetchedUser);
        return fetchedUser;
    }

    @Override
    public boolean updateUser(Long id,UserRequest newUserRequest) {
        logger.log(Level.INFO,"Inside update user!");
        return  userRepository.findById(id)
                .map(user -> {
                    updateUserFromRequest(user,newUserRequest);
                    userRepository.save(user);
                    return true;
                }).orElse(false);
    }

    private UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId().toString());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole().toString());
        if(user.getAddress() != null)
            response.setAddress(mapToAddressDTO(user.getAddress()));
        return response;
    }

    private AddressDTO mapToAddressDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setZipCode(address.getZipCode());
        return addressDTO;
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getAddress() != null){
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipCode(userRequest.getAddress().getZipCode());
            user.setAddress(address);
        }
    }
}
