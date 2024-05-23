package com.restaurantdelivery.service;

import com.restaurantdelivery.entity.RefreshToken;
import com.restaurantdelivery.entity.User;
import com.restaurantdelivery.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken getByUser(User user) {
        return refreshTokenRepository.findByUser(user).orElse(null);
    }

    public RefreshToken getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        RefreshToken refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("REFRESH_TOKEN".equals(cookie.getName())) {
                    refreshToken = getByToken(cookie.getValue());
                }
            }
        }

        return refreshToken;
    }

    public RefreshToken setRefreshToken(HttpServletResponse response, RefreshToken refreshToken) {
        Cookie cookie = new Cookie("REFRESH_TOKEN", refreshToken.getToken());
        cookie.setPath("/");
        Long diffTime = refreshToken.getExpirationDate().getTime() - refreshToken.getIssuedDate().getTime();
        cookie.setMaxAge((int) (diffTime / 1000)); // 1 day
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return refreshToken;
    }

    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("No such refresh token exists"));
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
