package ru.itis.filters.specifications;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;

public class Specifications<T> {
    public Specification<T> like(String field, String value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(getPath(root, field)), "%" + value.toLowerCase() + "%");
    }

    private <X, R> Path<X> getPath(Root<R> root, String field) {
        List<String> fields = Arrays.asList(field.split("\\."));
        if (fields.size() == 1) return root.get(field);
        Path<X> path = root.get(fields.get(0));
        fields = fields.subList(1, fields.size());
        for (String f : fields) {
            path = path.get(f);
        }
        return path;
    }
}
