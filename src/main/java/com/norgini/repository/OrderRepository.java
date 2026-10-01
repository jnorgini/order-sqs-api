package com.norgini.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.norgini.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	boolean existsByEventId(String eventId);
}