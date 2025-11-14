package com.movienow.backend.dtos.user;

import com.movienow.backend.models.Provider;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddMyProvidersDTO {

    private List<Long> proversList;

}
