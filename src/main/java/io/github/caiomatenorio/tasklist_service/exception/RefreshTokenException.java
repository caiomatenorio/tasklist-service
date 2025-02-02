package io.github.caiomatenorio.tasklist_service.exception;

public sealed class RefreshTokenException extends RuntimeException
        permits RefreshTokenException.NoRefreshTokenProvidedException,
        RefreshTokenException.InvalidRefreshTokenException {
    protected RefreshTokenException(String message) {
        super(message);
    }

    public static final class NoRefreshTokenProvidedException extends RefreshTokenException {
        public NoRefreshTokenProvidedException() {
            super("No refresh token cookie was provided");
        }
    }

    public static final class InvalidRefreshTokenException extends RefreshTokenException {
        public InvalidRefreshTokenException() {
            super("Invalid refresh token");
        }
    }
}
