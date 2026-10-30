package com.inventory.pos.core.repository;

import com.inventory.pos.core.domain.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
