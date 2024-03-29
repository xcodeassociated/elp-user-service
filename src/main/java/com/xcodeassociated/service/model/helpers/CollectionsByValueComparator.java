package com.xcodeassociated.service.model.helpers;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.ComparableBaseEntity;

import java.util.Collection;
import java.util.stream.Collectors;


public class CollectionsByValueComparator {
    private CollectionsByValueComparator() {
        throw new ServiceException(ErrorCode.S000, "CollectionsByValueComparator should not be instantiated");
    }

    public static <T extends ComparableBaseEntity> boolean areCollectionsSame(Collection<T> left, Collection<T> right) {
        return left.size() == right.size()
                && right.size() == left.stream()
                        .map(e -> CollectionsByValueComparator.isElementInCollection(e, right))
                        .filter(e -> e.equals(true))
                        .collect(Collectors.toList()).size();
    }

    public static <T extends ComparableBaseEntity> boolean isElementInCollection(T element, Collection<T> elements) {
        boolean hasElement = false;
        for (T elem : elements) {
            if (elem.compare(element)) {
                hasElement = true;
            }
        }
        return hasElement;
    }
}
