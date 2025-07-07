package com.librarysystem.notify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.librarysystem.common.dto.NotifyRequest;



@Service
public class KafkaConsumerService {

    @Autowired
    private NotifyService notificationService;
    

    @KafkaListener(topics = "notification-topic", groupId = "notification-group",containerFactory = "kafkaListenerContainerFactory")
    public void consume(NotifyRequest event) {
    	notificationService.sendNotification(event);
    }
}
