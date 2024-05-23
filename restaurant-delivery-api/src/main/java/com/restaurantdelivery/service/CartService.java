package ru.poolnsk.pool.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poolnsk.pool.entity.Cart;
import ru.poolnsk.pool.repository.CartRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }
    public Cart getOrCreateCart(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cart cart = null;

        // Поиск cookie, содержащей идентификатор сеанса пользователя
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION_ID".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    // Поиск корзины, соответствующей идентификатору сеанса
                    cart = cartRepository.findBySessionId(sessionId);
                    break;
                }
            }
        }


        /*if (userService.getCurrentUsername() != null) {
            cart = userService.getUserCart();
        }*/

        // Если корзина не найдена, создайте новый экземпляр
        if (cart == null) {
            cart = new Cart();
            cart.setSessionId(UUID.randomUUID().toString());
            cartRepository.save(cart);
            // Создание cookie с идентификатором сеанса
            Cookie cookie = new Cookie("SESSION_ID", cart.getSessionId());
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 1 day
            response.addCookie(cookie);
        }
        return cart;
    }

    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }
}