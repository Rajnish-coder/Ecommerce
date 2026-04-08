package com.app.ecom.service;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponse> fetchAllUsers();
    UserResponse addUser(UserRequest userRequest);
    Optional<UserResponse> fetchUser(Long id);
    boolean updateUser(Long id,UserRequest userRequest);
}
