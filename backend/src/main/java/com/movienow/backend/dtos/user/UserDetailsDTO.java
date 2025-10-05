package com.movienow.backend.dtos.user;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {

    private Long id;

    private String password;

    private String email;

}
