package course.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import course.enity.Teacher;
import lombok.Data;

import java.util.List;

/**
 * Created by zpole on 2016/11/27.
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CourseDto {
    private int id;
    private Teacher teacher;
    private List<StudentDto> students;
    private String name;
}
