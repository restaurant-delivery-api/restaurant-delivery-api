package ru.poolnsk.pool.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.poolnsk.pool.dto.JwtRequest;
import ru.poolnsk.pool.dto.RefreshTokenDto;
import ru.poolnsk.pool.dto.UserCreateDto;
import ru.poolnsk.pool.dto.WorkerDto;
import ru.poolnsk.pool.entity.RefreshToken;
import ru.poolnsk.pool.entity.User;
import ru.poolnsk.pool.response.AppResponse;
import ru.poolnsk.pool.service.RefreshTokenService;
import ru.poolnsk.pool.service.UserService;
import ru.poolnsk.pool.utils.JwtTokenUtils;

import java.time.Duration;
import java.util.*;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Value("${refresh.lifetime}")
    private Duration refreshLifetime;

    @PostMapping("auth")
    public ResponseEntity<AppResponse> createAuthToken(@RequestBody JwtRequest authRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new AppResponse("Неверный логин или пароль"));
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        return ResponseEntity.ok(new AppResponse("Access and refresh tokens successfully generated", generatePairOfTokens(userDetails, response)));
    }

    @PostMapping("refresh-auth-token")
    public ResponseEntity<AppResponse> refreshAuthToken(HttpServletRequest request, HttpServletResponse response) {
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(request);
        UserDetails userDetails = refreshToken.getUser();
        if (refreshToken.getExpirationDate().before(new Date())) {
            return ResponseEntity.badRequest().body(new AppResponse("Refresh token has expired"));
        }
        refreshTokenService.delete(refreshToken);
        return ResponseEntity.ok(new AppResponse("Generated new pair of access and refresh tokens", generatePairOfTokens(userDetails, response)));
    }

    @PostMapping("save-user")
    public ResponseEntity<AppResponse> addUser(@RequestBody @Valid UserCreateDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setUsername(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        if (!userService.saveUser(user)) {
            return ResponseEntity.badRequest().body(new AppResponse("Email already in use"));
        }

        return ResponseEntity.ok(new AppResponse("User successfully registered"));
    }

    @PostMapping("save-worker")
    public ResponseEntity<AppResponse> addWorker(@RequestBody @Valid UserCreateDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setUsername(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        if (!userService.saveWorker(user)) {
            return ResponseEntity.badRequest().body(new AppResponse("Email already in use"));
        }

        return ResponseEntity.ok(new AppResponse("Worker successfully registered"));
    }

    @GetMapping("get-workers")
    public ResponseEntity<AppResponse> getWorkers() {
        List<WorkerDto> workerDtoList = new ArrayList<>();
        for (User user : userService.getWorkers()) {
            WorkerDto workerDto = new WorkerDto();
            workerDto.setName(user.getName());
            workerDto.setSurname(user.getSurname());
            workerDto.setEmail(user.getUsername());
            workerDtoList.add(workerDto);
        }
        return ResponseEntity.ok(new AppResponse("Worker list", workerDtoList));
    }

    @PostMapping("logout-user")
    public ResponseEntity<AppResponse> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(request);
        refreshTokenService.delete(refreshToken);
        refreshTokenService.clearCookie(response);
        return ResponseEntity.ok(new AppResponse("User refresh token deleted"));
    }

    @PostMapping("create-admin")     // remove when not necessary
    public ResponseEntity<AppResponse> createAdmin() {
        userService.createAdmin();
        return ResponseEntity.ok(new AppResponse("Admin created"));
    }

    private Map<String, String> generatePairOfTokens(UserDetails userDetails, HttpServletResponse response) {
        String token = jwtTokenUtils.generateToken(userDetails);
        RefreshToken refreshToken = new RefreshToken((User) userDetails, refreshLifetime);
        refreshToken = refreshTokenService.save(refreshToken);
        refreshTokenService.setRefreshToken(response, refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", token);
        return tokens;
    }
}
