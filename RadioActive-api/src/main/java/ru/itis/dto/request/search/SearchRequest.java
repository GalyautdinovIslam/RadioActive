package ru.itis.dto.request.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SearchRequest {
    @NotBlank(message = "Search parameter must be not black")
    private String search;
    @Min(value = 0, message = "Minimum value of page is {value}")
    private Integer page;
    private Integer pageSize;
    private List<SortParameterRequest> sorts;
}
