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
public class ChatResponse {
    private UUID ownerId;
    private UUID roomId;
    private Set<UUID> moderators;
    private Set<UUID> mutedUsers;
}
