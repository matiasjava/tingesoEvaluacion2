package com.tingeso.ms5.Controller;

import com.tingeso.ms5.Entity.UserEntity;
import com.tingeso.ms5.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserEntity> addUser(@RequestBody UserEntity user) {
        if (user.getRut() == null || user.getRut().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        UserEntity savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/increment-visits")
    public ResponseEntity<?> incrementVisitsAndUpdateCategory(@PathVariable("id") Long userId) {
        try {
            UserEntity updatedUser = userService.incrementVisitsAndUpdateCategory(userId);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findByRut/{rut}")
    public ResponseEntity<UserEntity> getUserByRut(@PathVariable String rut) {
        UserEntity user = userService.getUserByRut(rut);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable("id") long id) {
        return userService.findUserById(id);
    }
}