package org.unibl.etf.models.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.validators.SqlValidators;
import org.unibl.etf.models.validators.XSSValidators;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {
    @NotBlank
    @Size(max=1000)
    @XSSValidators
    @SqlValidators
    private String comment;
    @NotNull
    private Long senderId;
}
