package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> getAllCoursesByCategoryId(Long id);

    Course getCourseByCategoryIdAndId(Long category_id, Long course_id);

    List<Course> findTop4ByOrderById();
}
