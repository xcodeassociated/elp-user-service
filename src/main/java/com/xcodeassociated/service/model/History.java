package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.HistoryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
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
@Table(name = "history",
    indexes = {
        @Index(name = "USER_HISTORY_ID_INDEX", columnList = "user_id"),
        @Index(name = "EVENT_TYPE_INDEX", columnList = "event")
    }
)
public class History extends ComparableBaseEntity<History> {

    @NotNull
    @Column(name = "event", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType event;

    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean compare(History other) {
        return ObjectUtils.compare(this.event, other.getEvent()) == 0
                && ObjectUtils.compare(this.timestamp, other.getTimestamp()) == 0
                && ObjectUtils.compare(this.user.getId(), other.getUser().getId()) == 0;
    }

    public HistoryDto toDto() {
        return HistoryDto.builder()
                .id(getId())
                .createdBy(getCreatedBy())
                .createdDate(getCreatedDate())
                .modifiedBy(getModifiedBy())
                .modifiedDate(getModifiedDate())
                .event(this.event)
                .timestamp(this.timestamp)
                .build();
    }

    public static History create(EventType eventType, Long timestamp, User user) {
        return new History().toBuilder()
                .event(eventType)
                .timestamp(timestamp)
                .user(user)
                .build();
    }
}
