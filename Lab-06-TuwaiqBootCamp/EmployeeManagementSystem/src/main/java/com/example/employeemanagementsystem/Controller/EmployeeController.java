package com.example.employeemanagementsystem.Controller;

import com.example.employeemanagementsystem.API.ApiResponse;
import com.example.employeemanagementsystem.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
        return ResponseEntity.status(200).body(employees);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody@Valid Employee employee, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }
        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("Added successfully"));
    }
    @PutMapping("/update/{index}")
    //TODO make update be with id not index
    public ResponseEntity<?> updateEmpolyee(@PathVariable int index ,@RequestBody@Valid Employee employee , Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }
        employees.set(index , employee);
        return ResponseEntity.status(200).body(new ApiResponse("updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmpolyee(@PathVariable String id){
        boolean isExist = false;
        for (Employee employee : employees){
            if(employee.getId().equals(id)){
                employees.remove(employee);
                isExist = true;
                break;
            }
        }
        if(isExist){
            return ResponseEntity.status(200).body(new ApiResponse("deleted successfully"));
        }else{
            return ResponseEntity.status(400).body(new ApiResponse("the Id is Not Exist"));
        }
    }
    @GetMapping("get-by-position/{position}")
    public ResponseEntity<?> getEmployeesByPosition(@PathVariable String position){
        ArrayList<Employee> searchedPosition = new ArrayList<>();
        if (position.equals("supervisor")||position.equals("coordinator")) {
                for (Employee employee : employees){
                    if(employee.getPosition().equals(position)){
                        searchedPosition.add(employee);
                    }
                }
                return ResponseEntity.status(200).body(searchedPosition);
        }else{
            return ResponseEntity.status(400).body(new ApiResponse("the spelling of position should be Either {supervisor} or {coordinator}"));
        }

    }
    @GetMapping("/get-employee-by-age-range/{min}/{max}")
    public ResponseEntity<?> getEmployeeByAgeRange(@PathVariable int min ,@PathVariable int max ) {
        if(min > 25){
            ArrayList<Employee> employeesInRange = new ArrayList<>();
            for (Employee employee : employees) {
                if (employee.getAge() > min && employee.getAge() < max) {
                    employeesInRange.add(employee);
                }
            }
            return ResponseEntity.status(200).body(employeesInRange);
        }
        return ResponseEntity.status(400).body(new ApiResponse("the age must be more than 25"));
    }
    @PutMapping("/annule-leave/{id}")
    public ResponseEntity<?> EmployeeAnnuleLeave(@PathVariable String id ){
        int index = 0 ;//بعدين باخذ الاندكس وبعدل على الاراي الاساسيه من خلاله
        Employee emp = null;
        for (Employee employee : employees){
            if(employee.getId().equals(id)){
                emp = new Employee(employee);
                break;
            }
            index++;
        }
        if(emp != null){
            //the employee found
            if(emp.isOnLeave()){
                return ResponseEntity.status(400).body(new ApiResponse("you already on leave After comeback you can Extends your vacation if you still have remaining days"));
            }else if(emp.getAnuualLeave() <= 0){
                return ResponseEntity.status(400).body(new ApiResponse("you don't have remaining days"));
            }else{
                //200 response statement
                emp.setOnLeave(true);
                emp.setAnuualLeave(emp.getAnuualLeave() - 1);
                employees.set(index,emp);
                return ResponseEntity.status(200).body(new ApiResponse("annual Leave is Activated"));
            }
        }else{
            //the employee is not found
            return ResponseEntity.status(400).body(new ApiResponse("the Employee with this id is Not Exist"));
        }
    }
    @GetMapping("/no-annual-leave")
    public ResponseEntity<?> getEmployeeswithNoAnnualLeave(){
        ArrayList<Employee> employeesWithNoAnnualLeave = new ArrayList<>();
        if (!employees.isEmpty()) {
            for (Employee employee : employees){
                if(employee.getAnuualLeave() <= 0){
                    employeesWithNoAnnualLeave.add(employee);
                }
            }
            return ResponseEntity.status(200).body(employeesWithNoAnnualLeave);
        }else{
            return ResponseEntity.status(400).body(new ApiResponse("There is no employee right now add employees"));
        }
    }
    @PutMapping("/promoted-employee/{supervisorId}/{employeePromoteId}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String supervisorId , @PathVariable String employeePromoteId){
        if (!employees.isEmpty()) {
            for (Employee supervisorEmployee : employees) {
                if (supervisorEmployee.getId().equals(supervisorId)) {
                    if (supervisorEmployee.getPosition().equals("supervisor")) {
                        for (Employee employee : employees) {
                            if (employee.getId().equals(employeePromoteId)) {
                                if (employee.getPosition().equals("coordinator")) {
                                    if(employee.getAge() >= 30){
                                        if(!employee.isOnLeave()){
                                            employee.setPosition("supervisor");
                                            return ResponseEntity.status(200).body(new ApiResponse("Employee is Promoted to supervisor"));
                                        }else{
                                            return ResponseEntity.status(400).body(new ApiResponse("Employee is Currently on leave  "));
                                        }
                                    }else{
                                        return ResponseEntity.status(400).body(new ApiResponse("this employee is under the age (30)"));
                                    }
                                } else {
                                    return ResponseEntity.status(400).body(new ApiResponse("this Employee is Already supervisor (or) The id is not exist"));
                                }
                            }
                        }
                    }else{
                        return ResponseEntity.status(400).body(new ApiResponse("you Dont have the Permeation to Promote Employee (you are not supervisor)"));
                    }
                }
            }
        }else{
            return ResponseEntity.status(400).body(new ApiResponse("There is no employee right now add employees"));
        }
        return null;//this will never read .
    }


}
