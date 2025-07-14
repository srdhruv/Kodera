package com.kodera.userservice.mapper;

import com.kodera.userservice.dto.UpdateUserProfileRequest;
import com.kodera.userservice.dto.UserProfileDto;
import com.kodera.userservice.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDto toDto(UserProfile entity);

    @Mapping(target="userId", ignore = true)
    void updateEntityFromRequest(UpdateUserProfileRequest request, @MappingTarget UserProfile entity);
}
