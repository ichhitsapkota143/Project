package service;

import model.Course;
import repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(int id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(int id, Course updatedCourse) {
        return courseRepository.findById(id).map(course -> {
            course.setCourseCode(updatedCourse.getCourseCode());
            course.setCourseName(updatedCourse.getCourseName());
            return courseRepository.save(course);
        }).orElse(null);
    }

    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }
}
