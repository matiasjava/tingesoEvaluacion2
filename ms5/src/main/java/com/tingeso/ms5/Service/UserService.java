package com.tingeso.ms5.Service;

import com.tingeso.ms5.Entity.UserEntity;
import com.tingeso.ms5.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity incrementVisitsAndUpdateCategory(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        int newVisits = user.getNumberVisits() + 1;
        user.setNumberVisits(newVisits);

        String nuevaCategoria;
        if (newVisits >= 7) {
            nuevaCategoria = "Muy frecuente";
        } else if (newVisits >= 5) {
            nuevaCategoria = "Frecuente";
        } else if (newVisits >= 2) {
            nuevaCategoria = "Regular";
        } else {
            nuevaCategoria = "No frecuente";
        }

        user.setCategory_frecuency(nuevaCategoria);
        return userRepository.save(user);
    }

    public UserEntity getUserByRut(String rut) {
        return userRepository.findByRut(rut).orElse(null);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }


}
