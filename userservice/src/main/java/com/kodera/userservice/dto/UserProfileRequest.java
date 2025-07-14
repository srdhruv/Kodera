package com.kodera.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserProfileRequest
{
    @NotBlank
    private String name;

    private String bio;
    private String avatarUrl;
    private String githubLink;
    private String linkedinLink;
}
