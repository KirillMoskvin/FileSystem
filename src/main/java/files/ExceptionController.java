package files;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.HashMap;

@ControllerAdvice
@RestController
public class ExceptionController {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handle(HttpServletRequest request) {
        return new ResponseEntity<>("no such file", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException (HttpServletRequest request) {
        return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
    }
}
