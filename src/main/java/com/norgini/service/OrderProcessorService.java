package com.norgini.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.norgini.domain.Order;
import com.norgini.dto.OrderEvent;
import com.norgini.dto.OrderResponse;
import com.norgini.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProcessorService {

	private final OrderRepository repository;

	@Transactional
	public void process(OrderEvent event) {
		if (repository.existsByEventId(event.eventId())) {
			log.warn("Event ID {} has already been processed. Skipping to avoid duplication.", event.eventId());
			return;
		}
		log.info("Processing business rules and persisting order into the Database...");
		Order newOrder = Order.builder().eventId(event.eventId()).clientId(event.clientId()).amount(event.amount())
				.status("PROCESSED").createdAt(LocalDateTime.now()).build();
		repository.save(newOrder);
		log.info("Order successfully processed! Internal Database ID: {}", newOrder.getId());
	}

	public List<OrderResponse> findAll() {
		return repository.findAll().stream().map(order -> new OrderResponse(order.getId(), order.getClientId(),
				order.getAmount(), order.getStatus(), order.getCreatedAt())).toList();
	}

	public Optional<OrderResponse> findById(Long id) {
		return repository.findById(id).map(order -> new OrderResponse(order.getId(), order.getClientId(),
				order.getAmount(), order.getStatus(), order.getCreatedAt()));
	}

}
