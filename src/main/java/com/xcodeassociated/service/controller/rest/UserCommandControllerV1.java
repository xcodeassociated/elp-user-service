package com.xcodeassociated.service.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcodeassociated.service.controller.rest.keycloak.KeycloakApi;
import com.xcodeassociated.service.controller.rest.keycloak.dto.UserUpdateRepresentationDto;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;

import java.io.IOException;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user/api/v1/users")
public class UserCommandControllerV1 {

    private final OauthAuditorServiceInterface oauthAuditorService;
    private final KeycloakApi keycloakApi;

    @PutMapping("/update")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> updateUser(@RequestBody UserUpdateRepresentationDto userDto) throws IOException {
        log.info("Processing `updateUser` request in UserControllerV1, userDto: {}", userDto);
        String authId = this.oauthAuditorService.getUserSub();

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(userDto);
        log.info("Updating user: {} and serialized dto: {}", authId, payload);

        Response<Void> response = this.keycloakApi.updateUserRepresentation(authId, userDto).execute();
        return new ResponseEntity<>(response.body(), HttpStatus.valueOf(response.code()));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> deleteUser() throws IOException {
        log.info("Processing `deleteUser` request in UserControllerV1");
        String authId = this.oauthAuditorService.getUserSub();
        log.info("Deleting user: {}", authId);

        Response<Void> response = this.keycloakApi.deleteUserRepresentation(authId).execute();
        return new ResponseEntity<>(response.body(), HttpStatus.valueOf(response.code()));
    }
}
