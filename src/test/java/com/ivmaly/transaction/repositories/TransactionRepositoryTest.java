package com.ivmaly.transaction.repositories;

import com.ivmaly.transaction.models.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testSaveTransaction() {
        // Создание транзакции
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.TEN);

        // Сохранение транзакции в репозитории
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Проверка, что транзакция сохранена успешно и имеет непустой ID
        assertTrue(savedTransaction.getTransactionId() != null && savedTransaction.getTransactionId() > 0);
    }

    @Test
    public void testFindTransactionById() {
        // Создание транзакции
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.TEN);
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Поиск транзакции по ID
        Transaction foundTransaction = transactionRepository.findById(savedTransaction.getTransactionId()).orElse(null);

        // Проверка, что найденная транзакция не равна null
        assertNotNull(foundTransaction);

        // Проверка, что найденная транзакция имеет правильную сумму
        assertEquals(savedTransaction.getAmount(), foundTransaction.getAmount());
    }
}

