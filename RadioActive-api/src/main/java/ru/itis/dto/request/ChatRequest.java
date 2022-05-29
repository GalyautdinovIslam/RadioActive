package ru.itis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ChatRequest {
    @NotNull(message = "Owner's id is null!")
    private UUID ownerId;
    @NotNull(message = "Room's id is null!")
    private UUID roomId;
}
