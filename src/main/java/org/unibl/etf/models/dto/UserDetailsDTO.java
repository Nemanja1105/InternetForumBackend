package org.unibl.etf.models.dto;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.entities.PermissionEntity;
import org.unibl.etf.models.enums.Role;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String role;
    private List<PermissionEntity> permissions;
    private boolean status;
    private boolean verified;
}
