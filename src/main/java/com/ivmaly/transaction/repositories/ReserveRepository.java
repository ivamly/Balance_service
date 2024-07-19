package com.ivmaly.transaction.repositories;

import com.ivmaly.transaction.models.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
}
