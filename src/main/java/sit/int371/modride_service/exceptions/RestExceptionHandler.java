package sit.int371.modride_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sit.int371.modride_service.models.UploadResponseMessage;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    
}
