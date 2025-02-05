package io.github.caiomatenorio.tasklist_service.security.token;

import java.util.UUID;

public record AuthenticationTokenDetails(
		UUID sessionId,
		UUID userId,
		String name) {
}
