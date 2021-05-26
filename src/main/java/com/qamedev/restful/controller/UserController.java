package com.qamedev.restful.controller;

import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.service.UserService;
import com.qamedev.restful.ui.request.UserDetailsRequest;
import com.qamedev.restful.ui.response.UserResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUsers(){
        return "get all users";
    }

    @PostMapping
    public UserResponse createUser(@RequestBody UserDetailsRequest userDetails){
        UserResponse response = new UserResponse();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, response);
        return response;
    }

    @GetMapping(path = "{id}")
//    @GetMapping(path = "{id}", produces = { MediaType.APPLICATION_XML_VALUE})
    public UserResponse getUser(@PathVariable String id){
        UserResponse response = new UserResponse();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, response);
        return response;
    }

    @PutMapping
    public String updateUser(){
        return "Update user";
    }

    @DeleteMapping
    public String deleteUser(){
        return "Delete user";
    }
}
