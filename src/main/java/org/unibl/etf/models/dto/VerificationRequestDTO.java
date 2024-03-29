package org.unibl.etf.models.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationRequestDTO {
    @NotNull
    private Long id;
    @NotBlank
    @Size(max = 4,min=4)
    private String code;
}
