package course.util;

import lombok.Data;

/**
 * Created by zpole on 2016/11/22.
 */
@Data
public class CheckedException extends RuntimeException {

    private ErrorCode errorCode;

    public CheckedException(ErrorCode errorCode) {
        super(errorCode.getErrorMsg());
        this.errorCode = errorCode;
    }
}
