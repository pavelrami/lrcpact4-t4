package mx.edu.lrcp.act4.exception;

import java.time.Instant;
import java.util.Map;
public record ApiError(Instant timestamp, int status, String error,
                       String message, Map<String, String> fields) {}
