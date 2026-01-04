package com.mtogo.restaurant.application.service;

import com.mtogo.restaurant.infrastructure.adapter.out.persistence.MenuJpaEntity;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.MenuJpaRepository;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.RestaurantJpaEntity;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.RestaurantJpaRepository;
import com.mtogo.restaurant.infrastructure.security.PasswordEncoderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantJpaRepository restaurantRepository;

    @Mock
    private MenuJpaRepository menuRepository;

    @Mock
    private PasswordEncoderUtil passwordEncoderUtil;

    private RestaurantService service;

    @BeforeEach
    void setUp() {
        service = new RestaurantService(restaurantRepository, menuRepository, passwordEncoderUtil);
    }

    @Test
    void createRestaurantEncodesPasswordAndSaves() {
        when(passwordEncoderUtil.encode("rawPassword")).thenReturn("encodedPassword");

        RestaurantJpaEntity savedRestaurant = new RestaurantJpaEntity(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "encodedPassword",
                "12345678",
                "123 Main St");
        when(restaurantRepository.save(any(RestaurantJpaEntity.class))).thenReturn(savedRestaurant);

        RestaurantJpaEntity result = service.createRestaurant(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "rawPassword",
                "12345678",
                "123 Main St");

        assertNotNull(result);
        verify(passwordEncoderUtil).encode("rawPassword");
        verify(restaurantRepository).save(any(RestaurantJpaEntity.class));
    }

    @Test
    void findByEmailReturnsRestaurantWhenExists() {
        RestaurantJpaEntity restaurant = new RestaurantJpaEntity(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "password",
                "12345678",
                "123 Main St");
        when(restaurantRepository.findByEmail("test@restaurant.com")).thenReturn(Optional.of(restaurant));

        RestaurantJpaEntity result = service.findByEmail("test@restaurant.com");

        assertEquals(restaurant, result);
    }

    @Test
    void findByEmailThrowsWhenNotFound() {
        when(restaurantRepository.findByEmail("notfound@restaurant.com")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findByEmail("notfound@restaurant.com"));
    }

    @Test
    void findByIdReturnsRestaurantWhenExists() {
        RestaurantJpaEntity restaurant = new RestaurantJpaEntity(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "password",
                "12345678",
                "123 Main St");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        RestaurantJpaEntity result = service.findById(1L);

        assertEquals(restaurant, result);
    }

    @Test
    void findByIdThrowsWhenNotFound() {
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findById(999L));
    }

    @Test
    void findAllReturnsAllRestaurants() {
        List<RestaurantJpaEntity> restaurants = new ArrayList<>();
        restaurants.add(new RestaurantJpaEntity("Restaurant 1", "Owner 1", "email1@test.com", "pw1", "111", "Addr1"));
        restaurants.add(new RestaurantJpaEntity("Restaurant 2", "Owner 2", "email2@test.com", "pw2", "222", "Addr2"));

        when(restaurantRepository.findAll()).thenReturn(restaurants);

        List<RestaurantJpaEntity> result = service.findAll();

        assertEquals(2, result.size());
        verify(restaurantRepository).findAll();
    }

    @Test
    void validatePasswordReturnsTrueWhenMatches() {
        when(passwordEncoderUtil.matches("rawPassword", "encodedPassword")).thenReturn(true);

        boolean result = service.validatePassword("rawPassword", "encodedPassword");

        assertTrue(result);
    }

    @Test
    void validatePasswordReturnsFalseWhenDoesNotMatch() {
        when(passwordEncoderUtil.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        boolean result = service.validatePassword("wrongPassword", "encodedPassword");

        assertFalse(result);
    }

    @Test
    void createMenuCreatesAndSavesMenu() {
        RestaurantJpaEntity restaurant = new RestaurantJpaEntity(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "password",
                "12345678",
                "123 Main St");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        List<RestaurantService.MenuItemRequest> items = List.of(
                new RestaurantService.MenuItemRequest("Pizza", "Delicious pizza", 12.99),
                new RestaurantService.MenuItemRequest("Burger", "Tasty burger", 9.99));

        MenuJpaEntity savedMenu = new MenuJpaEntity(restaurant);
        when(menuRepository.save(any(MenuJpaEntity.class))).thenReturn(savedMenu);

        MenuJpaEntity result = service.createMenu(1L, items);

        assertNotNull(result);
        verify(menuRepository).save(any(MenuJpaEntity.class));
    }

    @Test
    void createMenuThrowsWhenItemsEmpty() {
        assertThrows(ResponseStatusException.class, () -> service.createMenu(1L, new ArrayList<>()));
    }

    @Test
    void createMenuThrowsWhenItemsNull() {
        assertThrows(ResponseStatusException.class, () -> service.createMenu(1L, null));
    }

    @Test
    void createMenuThrowsWhenPriceIsNegative() {
        RestaurantJpaEntity restaurant = new RestaurantJpaEntity(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "password",
                "12345678",
                "123 Main St");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        List<RestaurantService.MenuItemRequest> items = List.of(
                new RestaurantService.MenuItemRequest("Pizza", "Delicious pizza", -5.0));

        assertThrows(ResponseStatusException.class, () -> service.createMenu(1L, items));
    }

    @Test
    void createMenuThrowsWhenPriceIsZero() {
        RestaurantJpaEntity restaurant = new RestaurantJpaEntity(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "password",
                "12345678",
                "123 Main St");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        List<RestaurantService.MenuItemRequest> items = List.of(
                new RestaurantService.MenuItemRequest("Pizza", "Delicious pizza", 0.0));

        assertThrows(ResponseStatusException.class, () -> service.createMenu(1L, items));
    }
}
