package com.mahdi.microserviceusermanagement.controller;

import com.mahdi.microserviceusermanagement.model.Xer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.mahdi.microserviceusermanagement.model.Role;
import com.mahdi.microserviceusermanagement.model.User;
import com.mahdi.microserviceusermanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private Environment env;

    @Value("${spring.application.name}")
    private String serviceId;
    @GetMapping("/user-service/services")
    public ResponseEntity<?> getServices() {
        return new ResponseEntity<>(discoveryClient.getServices(), HttpStatus.OK);
    }


    @GetMapping("/user-service/instances")
    public ResponseEntity<?> getInstances() {
        return new ResponseEntity<>(discoveryClient.getInstances(serviceId), HttpStatus.OK);
    }

    @GetMapping("/user-service/port")
    public ResponseEntity<String> getPort() {
        String response = "port is open on: " + env.getProperty("local.server.port");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PostMapping("/user-service/registration")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User userx = userService.findByUsername(user.getUsername());
        if (userx != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setRole(Role.USER);
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @GetMapping("/user-service/login")
    public ResponseEntity<?> getUser(Principal principal) {
        //Principal principal = request.getUserPrincipal();
        if (principal == null || principal.getName() == null) {
            //This means; logout will be successful. login?logout
            return new ResponseEntity<>(HttpStatus.OK);
        }
        //username = principal.getName()
        return ResponseEntity.ok(userService.findByUsername(principal.getName()));
    }

    /* takes list of user id and return list of users */
    @PostMapping("/user-service/names")
    public ResponseEntity<?> getNamesOfUsers(@RequestBody List<Long> idList){
        return ResponseEntity.ok(userService.findUsers(idList));
    }

    @GetMapping("/user-service/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("its working...");
    }

    @PostMapping("/user-service/test-rest")
    public ResponseEntity<Xer> test(@RequestBody Xer xer) {
        System.out.println(xer.toString());
        xer.setName("changed");
        xer.getMylistx().add("xxxxxxxxxxxxxxxxxxxxxxxx");

        return new ResponseEntity<>(xer, HttpStatus.OK);
    }
}
