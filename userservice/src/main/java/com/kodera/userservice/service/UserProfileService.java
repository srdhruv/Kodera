package com.kodera.userservice.service;

import com.kodera.userservice.dto.UpdateUserProfileRequest;
import com.kodera.userservice.dto.UserProfileDto;
import com.kodera.userservice.entity.UserProfile;
import com.kodera.userservice.mapper.UserProfileMapper;
import com.kodera.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;
    private final UserProfileMapper mapper;

    public UserProfileDto getUserDetails(Long id)
    {
        UserProfile profile = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("User profile not found"));
        return mapper.toDto(profile);
    }
    public void updateUserProfile(Long id, UpdateUserProfileRequest request)
    {
        UserProfile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
        mapper.updateEntityFromRequest(request, profile);
        repository.save(profile);
    }

}
