package com.mahdi.productmangementservice.controller;


import com.mahdi.productmangementservice.intercomm.UserClientProxy;
import com.mahdi.productmangementservice.model.Course;
import com.mahdi.productmangementservice.model.Transaction;
import com.mahdi.productmangementservice.model.Xer;
import com.mahdi.productmangementservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CourseController {
    @Autowired
    private UserClientProxy userClientProxy;

    @Autowired
    private CourseService courseService;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private Environment env;
    @Value("${spring.application.name}")
    private String serviceId;

    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/course-service/user/{userId}")
    public ResponseEntity<?> getTransactionOfUser(@PathVariable Long userId) {
        List<Transaction> transaction = courseService.findTransactionsOfUser(userId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping("/course-service/listInstances")
    public ResponseEntity<?> findAllInstances() {
        return new ResponseEntity<>(discoveryClient.getInstances(serviceId), HttpStatus.OK);
    }

    @GetMapping("/course-service/port")
    public ResponseEntity<?> getServicePort() {
        return new ResponseEntity<>("Course service is running on: " + env.getProperty("local.server.port"), HttpStatus.OK);
    }

    @GetMapping("/course-service/all")
    public ResponseEntity<?> findAllCourses() {
        List<Course> courses = courseService.allCourses();
        if (courses.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/course-service/enroll")
    public ResponseEntity<?> saveTransaction(@RequestBody Transaction transaction) {
        transaction.setDateOfIssue(LocalDateTime.now());
        transaction.setCourse(courseService.findCourseById(transaction.getCourse().getId()));
        Transaction transactionSaved = courseService.saveTransaction(transaction);
        return new ResponseEntity<>(transactionSaved, HttpStatus.CREATED);
    }

    @GetMapping("/course-service/course/{courseId}")
    public ResponseEntity<?> findAllCustomersEnrolledCourse(@PathVariable Long courseId) {
        List<Transaction> courseTransactions = courseService.findTransactionsOfCourse(courseId);
        if (CollectionUtils.isEmpty(courseTransactions)) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        List<Long> userIdList = courseTransactions.stream().map(Transaction::getId).collect(Collectors.toList());
        return new ResponseEntity<>(userIdList, HttpStatus.OK);
    }

    @GetMapping("/course-service/test-user-service")
    public ResponseEntity<?> testServicesInterConnexions() {
        ResponseEntity<String> testResponse =
                new RestTemplate().getForEntity("http://localhost:8765/user-service/test", String.class);
        return new ResponseEntity<>(testResponse.getBody(), HttpStatus.OK);
    }


    @GetMapping("/course-service/getUserServiceActivePort")
    public ResponseEntity<String> getExternalServicePort() {
        String response = restTemplate.getForObject(
                "http://localhost:8765/user-service/port",
                String.class
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/course-service-feign/test-rest")
    public ResponseEntity<Xer> testPostRestTemplateFeign() {
        Xer xer = new Xer("theXerez", 1);
        List<String> l = new ArrayList<>();
        l.add("mahdi");
        l.add("second");
        xer.setMylistx(l);
        Xer resxer = userClientProxy.test(xer).getBody();

        return new ResponseEntity<>(resxer, HttpStatus.OK);
    }

    @GetMapping("/course-service/test-rest")
    public ResponseEntity<Xer> testPostRestTemplate() {
        Xer xer = new Xer("theXerez", 1);
        List<String> l = new ArrayList<>();
        l.add("mahdi");
        l.add("second");
        xer.setMylistx(l);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Xer> response = restTemplate.postForEntity(
                "http://localhost:8765/user-service/test-rest",
                xer,
                Xer.class
        );

        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }
}