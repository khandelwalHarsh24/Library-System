package com.librarysystem.notify.service;

import com.librarysystem.common.dto.NotifyRequest;

public interface NotifyService {
	public void sendNotification(NotifyRequest request);
}
