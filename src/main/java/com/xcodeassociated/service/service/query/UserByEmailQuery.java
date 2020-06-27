package com.xcodeassociated.service.service.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.xcodeassociated.service.model.QUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserByEmailQuery {
    private String emailQuery;

    public Predicate toPredicate() {
        QUser q = QUser.user;
        return new BooleanBuilder()
                .and(q.contacts.any().email.eq(emailQuery))
                .getValue();
    }
}
