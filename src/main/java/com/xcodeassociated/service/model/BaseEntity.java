package com.xcodeassociated.service.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;


@MappedSuperclass
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@SuperBuilder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
@Audited(withModifiedFlag = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @EqualsAndHashCode.Include
    private String uuid = UUID.randomUUID().toString();

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private long createdDate;

    @LastModifiedDate
    private long modifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

}
