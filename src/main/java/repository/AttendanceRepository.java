package repository;

import model.Attendance;
import model.Student;
import model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByStudent(Student student);
    List<Attendance> findBySession(Session session);
    List<Attendance> findByPercentageLessThan(int percentage);
    long countByStudent(Student student);
    boolean existsByStudentAndSession(Student student, Session session);
}
