package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.ContactDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Audited(withModifiedFlag = true)
@ToString(callSuper = true)
public class Contact extends ComparableBaseEntity<Contact> {

    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    private Boolean primaryEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean compare(Contact other) {
        return StringUtils.equals(this.email, other.getEmail())
                && ObjectUtils.compare(this.primaryEmail, other.getPrimaryEmail()) == 0
                && ObjectUtils.compare(this.user.getId(), other.getUser().getId()) == 0;
    }

    public ContactDto toDto() {
        return ContactDto.builder()
                .id(getId())
                .createdBy(getCreatedBy())
                .createdDate(getCreatedDate())
                .modifiedBy(getModifiedBy())
                .modifiedDate(getModifiedDate())
                .email(this.email)
                .primaryEmail(this.primaryEmail)
                .build();
    }
}
