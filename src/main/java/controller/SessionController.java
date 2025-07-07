package controller;

import model.Session;
import model.Course;
import dto.SessionDTO;
import repository.SessionRepository;
import repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public List<SessionDTO> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(session -> new SessionDTO(
                        session.getSessionId(),
                        session.getDateTime().toString(),
                        session.getCourse().getCourseCode(),
                        session.getCourse().getCourseName()))
                .toList();
    }

    @GetMapping("/{id}")
    public Session getSessionById(@PathVariable int id) {
        return sessionRepository.findById(id).orElse(null);
    }

    @GetMapping("/course/{courseId}")
    public List<SessionDTO> getSessionsByCourse(@PathVariable int courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) return List.of();

        return sessionRepository.findByCourse(course).stream()
                .map(session -> new SessionDTO(
                        session.getSessionId(),
                        session.getDateTime().toString(),
                        course.getCourseCode(),
                        course.getCourseName()))
                .toList();
    }

    @PostMapping
    public Session createSession(@RequestBody Session session) {
        return sessionRepository.save(session);
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable int id) {
        sessionRepository.deleteById(id);
    }
}
