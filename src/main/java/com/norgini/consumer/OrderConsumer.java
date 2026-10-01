package com.norgini.consumer;

import org.springframework.stereotype.Component;

import com.norgini.dto.OrderEvent;
import com.norgini.service.OrderProcessorService;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

	private final OrderProcessorService processorService;

	@SqsListener(value = "order-queue")
	public void listen(OrderEvent event) {
		log.info("Message consumed from SQS. Event ID: {}", event.eventId());
		try {
			processorService.process(event);
		} catch (Exception e) {
			log.error("Fatal error processing event {}. Message will be retried.", event.eventId(), e);
			throw e;
		}
	}
	
}
