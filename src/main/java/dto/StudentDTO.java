package dto;

public class StudentDTO {
    private int student_id;
    private String fullName;
    private String email;

    public StudentDTO(int student_id, String fullName, String email) {
        this.student_id =student_id;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters (and optionally setters if needed)
    public int getId() {
        return student_id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}
