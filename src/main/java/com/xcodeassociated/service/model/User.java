package com.xcodeassociated.service.model;

import com.xcodeassociated.service.controller.rest.keycloak.dto.UserRepresentationDto;
import com.xcodeassociated.service.model.dto.UserDto;
import com.xcodeassociated.service.model.dto.UserFullDto;
import com.xcodeassociated.service.model.helpers.CollectionsByValueComparator;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Audited(withModifiedFlag = true)
@ToString(callSuper = true)
@Table(name = "users",
    indexes = {
        @Index(name = "AUTH_ID_INDEX", columnList = "auth_id"),
        @Index(name = "USERNAME_INDEX", columnList = "username")
    }
)
public class User extends ComparableBaseEntity<User> {

    @Column(name = "auth_id", nullable = false, unique = true)
    private String authId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "created_timestamp")
    private Long createdTimestamp;

    @Column(name = "enabled")
    private Boolean enabled;

    @BatchSize(size = 10)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Set<Contact> contacts = new HashSet<>();

    @BatchSize(size = 10)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Set<History> history = new HashSet<>();

    @Override
    public boolean compare(User other) {
        return StringUtils.equals(this.firstName, other.getFirstName())
                && StringUtils.equals(this.authId, other.getAuthId())
                && StringUtils.equals(this.username, other.getUsername())
                && StringUtils.equals(this.lastName, other.getLastName())
                && ObjectUtils.compare(this.createdTimestamp, other.getCreatedTimestamp()) == 0
                && ObjectUtils.compare(this.enabled, other.getEnabled()) == 0
                && CollectionsByValueComparator.areCollectionsSame(this.contacts, other.getContacts())
                && CollectionsByValueComparator.areCollectionsSame(this.history, other.getHistory());
    }

    public UserDto toDto() {
        return UserDto.builder()
                .id(getId())
                .createdBy(getCreatedBy())
                .createdDate(getCreatedDate())
                .modifiedBy(getModifiedBy())
                .modifiedDate(getModifiedDate())
                .username(this.username)
                .authId(this.authId)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .createdTimestamp(this.createdTimestamp)
                .enabled(this.enabled)
                .contacts(this.contacts.stream()
                        .map(Contact::toDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public UserFullDto toFullDto() {
        return UserFullDto.builder()
                .id(getId())
                .createdBy(getCreatedBy())
                .createdDate(getCreatedDate())
                .modifiedBy(getModifiedBy())
                .modifiedDate(getModifiedDate())
                .username(this.username)
                .authId(this.authId)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .createdTimestamp(this.createdTimestamp)
                .enabled(this.enabled)
                .contacts(this.contacts.stream()
                        .map(Contact::toDto)
                        .collect(Collectors.toSet()))
                .history(this.history.stream()
                        .map(History::toDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public boolean addHistoryEvent(History event) {
        if (!CollectionsByValueComparator.isElementInCollection(event, this.history)) {
            this.history.add(event);
            return true;
        }
        return false;
    }

    public User update(UserRepresentationDto dto) {
        this.username = dto.getUsername();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.enabled = dto.getEnabled();

        Optional<Contact> contact = Optional.ofNullable(dto.getEmail())
                .map(e -> new Contact().toBuilder()
                        .email(e)
                        .verified(dto.getEmailVerified())
                        .user(this)
                        .build());

        contact.ifPresent(e -> {
            if (!CollectionsByValueComparator.isElementInCollection(e, this.contacts)) {
                this.contacts.add(e);
            }
        });

        return this;
    }

    public static User fromUserRepresentation(UserRepresentationDto dto) {
        User user = new User().toBuilder()
                .authId(dto.getId())
                .createdTimestamp(dto.getCreatedTimestamp())
                .username(dto.getUsername())
                .enabled(dto.getEnabled())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .contacts(new HashSet<>())
                .history(new HashSet<>())
                .build();

        Optional<Contact> contact = Optional.ofNullable(dto.getEmail())
                .map(e -> new Contact().toBuilder()
                        .email(e)
                        .verified(dto.getEmailVerified())
                        .user(user)
                        .build());

        contact.ifPresent(e -> user.getContacts().add(e));
        return user;
    }

}
