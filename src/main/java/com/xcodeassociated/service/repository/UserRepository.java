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

    // note: postgres only native sql query paged
    @Query(nativeQuery = true,
            value = "select distinct u.id, u.first_name as firstName, u.last_name as lastName, " +
                    "array_to_string(array_agg(distinct co.email), ',') as emails " +
                    "from user_table as u " +
                        "join contact as co on co.user_id = u.id " +
                    "where u.id IN :ids group by u.id, u.first_name, u.last_name order by u.id DESC --#pageable",
            countQuery = "select count(*) from user_table as u where u.id in :ids"
    )
    Page<UserSimpleEmails> getAllClientsEmailsPaged(@NotNull Set<Long> ids, @NotNull Pageable pageable);
}
