package com.movienow.backend.dtos.user;
// UserProfileDTO.java

import com.movienow.backend.models.Provider;
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
    private List<Provider> platformsSubscribed;
    private List<String> favoriteGenres;

}
