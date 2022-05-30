package ru.itis.util.search;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.itis.dto.request.search.SortParameterRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class SearchUtil {
    public static PageRequest addSortingToPageRequest(PageRequest pageRequest, List<SortParameterRequest> sorts) {
        if (sorts != null && !sorts.isEmpty())
            pageRequest = pageRequest.withSort(Sort.by(sorts.stream()
                    .map(x -> Objects.isNull(x.getDesc()) || x.getDesc()
                            ? Sort.Order.asc(x.getParameter())
                            : Sort.Order.desc(x.getParameter()))
                    .collect(Collectors.toList())));
        return pageRequest;
    }
}
