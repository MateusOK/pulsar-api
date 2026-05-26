package com.soupulsar.application.interfaces;

import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.domain.model.user.User;

public interface CustomerGateway {
    String ensureCustomerExists(User user, ClientProfile client);
}