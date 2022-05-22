package ru.itis.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoomPageSearchResponse {

    private List<RoomSearchResponse> rooms;
    private Integer totalPage;

}
