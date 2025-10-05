package com.movienow.backend.exceptions.user;

import com.movienow.backend.exceptions.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User is under the required age.")
public class UnderAgeUserException extends BaseException {

    public UnderAgeUserException(String message) {
        super(message);
    }
}
