package com.inventory.pos.core;

/** A rule was broken in a way the person using the screen can fix. The message is shown to them as written. */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
