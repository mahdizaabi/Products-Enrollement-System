package com.mahdi.productmangementservice.controller;


import com.mahdi.productmangementservice.intercomm.UserClient;
import com.mahdi.productmangementservice.model.Course;
import com.mahdi.productmangementservice.model.Transaction;
import com.mahdi.productmangementservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CourseController {
      @Autowired
    private UserClient userClient;
      @Autowired
    private CourseService courseService;
      @Autowired
      private DiscoveryClient discoveryClient;
      @Autowired
      private Environment env;
      @Value("${spring.application.name}")
      private String serviceId;

      @GetMapping("/course-service/user/{userId}")
      public ResponseEntity<?> getTransactionOfUser (@PathVariable Long userId) {
             Course course = courseService.findCourseById(userId);
             if(course == null) {
                 return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
             }
             return new ResponseEntity<>(course, HttpStatus.OK);
      }
      @GetMapping("/course-service/listInstances")
      public ResponseEntity<?> findAllInstances() {
          return new ResponseEntity<>(discoveryClient.getInstances(serviceId), HttpStatus.OK);
      }
    @GetMapping("/course-service/port")
    public ResponseEntity<?> getServicePort() {
        return new ResponseEntity<>("Course service is running on: "+ env.getProperty("local.server.port"), HttpStatus.OK);
    }

    @GetMapping("/course-service/all")
    public ResponseEntity<?> findAllCourses () {
        List<Course> courses = courseService.allCourses();
        if(courses.size() == 0) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/course-service/enroll")
    public ResponseEntity<?> saveTransaction (@RequestBody Transaction transaction) {
          transaction.setDateOfIssue(LocalDateTime.now());
            transaction.setCourse(courseService.findCourseById(transaction.getCourse().getId()));
        Transaction transactionSaved = courseService.saveTransaction(transaction);

        return new ResponseEntity<>(transactionSaved, HttpStatus.CREATED);
    }

    @GetMapping("/course-service/course/{courseId}")
    public ResponseEntity<?> findAllCustomersEnrolledCourse(@PathVariable Long courseId) {
          List<Transaction> courseTransactions = courseService.findTransactionsOfCourse(courseId);
          if(CollectionUtils.isEmpty(courseTransactions)) {
              return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
          }
          List<Long> userIdList = courseTransactions.stream().map(Transaction::getId).collect(Collectors.toList());
          return new ResponseEntity<>(userIdList, HttpStatus.OK);
    }
}
