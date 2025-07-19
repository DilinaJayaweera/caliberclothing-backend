package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.EmployeeDTO;
import com.example.caliberclothing.dto.EmployeeUpdateRequest;
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
        try {
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

            // Get status
            Status status = statusRepository.findById(statusDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Status not found"));

            // Create user first
            User user = User.builder()
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .isActive(true)
                    .role(role) // Use the role string directly
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
                    .createdTimestamp(LocalDateTime.now()) // Add this
                    .updatedTimestamp(LocalDateTime.now()) // Add this
                    .createdUser(managerId)
                    .user(user) // Only once!
                    .status(status)
                    .build();

            employee = employeeRepository.save(employee);
            return convertToDTO(employee);

        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error creating employee: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create employee: " + e.getMessage());
        }
    }

    @Override
    public EmployeeDTO updateEmployee(int employeeId, EmployeeUpdateRequest employeeData, int managerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getDeletedTimestamp() != null) {
            throw new RuntimeException("Cannot update deleted employee");
        }

        // Check for unique NIC if changed
        if (!employee.getNicNo().equals(employeeData.getNicNo())) {
            if (employeeRepository.findByNicNoAndDeletedTimestampIsNull(employeeData.getNicNo()).isPresent()) {
                throw new RuntimeException("NIC number already exists");
            }
        }

        // Check for unique username if changed
        if (employeeData.getUsername() != null && !employeeData.getUsername().trim().isEmpty()) {
            User existingUser = employee.getUser();
            if (existingUser != null && !existingUser.getUsername().equals(employeeData.getUsername())) {
                if (userRepository.findByUsername(employeeData.getUsername()).isPresent()) {
                    throw new RuntimeException("Username already exists");
                }
            }
        }

        // Get status
        Status status = statusRepository.findById(employeeData.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status not found"));

        // Update employee fields
        employee.setEmployeeNo(employeeData.getEmployeeNo());
        employee.setFullName(employeeData.getFullName());
        employee.setFirstName(employeeData.getFirstName());
        employee.setLastName(employeeData.getLastName());
        employee.setDateOfBirth(employeeData.getDateOfBirth());
        employee.setSex(employeeData.getSex());
        employee.setCivilStatus(employeeData.getCivilStatus());
        employee.setAddress(employeeData.getAddress());
        employee.setMobileNumber(employeeData.getMobileNumber());
        employee.setTelephoneNumber(employeeData.getTelephoneNumber());
        employee.setNicNo(employeeData.getNicNo());
        employee.setUpdatedUser(managerId);
        employee.setUpdatedTimestamp(LocalDateTime.now());
        employee.setStatus(status);

        // Set isActive based on status - this is the key fix!
        boolean isActive = determineIsActiveFromStatus(status);
        employee.setIsActive(isActive);

        // Update user fields if provided
        User user = employee.getUser();
        if (user == null) {
            throw new RuntimeException("User not found for employee");
        }

        user.setRole(employeeData.getRole());

        // Update username if provided
        if (employeeData.getUsername() != null && !employeeData.getUsername().trim().isEmpty()) {
            user.setUsername(employeeData.getUsername());
        }

        // Update password if provided
        if (employeeData.getPassword() != null && !employeeData.getPassword().trim().isEmpty()) {
            // You might want to encode the password here if using password encoding
            // user.setPassword(passwordEncoder.encode(employeeData.getPassword()));
            user.setPassword(employeeData.getPassword());
        }

        // Save user and employee
        userRepository.save(user);
        employee = employeeRepository.save(employee);

        return convertToDTO(employee);
    }

    // Helper method to determine isActive based on status
    private boolean determineIsActiveFromStatus(Status status) {
        return status.getId() == 1;
    }

    @Override
    public boolean isEmployeeActive(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employee.getIsActive() && employee.getDeletedTimestamp() == null;
    }

//    @Override
//    public List<EmployeeDTO> getActiveEmployees() {
//        List<Employee> employees = employeeRepository.findByIsActiveTrueAndDeletedTimestampIsNull();
//        return employees.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }



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
                // ADD THESE LINES:
                .user(employee.getUser() != null ? UserDTO.builder()
                        .id(employee.getUser().getId())
                        .username(employee.getUser().getUsername())
                        .role(employee.getUser().getRole())
                        .isActive(employee.getUser().getIsActive())
                        .build() : null)
                .status(employee.getStatus() != null ? StatusDTO.builder()
                        .id(employee.getStatus().getId())
                        .value(employee.getStatus().getValue())
                        .build() : null)
                .build();
    }
}

