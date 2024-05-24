package com.restaurantdelivery.service;

import com.restaurantdelivery.configuration.AppConfiguration;
import com.restaurantdelivery.entity.Role;
import com.restaurantdelivery.entity.User;
import com.restaurantdelivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppConfiguration bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentUsername() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testUser");

        String result = userService.getCurrentUsername();

        assertEquals("testUser", result);
    }

    @Test
    void testSaveUser_UserExists() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        boolean result = userService.saveUser(user);

        assertFalse(result);
        verify(userRepository, never()).save(user);
    }

    @Test
    void testSaveWorker_UserExists() {
        User user = new User();
        user.setUsername("testWorker");
        user.setPassword("password");

        when(userRepository.findByUsername("testWorker")).thenReturn(user);

        boolean result = userService.saveWorker(user);

        assertFalse(result);
        verify(userRepository, never()).save(user);
    }

    @Test
    void testGetWorkers() {
        List<User> workers = new ArrayList<>();
        User worker1 = new User();
        worker1.setId(1L);
        worker1.setUsername("worker1");
        worker1.setPassword("password1");
        worker1.setRoles(Collections.singleton(new Role(3L, "ROLE_WORKER")));

        User worker2 = new User();
        worker2.setId(2L);
        worker2.setUsername("worker2");
        worker2.setPassword("password2");
        worker2.setRoles(Collections.singleton(new Role(3L, "ROLE_WORKER")));

        workers.add(worker1);
        workers.add(worker2);

        when(userRepository.findByRolesName("ROLE_WORKER")).thenReturn(workers);

        List<User> result = userService.getWorkers();

        assertEquals(2, result.size());
        assertEquals(workers, result);
        verify(userRepository, times(1)).findByRolesName("ROLE_WORKER");
    }

    @Test
    void testDeleteUser_UserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(1L);

        assertFalse(result);
        verify(userRepository, never()).deleteById(1L);
    }


}
