package org.unibl.etf.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.validators.SqlValidators;
import org.unibl.etf.models.validators.XSSValidators;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank
    @XSSValidators
    @SqlValidators
    private String username;
    @NotBlank
    @XSSValidators
    @SqlValidators
    private String password;
}
