package com.kodera.userservice.controller;

import com.kodera.userservice.dto.UpdateUserProfileRequest;
import com.kodera.userservice.dto.UserProfileDto;
import com.kodera.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUser(@PathVariable Long id)
    {
        return ResponseEntity.ok(userProfileService.getUserDetails(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @RequestBody @Valid UpdateUserProfileRequest request,
                                             Principal principal)
    {
        Long requesterId = Long.parseLong(principal.getName());
        if(!requesterId.equals(id))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own profile.");
        }
        userProfileService.updateUserProfile(id, request);
        return  ResponseEntity.ok("Profile Updated Successfully");
    }
}
