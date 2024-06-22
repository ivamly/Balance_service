package com.ivmaly.transaction.repositories;

import com.ivmaly.transaction.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        // Создание пользователя
        User user = new User();
        user.setBalance(BigDecimal.TEN);

        // Сохранение пользователя в репозитории
        User savedUser = userRepository.save(user);

        // Проверка, что пользователь сохранен успешно и имеет непустой ID
        assertTrue(savedUser.getUserId() != null && savedUser.getUserId() > 0);
    }

    @Test
    public void testFindUserById() {
        // Создание пользователя
        User user = new User();
        user.setBalance(BigDecimal.TEN);
        User savedUser = userRepository.save(user);

        // Поиск пользователя по ID
        User foundUser = userRepository.findById(savedUser.getUserId()).orElse(null);

        // Проверка, что найденный пользователь не равен null
        assertNotNull(foundUser);

        // Проверка, что найденный пользователь имеет правильный баланс
        assertEquals(savedUser.getBalance(), foundUser.getBalance());
    }
}

