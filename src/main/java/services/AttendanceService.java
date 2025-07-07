package service;

import model.Attendance;
import model.Student;
import model.Session;
import repository.AttendanceRepository;
import repository.StudentRepository;
import repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public String markAttendance(int studentId, int sessionId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Session session = sessionRepository.findById(sessionId).orElse(null);

        if (student == null || session == null) {
            return "Invalid student or session ID";
        }

        boolean exists = attendanceRepository.existsByStudentAndSession(student, session);
        if (exists) {
            return "Attendance already recorded.";
        }

        Attendance attendance = new Attendance(student, session, LocalDateTime.now());
        attendanceRepository.save(attendance);
        return "Attendance recorded successfully.";
    }
}
