package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.service.implementation.transition.*;

public interface UserEventHandler {
    void handleLogin(Login event);
    void handleLogout(Logout event);
    void handleRegister(Register event);
    void handleUpdate(Update event);
    void handleDelete(Delete event);
}
