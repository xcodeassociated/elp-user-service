package com.xcodeassociated.service.service.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.xcodeassociated.service.model.QUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserByNullablePropertiesQuery {
    private String firstName;
    private String lastName;
    private String email;

    public Optional<BooleanExpression> toPredicate() {
        if (StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(lastName) || StringUtils.isNotBlank(email)) {
            QUser q = QUser.user;
            return Stream.of(
                    Optional.ofNullable(firstName)
                            .filter(StringUtils::isNotBlank)
                            .map(q.firstName::eq)
                            .orElse(Expressions.asBoolean(true).isTrue()),
                    Optional.ofNullable(lastName)
                            .filter(StringUtils::isNotBlank)
                            .map(q.lastName::eq)
                            .orElse(Expressions.asBoolean(true).isTrue()),
                    Stream.of(
                            Optional.ofNullable(email)
                                    .filter(StringUtils::isNotBlank)
                                    .map(p -> q.contacts.any().email.equalsIgnoreCase(p))
                                    .orElse(Expressions.asBoolean(true).isTrue())

                    ).reduce(BooleanExpression::or).get()
            ).reduce(BooleanExpression::and);
        } else {
            return Optional.empty();
        }
    }
}
