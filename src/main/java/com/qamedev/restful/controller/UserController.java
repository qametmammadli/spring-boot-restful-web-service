package com.qamedev.restful.controller;

import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.mapper.UserMapper;
import com.qamedev.restful.service.UserService;
import com.qamedev.restful.ui.request.UserDetailsRequest;
import com.qamedev.restful.ui.response.UserResponse;
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

        return userDtoPage.map(UserMapper.INSTANCE::mapUserDtoToUserResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody UserDetailsRequest userDetails){
        UserDto userDto = UserMapper.INSTANCE.mapUserRequestToUserDto(userDetails);
        userDto = userService.createUser(userDto);
        return UserMapper.INSTANCE.mapUserDtoToUserResponse(userDto);
    }

    @GetMapping(path = "{id}")
//    @GetMapping(path = "{id}", produces = { MediaType.APPLICATION_XML_VALUE})
    public UserResponse getUser(@PathVariable String id){
        UserDto userDto = userService.getUserByUserId(id);
        return UserMapper.INSTANCE.mapUserDtoToUserResponse(userDto);
    }

    @PutMapping("{id}")
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequest userDetails){
        UserDto userDto = UserMapper.INSTANCE.mapUserRequestToUserDto(userDetails);
        userDto = userService.updateUser(id, userDto);
        return UserMapper.INSTANCE.mapUserDtoToUserResponse(userDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }
}
