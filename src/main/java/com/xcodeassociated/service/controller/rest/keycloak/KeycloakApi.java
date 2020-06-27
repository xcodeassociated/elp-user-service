package com.xcodeassociated.service.controller.rest.keycloak;

import com.xcodeassociated.service.controller.rest.keycloak.dto.UserRepresentationDto;
import com.xcodeassociated.service.controller.rest.keycloak.dto.UserUpdateRepresentationDto;
import retrofit2.Call;
import retrofit2.http.*;


public interface KeycloakApi {

    @GET("auth/admin/realms/service/users/{userId}")
    Call<UserRepresentationDto> getUserRepresentation(@Path("userId") String userId);

    @PUT("auth/admin/realms/service/users/{userId}")
    Call<Void> updateUserRepresentation(@Path("userId") String userId, @Body UserUpdateRepresentationDto dto);

    @DELETE("auth/admin/realms/service/users/{userId}")
    Call<Void> deleteUserRepresentation(@Path("userId") String userId);

}
