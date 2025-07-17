package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.EmployeeDTO;
import com.example.caliberclothing.dto.RoleDTO;
import com.example.caliberclothing.dto.StatusDTO;
import com.example.caliberclothing.dto.UserDTO;
import com.example.caliberclothing.entity.Status;
import com.example.caliberclothing.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO, UserDTO userDTO, String role, StatusDTO statusDTO,
            @RequestAttribute("currentUserId") int managerId) {

        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO, userDTO, role, statusDTO, managerId);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable int id,
            @Valid @RequestBody EmployeeDTO employeeDTO, String role, StatusDTO statusDTO,
            @RequestAttribute("currentUserId") int managerId) {

        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO, role, statusDTO, managerId);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable int id,
            @RequestAttribute("currentUserId") int managerId) {

        employeeService.deleteEmployee(id, managerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<EmployeeDTO>> getActiveEmployees() {
        List<EmployeeDTO> employees = employeeService.getActiveEmployees();
        return ResponseEntity.ok(employees);
    }

//    @GetMapping("/role/{roleName}")
//    @PreAuthorize("hasRole('MANAGER')")
//    public ResponseEntity<List<EmployeeDTO>> getEmployeesByRole(@PathVariable String roleName) {
//        List<EmployeeDTO> employees = employeeService.getEmployeesByRole(roleName);
//        return ResponseEntity.ok(employees);
//    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(@RequestParam String searchTerm) {
        List<EmployeeDTO> employees = employeeService.searchEmployees(searchTerm);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<String>> getAllRoles() {
        List<String> roles = employeeService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/statuses")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Status>> getAllStatuses() {
        List<Status> statuses = employeeService.getAllStatuses();
        return ResponseEntity.ok(statuses);
    }
}