package sit.int371.modride_service.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import sit.int371.modride_service.beans.APIResponseBean;
import sit.int371.modride_service.controllers.BaseController;
import sit.int371.modride_service.models.UploadResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    @ExceptionHandler(MissingServletRequestPartException.class)
    public APIResponseBean handleMissingServletRequestPartException(MissingServletRequestPartException ex,
            HttpServletResponse response) {
        APIResponseBean res = new APIResponseBean();
        response.setStatus(UnprocessableContentStatus);
        res.setResponse_code(UnprocessableContentStatus);
        res.setResponse_desc("ไม่สามารถ Upload ได้ กรุณาระบุ Key และ Value ให้ถูกต้อง");
        return res;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public APIResponseBean handleMaxSizeException(MaxUploadSizeExceededException exc, HttpServletResponse response) {
        APIResponseBean res = new APIResponseBean();
        response.setStatus(UnprocessableContentStatus);
        res.setResponse_code(UnprocessableContentStatus);
        res.setResponse_desc("ขนาดไฟล์ต้องไม่เกิน 10 MB");
        return res;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public APIResponseBean handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request, HttpServletResponse response) {
        APIResponseBean res = new APIResponseBean();
        System.out.println("request.getRequestURI(): " + request.getRequestURI());
        if ("/api_files/v1/files/attachflie".equals(request.getRequestURI())) {
            response.setStatus(UnSupportMediaTypeStatus);
            res.setResponse_code(UnSupportMediaTypeStatus);
            res.setResponse_desc("MediaType ไม่ถูกต้อง หรือไม่สามารถอัปโหลดได้ เนื่องจาก Key และ Value ไม่ถูกต้อง");
        } else {
            // You may choose to handle other scenarios differently or provide a generic
            // response
            response.setStatus(UnSupportMediaTypeStatus);
            res.setResponse_code(UnSupportMediaTypeStatus);
            res.setResponse_desc("MediaType ไม่ถูกต้อง");
        }
        return res;
    }

    
}