package ru.itis.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserSearchResponse {

    private String nickname;
    private Integer subscribersOfRoomsInOwnershipAmount;
}
