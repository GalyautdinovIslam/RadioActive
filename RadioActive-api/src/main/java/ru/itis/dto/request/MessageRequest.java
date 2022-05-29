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
public class MessageRequest {
    @NotNull(message = "Chat's id is null!")
    private UUID chatId;
    @NotNull(message = "Sender's id is null!")
    private UUID senderId;
    @NotBlank(message = "Message must be not blank")
    private String content;
}
