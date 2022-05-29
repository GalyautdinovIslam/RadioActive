package ru.itis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoomRequest {
    @NotBlank(message = "Title of room must be not blank")
    private String title;
    private String password;
    @NotNull(message = "Owner's id is null!")
    private UUID ownerId;
}
