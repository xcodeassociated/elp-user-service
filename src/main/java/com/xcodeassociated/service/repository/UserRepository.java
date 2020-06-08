package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.User;
import com.xcodeassociated.service.model.dto.simple.UserSimpleEmails;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findUserById(Long id);

    @NotNull
    Page<User> findAll(@NotNull Pageable pageable);

}
