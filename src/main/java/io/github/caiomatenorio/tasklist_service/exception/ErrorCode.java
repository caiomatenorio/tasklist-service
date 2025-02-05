package io.github.caiomatenorio.tasklist_service.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    ERR000("Internal server error"),
    ERR001("Invalid username or password"),
    ERR002("The user is not authorized to access this resource, please log in"),
    ERR003("Username already in use"),
    ERR004("The request method is not supported"),
    ERR005("This resource was not found"),
    ERR006("Invalid password"),;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.name() + ": " + this.message;
    }
}
