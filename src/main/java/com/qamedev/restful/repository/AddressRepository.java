package com.qamedev.restful.repository;

import com.qamedev.restful.entity.AddressEntity;
import com.qamedev.restful.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findAllByUserId(UserEntity user);

    Optional<AddressEntity> findByUserIdAndAddressId(long userId, String addressId);

}
