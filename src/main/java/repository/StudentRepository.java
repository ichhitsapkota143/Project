package repository;

import model.Student; // ✅ Import your entity
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    // You can add custom query methods here if needed
}
