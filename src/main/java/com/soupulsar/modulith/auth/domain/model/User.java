package com.soupulsar.modulith.auth.domain.model;

import com.soupulsar.modulith.auth.domain.model.enums.UserRole;
import com.soupulsar.modulith.auth.domain.model.enums.UserStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User {

    private final UUID userId;
    private final String name;
    private final String cpf;
    private String telephone;
    private String email;
    private String passwordHash;
    private final UserRole role;
    private  UserStatus status;


    public static User create(String name, String cpf, String telephone, String email, String passwordHash, UserRole role) {
        if (name == null || name.isBlank()) throw  new IllegalArgumentException("Name cannot be null or blank");
        if (cpf == null || cpf.isBlank()) throw  new IllegalArgumentException("CPF cannot be null or blank");
        if (telephone == null || telephone.isBlank()) throw  new IllegalArgumentException("Telephone cannot be null or blank");
        if (email == null || email.isBlank()) throw  new IllegalArgumentException("Email cannot be null or blank");
        if (passwordHash == null || passwordHash.isBlank()) throw  new IllegalArgumentException("Password cannot be null or blank");
        return new User(UUID.randomUUID(), name, cpf, telephone, email, passwordHash, role, UserStatus.ACTIVE);
    }

    public static User restore(UUID userId, String name, String cpf, String telephone, String email, String passwordHash, UserRole role, UserStatus status) {
        return new User(userId, name, cpf, telephone, email, passwordHash, role, status);
    }


    public void activate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new IllegalStateException("User is already active.");
        }
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new IllegalStateException("User is already inactive.");
        }
        this.status = UserStatus.INACTIVE;
    }




}