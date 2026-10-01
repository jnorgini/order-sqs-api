package com.norgini.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.norgini.dto.OrderRequest;
import com.norgini.dto.OrderResponse;
import com.norgini.service.OrderProcessorService;
import com.norgini.service.OrderPublisherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderPublisherService publisherService;
	private final OrderProcessorService processorService;

	@PostMapping
	public ResponseEntity<Void> createOrder(@RequestBody @Valid OrderRequest request) {
		publisherService.publish(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> getAllOrders() {
		List<OrderResponse> orders = processorService.findAll();
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
		return processorService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

}
