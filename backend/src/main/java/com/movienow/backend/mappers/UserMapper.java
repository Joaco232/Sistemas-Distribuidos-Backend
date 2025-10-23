package com.movienow.backend.mappers;

import com.movienow.backend.dtos.user.AddUserDTO;
import com.movienow.backend.dtos.user.UserDetailsDTO;
import com.movienow.backend.dtos.user.UserProfileDTO;
import com.movienow.backend.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(AddUserDTO userDTO, String encodedPassword) {

        if (userDTO == null) {

            return null;
        }

        return User.builder()
                .name(userDTO.getName())
                .platformsSubscribed(userDTO.getPlatformsSubscribed())
                .favoriteGenres(userDTO.getFavoriteGenres())
                .email(userDTO.getEmail())
                .birthDate(userDTO.getBirthDate())
                .password(encodedPassword)
                .build();
    }


    public UserDetailsDTO toUserDetailsDTO(User user) {

        if (user == null) {

            return null;
        }

        return UserDetailsDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }



    public UserProfileDTO toUserProfileDTO(User user) {

        if (user == null) {
            return null;
        }

        return UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .birthDate(user.getBirthDate())
                .platformsSubscribed(user.getPlatformsSubscribed())
                .favoriteGenres(user.getFavoriteGenres())
                .build();
    }


}
