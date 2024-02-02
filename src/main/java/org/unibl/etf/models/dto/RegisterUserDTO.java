package org.unibl.etf.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    @NotBlank
    @Size(max=50)
    private String username;
    @NotBlank
    @Size(max=50)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}", message="Password must contain at least 8 characters, of which at least one number and one capital letter!")
    private String password;
    @NotBlank
    @Size(max=255)
    private String name;
    @NotBlank
    @Size(max=255)
    private String surname;
    @NotBlank
    @Size(max=255)
    @Email
    private String email;
}
