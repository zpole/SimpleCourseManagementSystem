package course.util;

import lombok.Data;

/**
 * Created by zpole on 2016/11/20.
 */
@Data
public class ResultObject {

    private String msg;
    private int errorCode;

    private Object value;

    public ResultObject(ErrorCode code, Object value) {
        this.msg = code.getErrorMsg();
        this.errorCode = code.getErrorCode();
        this.value = value;
    }

    public ResultObject(ErrorCode code){
        this.msg = code.getErrorMsg();
        this.errorCode = code.getErrorCode();
    }
}
