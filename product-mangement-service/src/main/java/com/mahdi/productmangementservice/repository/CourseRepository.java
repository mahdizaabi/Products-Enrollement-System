package com.mahdi.productmangementservice.repository;

import com.mahdi.productmangementservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
