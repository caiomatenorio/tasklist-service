package io.github.caiomatenorio.tasklist_service.dto;

import org.springframework.http.ResponseEntity;

public class ConventionalResponseEntity extends ResponseEntity<ConventionalResponse> {
    public ConventionalResponseEntity(ConventionalResponse body) {
        super(body, body.getStatus());
    }
}
