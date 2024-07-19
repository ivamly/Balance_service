package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.Reserve;
import com.ivmaly.transaction.models.ReserveStatus;
import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final ReserveService reserveService;

    public TransactionService(TransactionRepository transactionRepository, UserService userService, ReserveService reserveService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.reserveService = reserveService;
    }

    public Transaction createTransactionFromReserve(Long reserveId) {
        Reserve reserve = reserveService.getReserveById(reserveId);
        Transaction transaction = new Transaction(reserve);

        User user = reserve.getUser();
        user.setReservedBalance(user.getReservedBalance().subtract(reserve.getReserveAmount()));

        userService.updateUser(user);
        transactionRepository.save(transaction);

        reserve.setReserveStatus(ReserveStatus.COMPLETED);
        reserveService.updateReserve(reserve);

        return transaction;
    }

    public Transaction createDeposit(Long userId, BigDecimal amount) {
        User user = userService.getUserById(userId);
        Transaction transaction = new Transaction(user, amount);

        user.setAvailableBalance(user.getAvailableBalance().add(amount));

        userService.updateUser(user);
        transactionRepository.save(transaction);

        return transaction;
    }
}
