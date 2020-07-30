package com.example.demo.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cat {

    @Min(value = 1, message = "Age should be greater than 1")
    @Max(value = 5, message = "Age should not be greater than 5")
    private int age;

    @NotBlank(message = "Name can't be blank")
    private String name;
}
