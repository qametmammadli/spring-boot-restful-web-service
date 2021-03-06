package com.qamedev.restful.controller;

import com.qamedev.restful.dto.AddressDto;
import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.service.AddressService;
import com.qamedev.restful.service.UserService;
import com.qamedev.restful.ui.request.PasswordResetRequest;
import com.qamedev.restful.ui.request.UserDetailsRequest;
import com.qamedev.restful.ui.response.AddressResponse;
import com.qamedev.restful.ui.response.StatusResponse;
import com.qamedev.restful.ui.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final ModelMapper mapper;

    public UserController(UserService userService, AddressService addressService, ModelMapper mapper) {
        this.userService = userService;
        this.addressService = addressService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<UserResponse> getUsers(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                       @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
                                       @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
                                       @RequestParam(name = "sortColumn", required = false, defaultValue = "id") String sortColumn) {

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.Direction.fromString(sortDirection), sortColumn);
        Page<UserDto> userDtoPage = userService.getUsers(pageRequest);

//        return userDtoPage.map(UserMapper.INSTANCE::mapUserDtoToUserResponse);
        return userDtoPage.map(userDto -> mapper.map(userDto, UserResponse.class));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody UserDetailsRequest userDetails) {
//        UserDto userDto = UserMapper.INSTANCE.mapUserRequestToUserDto(userDetails);

        UserDto userDto = mapper.map(userDetails, UserDto.class);
        userDto = userService.createUser(userDto);

//        return UserMapper.INSTANCE.mapUserDtoToUserResponse(userDto);
        return mapper.map(userDto, UserResponse.class);
    }

    @GetMapping(path = "/{id}")
//    @GetMapping(path = "{id}", produces = { MediaType.APPLICATION_XML_VALUE})
    public UserResponse getUser(@PathVariable String id) {
        UserDto userDto = userService.getUserByUserId(id);
//        return UserMapper.INSTANCE.mapUserDtoToUserResponse(userDto);
        return mapper.map(userDto, UserResponse.class);
    }

    @GetMapping(path = "/activate")
    public StatusResponse activateUser(@RequestParam(name = "token") String token){
        boolean isActivated = userService.activateUser(token);
        if(!isActivated){
            return StatusResponse.ERROR;
        }
        return StatusResponse.SUCCESS;
    }

    @GetMapping("/{userId}/addresses")
    public CollectionModel<AddressResponse> getUserAddresses(@PathVariable String userId) {
        List<AddressDto> addressesDtoList = addressService.getAddressesByUserId(userId);

        List<AddressResponse> addressResponseList = addressesDtoList.stream()
                .map(addressDto ->
                    {
                        AddressResponse addressResponse = mapper.map(addressDto, AddressResponse.class);
                        Link addressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .getUserAddress(userId, addressResponse.getAddressId())).withSelfRel();

                        return addressResponse.add(addressLink);
                    }
                ).collect(Collectors.toList());

        Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(userId)).withRel("user");
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId)).withSelfRel();

        return CollectionModel.of(addressResponseList, userLink, selfLink);
    }

    @GetMapping("/{userId}/addresses/{addressId}")
    public EntityModel<AddressResponse> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
        AddressDto addressDto = addressService.getAddress(userId, addressId);

        Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUser(userId))
                .withRel("user");

        Link allUserAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserAddresses(userId))
                .withRel("addresses");

        AddressResponse addressResponse = mapper.map(addressDto, AddressResponse.class);
        return EntityModel.of(addressResponse, userLink, allUserAddressesLink);
    }

    @PostMapping(path = "/password-reset")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public StatusResponse passwordReset(@RequestBody PasswordResetRequest passwordReset){
        return userService.passwordReset(passwordReset.getEmail()) ? StatusResponse.SUCCESS : StatusResponse.ERROR;
    }

    @PutMapping(path = "/save-password")
    public StatusResponse savePassword(@RequestBody PasswordResetRequest passwordReset){
        return userService.savePassword(passwordReset.getToken(), passwordReset.getPassword()) ? StatusResponse.SUCCESS : StatusResponse.ERROR;
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequest userDetails) {
//        UserDto userDto = UserMapper.INSTANCE.mapUserRequestToUserDto(userDetails);
        UserDto userDto = mapper.map(userDetails, UserDto.class);
        userDto = userService.updateUser(id, userDto);
//        return UserMapper.INSTANCE.mapUserDtoToUserResponse(userDto);
        return mapper.map(userDto, UserResponse.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
