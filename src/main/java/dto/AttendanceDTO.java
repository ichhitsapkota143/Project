package dto;

public class AttendanceDTO {
    private int id;
    private String studentName;
    private String courseCode;
    private String sessionDate;
    private String timestamp;

    public AttendanceDTO(int id, String studentName, String courseCode, String sessionDate, String timestamp) {
        this.id = id;
        this.studentName = studentName;
        this.courseCode = courseCode;
        this.sessionDate = sessionDate;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
