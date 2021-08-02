package com.xyy.pojo;


import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Pet {
    private String type;
    private String name;
}
