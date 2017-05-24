package course.controller;


import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import course.util.ConstantValue;
import course.util.ErrorCode;
import course.util.MD5Util;
import course.util.ResultObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by yy156 on 2016/8/18.
 */
@Controller
@Slf4j
@Api(value = "file-api", description = "图片上传和下载", position = 5)
public class FileController {

    private static ThreadLocalRandom random = ThreadLocalRandom.current();

    /**
     * 通过文件属性来判断文件是否是图片
     *
     * @param filepath
     * @return
     */
    private boolean isPic(String filepath) {
        int index = filepath.indexOf(".");
        if (index == -1 || index == 0 || index == filepath.length() - 1) {
            return false;
        }
        String lastname = filepath.substring(index + 1, filepath.length()).toLowerCase();
        if (lastname.equals("gif") || lastname.equals("bmp") || lastname.equals("jpng") ||
                lastname.equals("jpg") || lastname.equals("jpeg") || lastname.equals("png")) {
            return true;
        }
        return false;
    }

    @Data
    static class FileDto {
        private String file_name;
    }

    /**
     * 上传图片
     *
     * @param files
     * @return
     */
    @RequestMapping(value = "/img/upload", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "图片上传", response =
            ResultObject.class, notes = "图片上传",
            httpMethod = "POST", produces = "application/json")
    public ResultObject uploadMutilPic(@RequestParam(value = "files") MultipartFile files) {
        ResultObject resultObject;
        if (isPic(files.getOriginalFilename())) {
            String filename = saveFile(files);
            //不符合图片格式
            if (filename == null) {
                resultObject = new ResultObject(ErrorCode.ERROR_OK);
            } else {
                FileDto fileDto = new FileDto();
                fileDto.setFile_name(filename);
                resultObject = new ResultObject(ErrorCode.ERROR_OK, fileDto);
            }
        } else {
            resultObject = new ResultObject(ErrorCode.ERROR_OK);
        }
        return resultObject;
    }

    @RequestMapping(value = "/img/download/{filename:.+}")
    @ApiOperation(value = "图片下载", response =
            ResultObject.class, notes = "图片下载",
            httpMethod = "GET")
    public ResponseEntity<byte[]> downloadPic(@PathVariable String filename) {
        if (filename == null || filename.trim().length() == 0) {
            return null;
        }
        if (!isPic(filename)) {
            return null;
        }
        String contettype = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(new File(filename));
        if (StringUtils.isEmpty(contettype)) {
            return null;
        }
        return getFileByte(filename, contettype);
    }


    /***
     * 保存文件
     *
     * @param file
     * @return
     */

    private String saveFile(MultipartFile file) {
        //判断操作系统
        String os = System.getProperty("os.name");
        String filePath = null;
        if (os.toLowerCase().startsWith("win")) {
            filePath = ConstantValue.WINDOW_IMAGE_BASEPATH;
        } else {
            filePath = ConstantValue.LINUX_IMAGE_BASEPATH;
        }
        File root = new File(filePath);
        if (!root.exists()) {
            root.mkdirs();
        }
        String filename = file.getOriginalFilename();
        int index = filename.lastIndexOf(".");
        String lastName;
        if (index == -1) {
            return null;
        }

        lastName = filename.substring(index, filename.length()).toLowerCase();
        if (lastName.trim().equals(".js") || lastName.trim().equals(".exe") || lastName.trim().equals(".sh")) {
            return null;
        }
        String date = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
        date += random.nextLong(10000);
        String name = MD5Util.MD5(date);
        filename = name + lastName;
        filePath = filePath + File.separator + filename;
        try {
            file.transferTo(new File(filePath));
            //fix the
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private ResponseEntity<byte[]> getFileByte(String filename, String contettype) {
        HttpHeaders headers = new HttpHeaders();
        byte[] body = null;
        String path = null;
        HttpStatus httpState = HttpStatus.NOT_FOUND;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            path = ConstantValue.WINDOW_IMAGE_BASEPATH + File.separator + filename;
        } else {
            path = ConstantValue.LINUX_IMAGE_BASEPATH + File.separator + filename;
        }
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                body = new byte[is.available()];
                is.read(body);
                is.close();
                headers.add("Content-Type", contettype);
                headers.add("Content-Length", "" + body.length);
                headers.add("Content-Disposition", "attachment;filename="
                        + filename);
                httpState = HttpStatus.OK;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers,
                httpState);
        return entity;
    }

}
