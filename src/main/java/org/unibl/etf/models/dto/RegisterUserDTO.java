package org.unibl.etf.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.validators.SqlValidators;
import org.unibl.etf.models.validators.XSSValidators;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    @NotBlank
    @Size(max=50)
    @XSSValidators
    @SqlValidators
    private String username;
    @NotBlank
    @Size(max=50)
    @XSSValidators
    @SqlValidators
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}", message="Password must contain at least 8 characters, of which at least one number and one capital letter!")
    private String password;
    @NotBlank
    @XSSValidators
    @SqlValidators
    @Size(max=255)
    private String name;
    @NotBlank
    @XSSValidators
    @SqlValidators
    @Size(max=255)
    private String surname;
    @NotBlank
    @XSSValidators
    @SqlValidators
    @Size(max=255)
    @Email
    private String email;
}
