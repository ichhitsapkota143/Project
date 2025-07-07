package repository;

import model.Session;
import model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    List<Session> findByCourse(Course course);
    List<Session> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    long count();
}

