package service;

import model.Session;
import model.Course;
import repository.SessionRepository;
import repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(int id) {
        return sessionRepository.findById(id).orElse(null);
    }

    public Session createSession(LocalDateTime dateTime, int courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) return null;

        Session session = new Session(dateTime, course);
        return sessionRepository.save(session);
    }

    public void deleteSession(int id) {
        sessionRepository.deleteById(id);
    }
}
