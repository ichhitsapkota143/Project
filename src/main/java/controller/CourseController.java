package controller;

import model.Course;
import repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import dto.CourseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    // ✅ Return DTOs instead of entities
    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(c -> new CourseDTO(c.getCourseId(), c.getCourseCode(), c.getCourseName()))
                .toList();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable int id) {
        return courseRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
        return courseRepository.findById(id).map(course -> {
            course.setCourseCode(updatedCourse.getCourseCode());
            course.setCourseName(updatedCourse.getCourseName());
            return courseRepository.save(course);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        courseRepository.deleteById(id);
    }
}
