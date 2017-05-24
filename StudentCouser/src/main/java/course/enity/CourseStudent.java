package course.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class CourseStudent {
    @JsonIgnore
    private Integer id;
    @JsonIgnore
    private Date createTime;
    @JsonIgnore
    private Date updateTime;


    private Integer studentId;

    private Integer courserId;
    @JsonIgnore
    private Byte state;

    private Byte grade;

}