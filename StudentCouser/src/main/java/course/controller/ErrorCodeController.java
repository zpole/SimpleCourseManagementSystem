package course.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import course.util.ErrorCode;
import course.util.ResultObject;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpole on 2016/11/26.
 */
@Controller
@Api(value = "错误提示信息")
@RequestMapping(value = "/errorCode")
public class ErrorCodeController {
    @Data
    class ErrorResponse {
        private int errorCode;
        private String msg;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    @ApiOperation(value = "错误信息列表")
    public ResultObject list() {
        ErrorCode[] errorCodes = ErrorCode.values();
        List<ErrorResponse> errorResponses = new ArrayList<>();
        for (int index = 0; index < errorCodes.length; index++) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(errorCodes[index].getErrorCode());
            errorResponse.setMsg(errorCodes[index].getErrorMsg());
            errorResponses.add(errorResponse);
        }
        return new ResultObject(ErrorCode.ERROR_OK, errorResponses);
    }
}
