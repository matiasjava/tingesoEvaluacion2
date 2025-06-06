package com.tingeso.ms5.Repository;

import com.tingeso.ms5.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByRut(String rut);

    Optional<UserEntity> findById(Long id);
}
