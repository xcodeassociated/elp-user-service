package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.UserDto;
import com.xcodeassociated.service.model.helpers.CollectionsByValueComparator;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Audited(withModifiedFlag = true)
@ToString(callSuper = true)
@Table(name = "user_table",
    indexes = {
        @Index(name = "FIRST_NAME_INDEX", columnList = "first_name"),
        @Index(name = "LAST_NAME_INDEX", columnList = "last_name")
    }
)
public class User extends ComparableBaseEntity<User> {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @BatchSize(size = 10)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Set<Contact> contacts = new HashSet<>();

    @Override
    public boolean compare(User other) {
        return StringUtils.equals(this.firstName, other.getFirstName())
                && StringUtils.equals(this.lastName, other.getLastName())
                && CollectionsByValueComparator.areCollectionsSame(this.contacts, other.getContacts());
    }

    public UserDto toDto() {
        return UserDto.builder()
                .id(getId())
                .createdBy(getCreatedBy())
                .createdDate(getCreatedDate())
                .modifiedBy(getModifiedBy())
                .modifiedDate(getModifiedDate())
                .firstName(this.firstName)
                .lastName(this.lastName)
                .contacts(this.contacts.stream()
                        .map(Contact::toDto)
                        .collect(Collectors.toSet()))
                .build();
    }
}
