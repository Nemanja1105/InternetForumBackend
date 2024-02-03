package org.unibl.etf.models.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.entities.PermissionEntity;
import org.unibl.etf.models.enums.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String role;
    private List<String> permissions;
    private String token;
}
