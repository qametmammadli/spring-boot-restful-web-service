package com.qamedev.restful.controller;

import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.service.UserService;
import com.qamedev.restful.ui.request.UserDetailsRequest;
import com.qamedev.restful.ui.response.UserResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserResponse> getUsers(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                       @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
                                       @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
                                       @RequestParam(name = "sortColumn", required = false, defaultValue = "id") String sortColumn){

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.Direction.fromString(sortDirection), sortColumn);
        Page<UserDto> userDtoPage = userService.getUsers(pageRequest);

        return userDtoPage.map(userDto -> {
            UserResponse response = new UserResponse();
            BeanUtils.copyProperties(userDto, response);
            return response;
        });
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody UserDetailsRequest userDetails){
        UserResponse response = new UserResponse();
        // todo mapStruct
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

    @PutMapping("{id}")
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequest userDetails){
        UserResponse response = new UserResponse();
        // todo mapStruct
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, response);
        return response;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }
}
