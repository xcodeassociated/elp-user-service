package com.xcodeassociated.service.model.helpers;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.ComparableBaseEntity;

import java.util.Objects;

public class ComparableEntityComparator {
    private ComparableEntityComparator() {
        throw new ServiceException(ErrorCode.S000, "ComparableEntityComparator should not be instantiated");
    }

    public static <T extends ComparableBaseEntity<T>> boolean compare(T left, T right) {
        return (Objects.isNull(left) && Objects.isNull(right))
                || (Objects.nonNull(left) && Objects.nonNull(right) && left.compare(right));
    }

}
