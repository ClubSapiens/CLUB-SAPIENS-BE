// src/main/java/org/example/cowmatchingbe/config/GlobalExceptionHandler.java
package org.example.cowmatchingbe.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 공통 응답 포맷: RFC7807 ProblemDetail 권장
    private ProblemDetail problem(HttpStatus status, String detail, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(status.getReasonPhrase());
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    // 405 Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> methodNotAllowed(HttpRequestMethodNotSupportedException ex,
                                                          HttpServletRequest req) {
        ProblemDetail pd = problem(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), req);
        // 허용 메서드도 같이 알려주자
        if (ex.getSupportedHttpMethods() != null) {
            pd.setProperty("allow", ex.getSupportedHttpMethods());
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(pd);
    }

    // 400 Validation 에러 (DTO에 @Valid 사용 시)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> invalidArgument(MethodArgumentNotValidException ex,
                                                         HttpServletRequest req) {
        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "유효성 검증에 실패했습니다.", req);
        pd.setProperty("errors", ex.getBindingResult().getFieldErrors().stream().map(fe ->
                Map.of("field", fe.getField(), "message", fe.getDefaultMessage())
        ).toList());
        return ResponseEntity.badRequest().body(pd);
    }

    // 400 (서비스에서 던지는 IllegalArgumentException 등)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> badRequest(IllegalArgumentException ex, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(problem(HttpStatus.BAD_REQUEST, ex.getMessage(), req));
    }

    // 400 (파라미터 제약 위반)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> constraint(ConstraintViolationException ex, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(problem(HttpStatus.BAD_REQUEST, ex.getMessage(), req));
    }



    // 명시적으로 상태를 담아 던지는 경우
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> responseStatus(ResponseStatusException ex, HttpServletRequest req) {
        HttpStatusCode code = ex.getStatusCode();
        ProblemDetail pd = problem(HttpStatus.valueOf(code.value()), ex.getReason(), req);
        return ResponseEntity.status(code).body(pd);
    }

    // 500 (그 외)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> unknown(Exception ex, HttpServletRequest req) {
        // 로그는 꼭 남기고
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problem(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", req));
    }
}