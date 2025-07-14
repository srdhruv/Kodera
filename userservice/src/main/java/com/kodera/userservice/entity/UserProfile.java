package com.kodera.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="user_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfile {
    @Id
    private Long userId; // map to auth-service user id

    @Column(nullable = false)
    private String name;

    private String bio;
    private String avatarUrl;
    private String githubLink;
    private String linkedinLink;

}
