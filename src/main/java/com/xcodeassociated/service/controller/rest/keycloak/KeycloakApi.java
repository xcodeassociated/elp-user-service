package com.xcodeassociated.service.controller.rest.keycloak;

import com.xcodeassociated.service.controller.rest.keycloak.dto.UserRepresentationDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface KeycloakApi {

    @GET("auth/admin/realms/service/users/{userId}")
    Call<UserRepresentationDto> getUserRepresentation(@Path("userId") String userId);

}
