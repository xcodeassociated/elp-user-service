package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class OauthAuditorService implements OauthAuditorServiceInterface {
    @Override
    public String getUsername() {
        return this.getAccessToken().getPreferredUsername();
    }

    @Override
    public String getUserSub() {
        return this.getAccessToken().getSubject();
    }

    @Override
    public Set<String> getUserRoles() {
        return this.getAccessToken().getRealmAccess().getRoles();
    }

    private AccessToken getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> kp =
                    (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
            return kp.getKeycloakSecurityContext().getToken();
        } else {
            throw new ServiceException(ErrorCode.S000, "Could not get access token");
        }
    }
}
