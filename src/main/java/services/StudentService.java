package service;

import model.Student;
import repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(int id, Student updatedStudent) {
        return studentRepository.findById(id).map(student -> {
            student.setFullName(updatedStudent.getFullName());
            student.setEmail(updatedStudent.getEmail());
            student.setNfcId(updatedStudent.getNfcId());
            student.setFaceId(updatedStudent.getFaceId());
            return studentRepository.save(student);
        }).orElse(null);
    }

    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }
}
