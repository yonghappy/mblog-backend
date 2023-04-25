package st.coo.memo.common;

import cn.dev33.satoken.exception.NotLoginException;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseDTO<Void> bizException(BizException bizException) {
        log.info("BizException : {} => {}", bizException.getCode(), bizException.getMsg());
        return ResponseDTO.fail(bizException);
    }
    @ExceptionHandler(NotLoginException.class)
    public ResponseDTO<Void> bizException(NotLoginException bizException) {
        return ResponseDTO.fail(ResponseCode.need_login);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO<Void> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        List<String> allErrors = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
        String msg = Joiner.on(",").join(allErrors);
        return ResponseDTO.fail(ResponseCode.param_error.getCode(), msg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseDTO<Void> methodArgumentNotValidExceptionHandler(HttpMessageNotReadableException ex) {
        return ResponseDTO.fail(ResponseCode.param_error.getCode(), "请求参数不合法");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseDTO<Void> methodArgumentNotValidExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        return ResponseDTO.fail(ResponseCode.param_error.getCode(), "请求方法" + ex.getMethod() + "不支持");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseDTO<Void> methodArgumentNotValidExceptionHandler(IllegalArgumentException ex) {
        return ResponseDTO.fail(ResponseCode.param_error.getCode(), ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseDTO<Void> defaultExceptionHandler(DuplicateKeyException ex) {
        return ResponseDTO.fail(ResponseCode.param_error.getCode(), "数据已存在");
    }
}
