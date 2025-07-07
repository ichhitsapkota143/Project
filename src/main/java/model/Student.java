package model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int student_id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "nfc_id")
    private String nfcId;

    @Column(name = "face_id")
    private String faceId;

    // ✅ You need constructors, getters & setters for JPA to work properly

    public Student() {
        // No-arg constructor is required by JPA
    }

    public Student(String fullName, String email, String nfcId, String faceId) {
        this.fullName = fullName;
        this.email = email;
        this.nfcId = nfcId;
        this.faceId = faceId;
    }

    // ✅ Getters and Setters

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNfcId() {
        return nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }
}
