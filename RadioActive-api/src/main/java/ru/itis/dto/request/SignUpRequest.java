package ru.itis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.itis.annotation.FieldsMatch;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldsMatch(fields = {"password", "repeatPassword"})
public class SignUpRequest extends UserExtendedRequest {
    private String repeatPassword;
}
