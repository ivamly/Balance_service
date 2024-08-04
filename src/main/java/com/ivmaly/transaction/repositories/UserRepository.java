package com.ivmaly.transaction.repositories;

import com.ivmaly.transaction.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
