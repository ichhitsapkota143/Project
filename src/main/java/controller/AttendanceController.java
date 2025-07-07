package controller;


import model.Attendance;
import model.Student;
import model.Session;
import dto.AttendanceDTO;
import repository.AttendanceRepository;
import repository.StudentRepository;
import repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.AttendanceService;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public List<AttendanceDTO> getAllAttendance() {
        return attendanceRepository.findAll().stream()
                .map(a -> new AttendanceDTO(
                        a.getAttendanceId(),
                        a.getStudent().getFullName(),
                        a.getSession().getCourse().getCourseCode(),
                        a.getSession().getDateTime().toString(),
                        a.getTimestamp().toString()))
                .toList();
    }

    @GetMapping("/student/{studentId}")
    public List<AttendanceDTO> getByStudent(@PathVariable int studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) return List.of();

        return attendanceRepository.findByStudent(student).stream()
                .map(a -> new AttendanceDTO(
                        a.getAttendanceId(),
                        a.getStudent().getFullName(),
                        a.getSession().getCourse().getCourseCode(),
                        a.getSession().getDateTime().toString(),
                        a.getTimestamp().toString()))
                .toList();
    }

    @PostMapping
    public String markAttendance(@RequestParam int studentId, @RequestParam int sessionId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Session session = sessionRepository.findById(sessionId).orElse(null);

        if (student == null || session == null) return "Invalid student or session ID";

        boolean exists = attendanceRepository.existsByStudentAndSession(student, session);
        if (exists) return "Attendance already recorded.";

        Attendance attendance = new Attendance(student, session, LocalDateTime.now());
        attendanceRepository.save(attendance);
        return "Attendance recorded successfully.";
    }
}
