package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.service.implementation.transition.Login;
import com.xcodeassociated.service.service.implementation.transition.Logout;
import com.xcodeassociated.service.service.implementation.transition.Register;
import com.xcodeassociated.service.service.implementation.transition.Update;
import com.xcodeassociated.service.service.implementation.transition.Delete;

public interface UserEventHandler {
    void handleLogin(Login event);
    void handleLogout(Logout event);
    void handleRegister(Register event);
    void handleUpdate(Update event);
    void handleDelete(Delete event);
}
