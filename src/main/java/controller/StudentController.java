package controller;

import model.Student;
import dto.StudentDTO;
import repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {


    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(s -> new StudentDTO(s.getStudent_id(), s.getFullName(), s.getEmail()))
                .toList();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable int id, @RequestBody Student updatedStudent) {
        return studentRepository.findById(id).map(student -> {
            student.setFullName(updatedStudent.getFullName());
            student.setEmail(updatedStudent.getEmail());
            student.setNfcId(updatedStudent.getNfcId());
            student.setFaceId(updatedStudent.getFaceId());
            return studentRepository.save(student);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable int id) {
        studentRepository.deleteById(id);
    }
}

