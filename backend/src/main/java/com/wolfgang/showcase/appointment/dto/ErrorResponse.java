package com.wolfgang.showcase.appointment.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class ErrorResponse {

    private final OffsetDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final List<ValidationViolation> violations;

    public ErrorResponse(
            OffsetDateTime timestamp,
            int status,
            String error,
            String message,
            String path,
            List<ValidationViolation> violations
    ) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.violations = violations;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public List<ValidationViolation> getViolations() {
        return violations;
    }
}
