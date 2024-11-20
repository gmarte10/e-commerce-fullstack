package com.giancarlos.repository;

import com.giancarlos.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Order> findByUserIdAndCreatedAtBetween(
            Long userId,
            ZonedDateTime startDate,
            ZonedDateTime endDate
    );

}
