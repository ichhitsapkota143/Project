package repository;

import model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByExamDateBetween(LocalDate from, LocalDate to);
}
