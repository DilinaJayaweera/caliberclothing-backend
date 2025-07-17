package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.EmployeeDTO;
import com.example.caliberclothing.dto.StatusDTO;
import com.example.caliberclothing.dto.UserDTO;
import com.example.caliberclothing.entity.Status;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO, UserDTO userDTO, String role, StatusDTO statusDTO, int managerId);
    EmployeeDTO updateEmployee(int employeeId, EmployeeDTO employeeDTO, String role, StatusDTO statusDTO, int managerId);

    void deleteEmployee(int employeeId, int managerId);
    EmployeeDTO getEmployeeById(int employeeId);
    List<EmployeeDTO> getAllEmployees();
    List<EmployeeDTO> getActiveEmployees();
//    List<EmployeeDTO> getEmployeesByRole(String roleName);
    List<EmployeeDTO> searchEmployees(String searchTerm);
    List<String> getAllRoles();
    List<Status> getAllStatuses();
    boolean isManager(int employeeId);
}
