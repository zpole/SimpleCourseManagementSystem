package course.dto;

import course.enity.Student;
import lombok.Data;

/**
 * Created by zpole on 2016/11/27.
 */
@Data
public class StudentDto {
    private int id;
    private String name;
    private int number;
    private String sex;
    private int grade;


    public StudentDto() {

    }

    public StudentDto(Student student, int grade) {
        this.id = student.getId();
        this.name = student.getName();
        this.number = student.getNumber();
        this.grade = grade;
        if (student.getSex() == 1) {
            this.sex = "男";
        } else {
            this.sex = "女";
        }
    }
}
