package ru.itis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserResponse {

    private String nickname;
    private Set<UUID> uuidsOfRoomsInOwnership;
    private Set<UUID> uuidsOfRoomsInAdministration;
    private Set<UUID> uuidsOfChatsInOwnership;
    private Set<UUID> uuidsOfChatsInModeration;
    private Set<UUID> uuidsOfRoomsInFavorites;
}
