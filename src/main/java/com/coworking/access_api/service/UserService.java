package com.coworking.access_api.service;

import java.util.List;

import com.coworking.access_api.dto.UserDTO;
import com.coworking.access_api.model.User;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    List<UserDTO> getActiveUsers();
    List<UserDTO> getUsersByMembership(User.MembershipType membership);
}
