package com.mahdi.microserviceusermanagement.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Xer {
    private String name;
    private Integer id;
    private List<String> mylistx;

    public Xer(String theXerez, int i) {
    }
}
