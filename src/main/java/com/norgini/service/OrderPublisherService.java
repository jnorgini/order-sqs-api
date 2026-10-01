package com.norgini.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.norgini.dto.OrderEvent;
import com.norgini.dto.OrderRequest;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderPublisherService {

	private final SqsTemplate sqsTemplate;

	public void publish(OrderRequest request) {
		OrderEvent event = new OrderEvent(UUID.randomUUID().toString(), request.clientId(), request.amount());
		log.info("Publishing order to SQS queue. Generated Event ID: {}", event.eventId());
		sqsTemplate.send("order-queue", event);
	}
	
}
