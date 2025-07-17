package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.EmployeeDTO;
import com.example.caliberclothing.dto.StatusDTO;
import com.example.caliberclothing.dto.UserDTO;
import com.example.caliberclothing.entity.Employee;
import com.example.caliberclothing.entity.Status;
import com.example.caliberclothing.entity.User;
import com.example.caliberclothing.repository.EmployeeRepository;
import com.example.caliberclothing.repository.StatusRepository;
import com.example.caliberclothing.repository.UserRepository;
import com.example.caliberclothing.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private RoleRepository roleRepository;

    @Autowired
    private StatusRepository statusRepository;

//    private final EmployeeRepository employeeRepository;
//    private final RoleRepository roleRepository;
//    private final StatusRepository statusRepository;
//    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO, UserDTO userDTO, String role, StatusDTO statusDTO, int managerId) {
        // Validate unique fields
        if (employeeRepository.findByEmployeeNoAndDeletedTimestampIsNull(employeeDTO.getEmployeeNo()).isPresent()) {
            throw new RuntimeException("Employee number already exists");
        }

        if (employeeRepository.findByNicNoAndDeletedTimestampIsNull(employeeDTO.getNicNo()).isPresent()) {
            throw new RuntimeException("NIC number already exists");
        }

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

//        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
//            throw new RuntimeException("Email already exists");
//        }

        // Get role and status
//        Role role = roleRepository.findById(roleDTO.getId()).orElse(null);
//        if (role == null) {
//            throw new RuntimeException("Role not found");
//        }

        Status status = statusRepository.findById(statusDTO.getId()).orElse(null);
        if (status == null) {
            throw new RuntimeException("Status not found");
        }

        // Create user
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .isActive(true)
                .role(role)
                .build();

        user = userRepository.save(user);

        // Create employee
        Employee employee = Employee.builder()
                .employeeNo(employeeDTO.getEmployeeNo())
                .fullName(employeeDTO.getFullName())
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .dateOfBirth(employeeDTO.getDateOfBirth())
                .sex(employeeDTO.getSex())
                .civilStatus(employeeDTO.getCivilStatus())
                .address(employeeDTO.getAddress())
                .mobileNumber(employeeDTO.getMobileNumber())
                .telephoneNumber(employeeDTO.getTelephoneNumber())
                .nicNo(employeeDTO.getNicNo())
                .isActive(true)
                .createdUser(managerId)
//                .role(role)
                .user(user)
                .status(status)
                .user(user)
                .build();

        employee = employeeRepository.save(employee);
        return convertToDTO(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(int employeeId, EmployeeDTO employeeDTO, String role, StatusDTO statusDTO, int managerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getDeletedTimestamp() != null) {
            throw new RuntimeException("Cannot update deleted employee");
        }

        // Check for unique NIC if changed
        if (!employee.getNicNo().equals(employeeDTO.getNicNo())) {
            if (employeeRepository.findByNicNoAndDeletedTimestampIsNull(employeeDTO.getNicNo()).isPresent()) {
                throw new RuntimeException("NIC number already exists");
            }
        }

        // Get status (Role no longer an entity here, so no need to fetch from repo)
        Status status = statusRepository.findById(statusDTO.getId())
                .orElseThrow(() -> new RuntimeException("Status not found"));

        // Update employee fields
        employee.setFullName(employeeDTO.getFullName());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setSex(employeeDTO.getSex());
        employee.setCivilStatus(employeeDTO.getCivilStatus());
        employee.setAddress(employeeDTO.getAddress());
        employee.setMobileNumber(employeeDTO.getMobileNumber());
        employee.setTelephoneNumber(employeeDTO.getTelephoneNumber());
        employee.setNicNo(employeeDTO.getNicNo());
        employee.setIsActive(employeeDTO.getIsActive());
        employee.setUpdatedUser(managerId);
        employee.setStatus(status);

        // Update role in User entity
        User user = employee.getUser();
        if (user == null) {
            throw new RuntimeException("User not found for employee");
        }
        user.setRole(role);  // role is String now

        // Save user and employee (assuming cascading not enabled on user)
        userRepository.save(user);
        employee = employeeRepository.save(employee);

        return convertToDTO(employee);
    }


    @Override
    public void deleteEmployee(int employeeId, int managerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getDeletedTimestamp() != null) {
            throw new RuntimeException("Employee already deleted");
        }

        employee.setDeletedTimestamp(LocalDateTime.now());
        employee.setDeletedUser(managerId);
        employee.setIsActive(false);

        // Also deactivate user
        User user = employee.getUser();
        if (user != null) {
            user.setIsActive(false);
            userRepository.save(user);
        }

        employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getDeletedTimestamp() != null) {
            throw new RuntimeException("Employee not found");
        }

        return convertToDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findByDeletedTimestampIsNull();
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee employee : employees) {
            employeeDTOs.add(convertToDTO(employee));
        }
        return employeeDTOs;
    }


    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getActiveEmployees() {
        List<Employee> employees = employeeRepository.findActiveEmployees();
        List<EmployeeDTO> employeesList = new ArrayList<>();
        for (Employee employee : employees) {
            employeesList.add(convertToDTO(employee));
        }
        return employeesList;
    }


//    @Override
//    @Transactional(readOnly = true)
//    public List<EmployeeDTO> getEmployeesByRole(String roleName) {
//        List<Employee> employees = userRepository.findByRoleAndNotDeleted(roleName);
//        List<EmployeeDTO> employeesList = new ArrayList<>();
//        for (Employee employee : employees) {
//            employeesList.add(convertToDTO(employee));
//        }
//        return employeesList;
//    }


    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> searchEmployees(String searchTerm) {
        List<Employee> employees = employeeRepository.searchEmployees(searchTerm);
        List<EmployeeDTO> employeesList = new ArrayList<>();
        for (Employee employee : employees) {
            employeesList.add(convertToDTO(employee));
        }
        return employeesList;
    }


    @Override
    @Transactional(readOnly = true)
    public List<String> getAllRoles() {
        return userRepository.findAllDistinctRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isManager(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        User user = employee.getUser();
        if (user == null || user.getRole() == null) {
            return false;  // or throw exception if preferred
        }

        return "MANAGER".equalsIgnoreCase(user.getRole());
    }


    private EmployeeDTO convertToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .employeeNo(employee.getEmployeeNo())
                .fullName(employee.getFullName())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .dateOfBirth(employee.getDateOfBirth())
                .sex(employee.getSex())
                .civilStatus(employee.getCivilStatus())
                .address(employee.getAddress())
                .mobileNumber(employee.getMobileNumber())
                .telephoneNumber(employee.getTelephoneNumber())
                .nicNo(employee.getNicNo())
                .isActive(employee.getIsActive())
                .createdTimestamp(employee.getCreatedTimestamp())
                .updatedTimestamp(employee.getUpdatedTimestamp())
//                .role(employee.getRole().getRoleName())
//                .status(employee.getStatus().getStatusName())
//                .username(employee.getUser().getUsername())
//                .email(employee.getUser().getEmail())
                .build();
    }
}

