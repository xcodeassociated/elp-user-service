package com.xcodeassociated.service.service.implementation;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.xcodeassociated.service.exception.ObjectNotFoundException;
import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.Contact;
import com.xcodeassociated.service.model.User;
import com.xcodeassociated.service.model.dto.ContactDto;
import com.xcodeassociated.service.model.dto.UserDto;
import com.xcodeassociated.service.model.dto.simple.UserSimpleEmailsDto;
import com.xcodeassociated.service.repository.UserRepository;
import com.xcodeassociated.service.service.UserServiceCommand;
import com.xcodeassociated.service.service.UserServiceQuery;
import com.xcodeassociated.service.service.query.UserByEmailQuery;
import com.xcodeassociated.service.service.query.UserByNullablePropertiesQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
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

        return this.userRepository
                .findAll(pageable)
                .map(User::toDto);
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> user = this.userRepository.findUserById(id);
        if (user.isPresent()) {
            log.info("Found user: {}", user.get());
            return user.get().toDto();
        } else {
            log.warn("User not found, id: {}", id);
            throw new ObjectNotFoundException(ErrorCode.E001, id);
        }
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
    public UserDto createUser(UserDto userDto) {
        log.info("Creating user with dto: {}", userDto);

        final User user = new User()
                .toBuilder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .contacts(this.userEmailsDtoToEmails(userDto))
                .build();

        user.getContacts()
                .forEach(e -> e.setUser(user));

        return this.userRepository
                .save(user)
                .toDto();
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        log.info("Updating user with dto: {}", userDto);

        Optional<User> user = this.userRepository.findUserById(userDto.getId());
        if (user.isPresent()) {
            log.info("User found: {}", user.get());

            // user data:
            User foundUser = user.get();
            foundUser.setFirstName(userDto.getFirstName());
            foundUser.setLastName(userDto.getLastName());
            // user -> contacts
            Set<Contact> contacts = foundUser.getContacts();
            contacts.addAll(this.userEmailsDtoToEmails(userDto));
            foundUser.setContacts(contacts);
            // each contact -> user
            foundUser.getContacts()
                    .forEach(e -> e.setUser(foundUser));

            return this.userRepository
                    .save(foundUser)
                    .toDto();
        } else {
            log.warn("User by dto: {} not found", userDto);
            throw new ObjectNotFoundException(ErrorCode.E001, userDto.getId());
        }
    }

    @Override
    public Page<UserSimpleEmailsDto> getAllUsersEmailsById(Set<Long> ids, Pageable pageable) {
        return this.userRepository
                .getAllClientsEmailsPaged(ids, pageable)
                .map(e -> new UserSimpleEmailsDto()
                        .toBuilder()
                        .firstName(e.getFirstName())
                        .lastName(e.getLastName())
                        .emails(new HashSet<>(Arrays.asList(e.getEmails().split("\\s*,\\s*"))))
                        .build()
                );
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user by id: {}", id);

        this.userRepository.deleteById(id);
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
