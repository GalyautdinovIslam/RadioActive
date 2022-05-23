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
public class RoomResponse {
    private String title;
    private UUID ownerId;
    private UUID chatId;
    private boolean streaming;
    private Set<UUID> admins;
    private Set<UUID> listeners;
    private Set<UUID> bannedUsers;
}
