package com.movienow.backend.exceptions.user;

import com.movienow.backend.exceptions.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
