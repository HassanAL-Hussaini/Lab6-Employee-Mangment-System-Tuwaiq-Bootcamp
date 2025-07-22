package com.example.employeemanagementsystem.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @NotEmpty
    @Size(min=3 , message = "id length must be more than 2 characters")
    private String id;

    @NotEmpty
    @Size(min=1 , message = "name length must be more than 1 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Must contain only characters (no numbers)")
    private String name;

    @Email(message = "must be a valid email Format")
    private String email;

    @Size(min = 10 , max = 10)
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and be exactly 10 digits")
    private String phoneNumber;

    @NotNull
    @Positive
    @Min(value = 26 , message = "age must be more than 25")
    private int age;

    @NotNull
    @Pattern(regexp = "^(supervisor|coordinator)$", message = "Position must be either 'supervisor' or 'coordinator'")
    private String position; //true -> supervise , false -> coordinator


    private boolean onLeave = false;


    @NotNull
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime hireDate;

    @NotNull
    @Positive
    private int anuualLeave;


    public Employee(Employee emp) {
        this.id = emp.getId();
        this.name = emp.getName();
        this.email = emp.getEmail();
        this.phoneNumber = emp.getPhoneNumber();
        this.age = emp.getAge();
        this.position = emp.getPosition();
        this.onLeave = emp.isOnLeave();
        this.hireDate = emp.getHireDate();
        this.anuualLeave = emp.getAnuualLeave();
    }
}
