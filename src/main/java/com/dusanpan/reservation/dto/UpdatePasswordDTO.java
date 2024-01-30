package com.dusanpan.reservation.dto;

import com.dusanpan.reservation.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {

    private String username;
    private String newPassword;

    public UpdatePasswordDTO(String username, String newPassword) {
        this.username = username;
        this.newPassword = newPassword;
    }

    // Static method to create DTO from User entity
    public static UpdatePasswordDTO fromEntity(User user, String newPassword) {
        return new UpdatePasswordDTO(user.getUsername(), newPassword);
    }
}
