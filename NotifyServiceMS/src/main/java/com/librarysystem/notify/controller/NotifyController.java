package com.librarysystem.notify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.common.dto.NotifyRequest;
import com.librarysystem.notify.service.NotifyService;

@RestController
@RequestMapping("/notify")
public class NotifyController {
	
	@Autowired
    private NotifyService notifyService;
	
	@PostMapping("/borrow")
    public ResponseEntity<String> notifyBorrow(@RequestBody NotifyRequest request) {
		notifyService.sendNotification(request);
        return ResponseEntity.ok("Borrow notification sent.");
    }
	
	@PostMapping("/return")
    public ResponseEntity<String> notifyReturn(@RequestBody NotifyRequest request) {
		notifyService.sendNotification(request);
        return ResponseEntity.ok("Return notification sent.");
    }
	
	@PostMapping("/overdue")
    public ResponseEntity<String> notifyOverdue(@RequestBody NotifyRequest request) {
		notifyService.sendNotification(request);
        return ResponseEntity.ok("Overdue notification sent.");
    }
	
}
