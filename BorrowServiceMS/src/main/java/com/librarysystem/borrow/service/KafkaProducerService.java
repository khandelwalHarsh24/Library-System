package com.librarysystem.borrow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.librarysystem.common.dto.NotifyRequest;


@Service
public class KafkaProducerService {
	
	@Autowired
    private KafkaTemplate<String, NotifyRequest> kafkaTemplate;

    private static final String TOPIC = "notification-topic";
    public void sendNotification(NotifyRequest event) {
    	try {
            System.out.println("Sending Kafka event to topic: " + TOPIC);
            kafkaTemplate.send(TOPIC, event);
            System.out.println("Kafka event sent successfully");
        } catch (Exception e) {
            System.err.println("Failed to send Kafka message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
