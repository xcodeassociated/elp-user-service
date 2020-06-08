package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findUserById(Long id);

    @NotNull
    Page<User> findAll(@NotNull Pageable pageable);

}
