package org.unibl.etf.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.unibl.etf.models.enums.Role;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private Long id;

    @Basic
    @Column(name = "username",nullable = false,length=50,unique = true)
    private String username;

    @Basic
    @Column(name = "password",nullable = false,length=512)
    private String password;

    @Basic
    @Column(name="name",nullable = false)
    private String name;

    @Basic
    @Column(name="surname",nullable = false)
    private String surname;

    @Basic
    @Column(name="email",nullable = false,unique = true)
    private String email;

    @Basic
    @Column(name="status",nullable = false)
    private boolean status;

    @Basic
    @Column(name="verified",nullable = false)
    private boolean verified;

    @Basic
    @Column(name="role",nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Basic
    @Column(name="login_code",length = 4)
    private String loginCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<PermissionEntity> permissions;
}
