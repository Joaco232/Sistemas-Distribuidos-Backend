package com.movienow.backend.dtos.user;
// UserProfileDTO.java

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    private Long id;
    private String email;
    private String name;
    private LocalDate birthDate;
    private List<String> platformsSubscribed;
    private List<String> favoriteGenres;

}
