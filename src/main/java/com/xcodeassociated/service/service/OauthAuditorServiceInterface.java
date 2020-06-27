package com.xcodeassociated.service.service;

import java.util.Set;

public interface OauthAuditorServiceInterface {
    String getUsername();
    String getUserSub();
    Set<String> getUserRoles();
}
