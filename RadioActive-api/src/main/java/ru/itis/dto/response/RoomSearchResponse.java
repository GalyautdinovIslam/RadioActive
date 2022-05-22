package ru.itis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoomSearchResponse {

    private String title;
    private String ownerNickname;
    private Integer subscriberAmount;
    private Boolean streaming;
    private Integer listenerAmount;

}
