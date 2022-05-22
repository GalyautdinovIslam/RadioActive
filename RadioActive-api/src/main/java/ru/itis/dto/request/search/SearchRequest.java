package ru.itis.dto.request.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SearchRequest {
    private String search;
    private Integer page;
    private Integer pageSize;
    private List<SortParameterRequest> sorts;
}
