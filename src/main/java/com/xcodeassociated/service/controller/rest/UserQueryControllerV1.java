package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.commons.paging.CustomPageRequest;
import com.xcodeassociated.commons.paging.JsonViewAwarePage;
import com.xcodeassociated.commons.paging.SortDirection;
import com.xcodeassociated.service.controller.rest.dto.UserSearchDto;
import com.xcodeassociated.service.model.dto.UserDto;
import com.xcodeassociated.service.model.dto.UserFullDto;
import com.xcodeassociated.service.service.UserServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user/api/v1/users")
public class UserQueryControllerV1 {

    private final UserServiceQuery userServiceQuery;

    @GetMapping("/paged")
    @PreAuthorize("hasRole('backend_service')")
    public JsonViewAwarePage<UserDto> getAllUsersPaged(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                         @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getAllUsersPaged` request in UserControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new JsonViewAwarePage<>(userServiceQuery.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        log.info("Processing `getUser` request in UserControllerV1, id: {}", id);

        return new ResponseEntity<>(this.userServiceQuery.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/full")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserFullDto> getFullUser(@PathVariable Long id) {
        log.info("Processing `getFullUser` request in UserControllerV1, id: {}", id);

        return new ResponseEntity<>(this.userServiceQuery.getFullUserById(id), HttpStatus.OK);
    }

    @GetMapping("/by/authId/{authId}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserDto> getUserByAuthId(@PathVariable String authId) {
        log.info("Processing `getUserByAuthId` request in UserControllerV1, authId: {}", authId);

        return new ResponseEntity<>(this.userServiceQuery.getUserByAuthId(authId), HttpStatus.OK);
    }

    @GetMapping("/by/email/{value}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String value) {
        log.info("Processing `getUserByEmail` request in UserControllerV1, value: {}", value);

        return new ResponseEntity<>(this.userServiceQuery.getUserByEmail(value), HttpStatus.OK);
    }

    @PostMapping("/by/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserDto> getUserByData(@RequestBody UserSearchDto dto) {
        log.info("Processing `getUserByData` request in UserControllerV1, dto: {}", dto);

        return new ResponseEntity<>(this.userServiceQuery.getUserByFirstNameAndLastNameAndNullableEmail
                (dto.getFirstName(), dto.getLastName(), dto.getEmail()), HttpStatus.OK);
    }

}
