package com.xcodeassociated.service.service.implementation;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.xcodeassociated.service.controller.rest.keycloak.dto.UserRepresentationDto;
import com.xcodeassociated.service.exception.ObjectNotFoundException;
import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.Contact;
import com.xcodeassociated.service.model.EventType;
import com.xcodeassociated.service.model.History;
import com.xcodeassociated.service.model.User;
import com.xcodeassociated.service.model.dto.ContactDto;
import com.xcodeassociated.service.model.dto.UserDto;
import com.xcodeassociated.service.model.dto.UserFullDto;
import com.xcodeassociated.service.repository.UserRepository;
import com.xcodeassociated.service.service.UserServiceCommand;
import com.xcodeassociated.service.service.UserServiceQuery;
import com.xcodeassociated.service.service.implementation.transition.Delete;
import com.xcodeassociated.service.service.implementation.transition.Login;
import com.xcodeassociated.service.service.implementation.transition.Logout;
import com.xcodeassociated.service.service.query.UserByEmailQuery;
import com.xcodeassociated.service.service.query.UserByNullablePropertiesQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserService implements UserServiceQuery, UserServiceCommand {
    private final UserRepository userRepository;

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        log.info("Getting all users pageable: {}", pageable);

        return this.userRepository.findAll(pageable)
                .map(User::toDto);
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Getting user by id: {}", id);

        return this.userRepository.findUserById(id)
                .map(User::toDto)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorCode.E001, id));
    }

    @Override
    public UserFullDto getFullUserById(Long id) {
        log.info("Getting full user by id: {}", id);

        return this.userRepository.findUserById(id)
                .map(User::toFullDto)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorCode.E001, id));
    }

    @Override
    public UserDto getUserByAuthId(String id) {
        log.info("Getting user by auth id: {}", id);

        return this.userRepository.findUserByAuthId(id)
                .map(User::toDto)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorCode.E004, id));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Getting User by email: {}", email);

        UserByEmailQuery queryObject = new UserByEmailQuery()
                .toBuilder()
                .emailQuery(email)
                .build();

        Predicate predicate = queryObject.toPredicate();
        log.debug("Using query predicate: {}", predicate);

        return this.userRepository.findOne(predicate)
                .orElseThrow(() -> {
                    log.warn("User not found by email: {}", email);
                    throw new ObjectNotFoundException(ErrorCode.E003, email);
                })
                .toDto();
    }

    @Override
    public UserDto getUserByFirstNameAndLastNameAndNullableEmail(String firstName, String lastName, String email) {
        log.info("Getting User by firstName: {} || lastName: {} || email: {}", firstName, lastName, email);

        UserByNullablePropertiesQuery queryObject = new UserByNullablePropertiesQuery()
                .toBuilder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();

        Optional<BooleanExpression> predicate = queryObject.toPredicate();
        if (predicate.isPresent()) {
            log.debug("Using query predicate: {}", predicate.get());

            return this.userRepository.findOne(predicate.get())
                    .orElseThrow(() -> {
                        log.warn("User by firstName: {} || lastName: {} || email: {} not found", firstName, lastName, email);
                        throw new ObjectNotFoundException(ErrorCode.E003, "User with email", email, " not found!");
                    })
                    .toDto();
        } else {
            throw new ServiceException(ErrorCode.S000, "Empty query predicate");
        }
    }

    @Override
    public Optional<User> createUser(UserRepresentationDto userRepresentationDto) {
        log.info("Creating user based on representation");

        User user = User.fromUserRepresentation(userRepresentationDto);
        this.addUserHistoryEvent(user, History.create(EventType.REGISTER, userRepresentationDto.getCreatedTimestamp(), user));

        log.info("Created User: {}", user);
        return Optional.of(this.userRepository.save(user));
    }

    @Override
    public Optional<User> updateUser(UserRepresentationDto userRepresentationDto) {
        log.info("Updating user based on representation");

        Optional<User> user = this.userRepository.findUserByAuthId(userRepresentationDto.getId());
        user.ifPresentOrElse(e -> {
            log.info("Running update for user authId: {}", e.getAuthId());
            e.update(userRepresentationDto);
            this.addUserHistoryEvent(e, History.create(EventType.UPDATE, Instant.now().toEpochMilli(), e));
        }, () -> {
            throw new ServiceException(ErrorCode.E004, "Could not find user by auth id: {} for update", userRepresentationDto.getId());
        });

        return user;
    }

    @Override
    public Optional<User> registerUserLogin(Login event) {
        log.info("Registering user login for id: {}", event.getUserId());

        Optional<User> user = this.userRepository.findUserByAuthId(event.getUserId());
        user.ifPresentOrElse(e -> {
            log.info("Registering login for user authId: {}", e.getAuthId());
            this.addUserHistoryEvent(e, History.create(EventType.LOGIN, event.getTime(), e));
        }, () -> {
            throw new ServiceException(ErrorCode.E004, "Could not find user by auth id: {} for event register", event.getUserId());
        });

        return user;
    }

    @Override
    public Optional<User> registerUserLogout(Logout event) {
        log.info("Registering user logout for id: {}", event.getUserId());

        Optional<User> user = this.userRepository.findUserByAuthId(event.getUserId());
        user.ifPresentOrElse(e -> {
            log.info("Registering logout for user authId: {}", e.getAuthId());
            this.addUserHistoryEvent(e, History.create(EventType.LOGOUT, event.getTime(), e));
        }, () -> {
            throw new ServiceException(ErrorCode.E004, "Could not find user by auth id: {} for event register", event.getUserId());
        });

        return user;
    }


    @Override
    public void deleteUserById(Long id) {
        log.info("Deleting user by id: {}", id);

        if (this.userRepository.existsById(id)) {
            this.userRepository.deleteById(id);
        } else {
            log.error("User with id: {} does not exist", id);
            throw new ServiceException(ErrorCode.E001, "Could not delete user");
        }
    }

    @Override
    public void deleteUser(Delete event) {
        String authID = event.getUserId();
        log.info("Deleting user by auth id: {}", authID);

        if (this.userRepository.existsByAuthId(authID)) {
            this.userRepository.deleteUserByAuthId(authID);
        } else {
            log.error("User with auth id: {} does not exist", authID);
            throw new ServiceException(ErrorCode.E004, "Could not delete user");
        }
    }

    private void addUserHistoryEvent(User user, History event) {
        if (user.addHistoryEvent(event)) {
            log.info("History event added");
        } else {
            log.warn("History event was not added, event already present");
        }
    }

    @NotNull
    private Set<Contact> userEmailsDtoToEmails(@NotNull UserDto userDto) {
        return userDto.getContacts()
                .stream()
                .map(this::emailFromDto)
                .collect(Collectors.toSet());
    }

    private Contact emailFromDto(@NotNull ContactDto contactDto) {
        return new Contact()
                .toBuilder()
                .email(contactDto.getEmail())
                .verified(contactDto.getVerified())
                .build();
    }
}
