package com.restaurantdelivery.service;

import com.restaurantdelivery.configuration.AppConfiguration;
import com.restaurantdelivery.entity.Role;
import com.restaurantdelivery.entity.User;
import com.restaurantdelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AppConfiguration bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        } else {
            return null;
        }
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.bCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public User createAdmin() {
        User admin = new User();
        admin.setRoles(Collections.singleton(new Role(2L, "ROLE_ADMIN")));
        admin.setUsername("admin@gmail.com");
        admin.setName("admin");
        admin.setSurname("admin");
        admin.setPassword(bCryptPasswordEncoder.bCryptPasswordEncoder().encode("M96oqzl8ID8r"));
        admin = userRepository.save(admin);
        return admin;
    }

    public boolean saveWorker(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }
        user.setRoles(Collections.singleton(new Role(3L, "ROLE_WORKER")));
        user.setPassword(bCryptPasswordEncoder.bCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public List<User> getWorkers() {
        List<User> workerList = userRepository.findByRolesName("ROLE_WORKER");
        return workerList;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public void refreshPassword(User user) {
        user.setPassword(bCryptPasswordEncoder.bCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public void refresh(User user) {
        userRepository.save(user);
    }
}