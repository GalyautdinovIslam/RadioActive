package ru.itis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static ru.itis.constants.RadioActiveConstants.EMAIL_REGEX;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserRequest {
    @NotBlank
    @Email(regexp = EMAIL_REGEX, message = "Incorrect email")
    private String email;
    @NotBlank(message = "Password must be not blank")
    private String password;
}
