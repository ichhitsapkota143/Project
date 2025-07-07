package dto;

public class SessionDTO {
    private int id;
    private String dateTime;
    private String courseCode;
    private String courseName;

    public SessionDTO(int id, String dateTime, String courseCode, String courseName) {
        this.id = id;
        this.dateTime = dateTime;
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public int getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }
}
