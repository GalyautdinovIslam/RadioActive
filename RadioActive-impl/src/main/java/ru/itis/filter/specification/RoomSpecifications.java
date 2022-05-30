package ru.itis.filter.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.itis.model.RoomEntity;

@Component
public class RoomSpecifications extends Specifications<RoomEntity> {

    public Specification<RoomEntity> hasPassword(Boolean value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null)
                return criteriaBuilder.conjunction();
            if (value)
                return criteriaBuilder.isNotNull(root.get("password"));
            return criteriaBuilder.isNull(root.get("password"));
        };
    }

    public Specification<RoomEntity> isStreaming(Boolean value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null)
                return criteriaBuilder.conjunction();
            if (value)
                return criteriaBuilder.isTrue(root.get("streaming"));
            return criteriaBuilder.isFalse(root.get("streaming"));
        };
    }
}
