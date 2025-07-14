package com.kodera.userservice.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfileDto {
    private Long userId;
    private String name;
    private String bio;
    private String avatarUrl;
    private String githubLink;
    private String linkedinLink;
}
