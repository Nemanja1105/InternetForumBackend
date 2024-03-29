package org.unibl.etf.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.validators.SqlValidators;
import org.unibl.etf.models.validators.XSSValidators;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserRoleDTO {
    @NotBlank
    @Pattern(regexp = "^(Client|Moderator|Admin)$")
    @XSSValidators
    @SqlValidators
    private String role;
}
