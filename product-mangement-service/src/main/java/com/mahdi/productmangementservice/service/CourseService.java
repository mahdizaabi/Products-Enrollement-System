package com.mahdi.productmangementservice.service;

import com.mahdi.productmangementservice.model.Course;
import com.mahdi.productmangementservice.model.Transaction;

import java.util.List;

public interface CourseService {
    List<Course> allCourses();

    Course findCourseById(Long courseId);

    List<Transaction> findTransactionsOfUser(Long userId);

    List<Transaction> findTransactionsOfCourse(Long courseId);

    Transaction saveTransaction(Transaction transaction);
}
