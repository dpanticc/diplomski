package com.dusanpan.reservation.dto;

import com.dusanpan.reservation.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordVerificationDTO {
    private String username;
    private String currentPasswordEncoded; // Store encoded password

    public static PasswordVerificationDTO fromEntity(User user) {
        PasswordVerificationDTO dto = new PasswordVerificationDTO();
        dto.setUsername(user.getUsername());
        dto.setCurrentPasswordEncoded(user.getPassword()); // Assuming user.getPassword() returns the encoded password
        return dto;
    }
}
