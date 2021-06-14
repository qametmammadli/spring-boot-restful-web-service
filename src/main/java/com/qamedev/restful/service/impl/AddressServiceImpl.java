package com.qamedev.restful.service.impl;

import com.qamedev.restful.dto.AddressDto;
import com.qamedev.restful.entity.AddressEntity;
import com.qamedev.restful.entity.UserEntity;
import com.qamedev.restful.exception.ErrorMessages;
import com.qamedev.restful.exception.UserServiceException;
import com.qamedev.restful.repository.AddressRepository;
import com.qamedev.restful.repository.UserRepository;
import com.qamedev.restful.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper mapper;

    public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.mapper = mapper;
    }

    @Override
    public List<AddressDto> getAddressesByUserId(String userId) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(userId);
        if (optionalUserEntity.isEmpty())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

//            return optionalUserEntity.get().getAddresses().stream()
//                    .map(AddressMapper.INSTANCE::mapAddressEntityToAddressDto)
//                    .collect(Collectors.toList());
        return optionalUserEntity.get().getAddresses().stream()
                .map(addressEntity -> mapper.map(addressEntity, AddressDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public AddressDto getAddress(String userId, String addressId) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(userId);
        if (optionalUserEntity.isEmpty())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        Optional<AddressEntity> optionalAddressEntity = addressRepository.findByUserIdAndAddressId(optionalUserEntity.get().getId(), addressId);
        if (optionalAddressEntity.isEmpty())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        return mapper.map(optionalAddressEntity.get(), AddressDto.class);
    }
}
