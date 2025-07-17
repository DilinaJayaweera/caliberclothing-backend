package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.CustomerDTO;
import com.example.caliberclothing.dto.UserDTO;
import com.example.caliberclothing.entity.Customer;
import com.example.caliberclothing.entity.Province;
import com.example.caliberclothing.entity.Status;
import com.example.caliberclothing.entity.User;
import com.example.caliberclothing.exception.ResourceNotFoundException;
import com.example.caliberclothing.repository.CustomerRepository;
import com.example.caliberclothing.repository.ProvinceRepository;
import com.example.caliberclothing.repository.StatusRepository;
import com.example.caliberclothing.repository.UserRepository;
import com.example.caliberclothing.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final StatusRepository statusRepository;
    private final ProvinceRepository provinceRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO, UserDTO userDTO) {
        // Validate unique fields
        if (customerRepository.existsByEmailAndDeletedTimestampIsNull(customerDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (customerRepository.existsByNicNoAndDeletedTimestampIsNull(customerDTO.getNicNo())) {
            throw new RuntimeException("NIC number already exists");
        }

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Get status and province
        Status status = statusRepository.findById(customerDTO.getStatus().getId())
                .orElseThrow(() -> new RuntimeException("Status not found"));
        Province province = provinceRepository.findById(customerDTO.getProvince().getId())
                .orElseThrow(() -> new RuntimeException("Province not found"));

        // Create user
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .isActive(true)
                .role("CUSTOMER")
                .build();

        user = userRepository.save(user);

        // Create customer
        Customer customer = Customer.builder()
                .fullName(customerDTO.getFullName())
                .firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .dateOfBirth(customerDTO.getDateOfBirth())
                .sex(customerDTO.getSex())
                .email(customerDTO.getEmail())
                .address(customerDTO.getAddress())
                .country(customerDTO.getCountry())
                .zipCode(customerDTO.getZipCode())
                .mobileNumber(customerDTO.getMobileNumber())
                .nicNo(customerDTO.getNicNo())
                .status(status)
                .user(user)
                .build();

        customer = customerRepository.save(customer);
        return convertToDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(int customerId, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (customer.getDeletedTimestamp() != null) {
            throw new RuntimeException("Cannot update deleted customer");
        }

        // Check for unique email if changed
        if (!customer.getEmail().equals(customerDTO.getEmail())) {
            if (customerRepository.existsByEmailAndDeletedTimestampIsNull(customerDTO.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
        }

        // Check for unique NIC if changed
        if (!customer.getNicNo().equals(customerDTO.getNicNo())) {
            if (customerRepository.existsByNicNoAndDeletedTimestampIsNull(customerDTO.getNicNo())) {
                throw new RuntimeException("NIC number already exists");
            }
        }

        // Get status and province
        Status status = statusRepository.findById(customerDTO.getStatus().getId())
                .orElseThrow(() -> new RuntimeException("Status not found"));
        Province province = provinceRepository.findById(customerDTO.getProvince().getId())
                .orElseThrow(() -> new RuntimeException("Province not found"));

        // Update customer
        customer.setFullName(customerDTO.getFullName());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setSex(customerDTO.getSex());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(customerDTO.getAddress());
        customer.setCountry(customerDTO.getCountry());
        customer.setZipCode(customerDTO.getZipCode());
        customer.setMobileNumber(customerDTO.getMobileNumber());
        customer.setNicNo(customerDTO.getNicNo());
        customer.setStatus(status);

//        // Update user email if changed
//        if (customer.getUser() != null) {
//            customer.getUser().setEmail(updateCustomerDto.getEmail());
//            userRepository.save(customer.getUser());
//        }

        customer = customerRepository.save(customer);
        return convertToDTO(customer);
    }

    @Override
    public void deleteCustomer(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (customer.getDeletedTimestamp() != null) {
            throw new RuntimeException("Customer already deleted");
        }

        customer.setDeletedTimestamp(LocalDateTime.now());

        // Also deactivate user
        User user = customer.getUser();
        if (user != null) {
            user.setIsActive(false);
            userRepository.save(user);
        }

        customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (customer.getDeletedTimestamp() != null) {
            throw new RuntimeException("Customer not found");
        }

        return convertToDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findByDeletedTimestampIsNull()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getActiveCustomers() {
        return customerRepository.findActiveCustomers()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByStatus(String statusName) {
        return customerRepository.findByStatusAndNotDeleted(statusName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> searchCustomers(String searchTerm) {
        return customerRepository.searchCustomers(searchTerm)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return customerRepository.findCustomersCreatedBetween(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<Status> getAllStatuses() {
//        return List.of();
//    }
//
//    @Override
//    public List<Province> getAllProvinces() {
//        return List.of();
//    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<Status> getAllStatuses() {
//        return statusRepository.findByIsActiveTrue();
//    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<Province> getAllProvinces() {
//        return provinceRepository.findByIsActiveTrue();
//    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalCustomersCount() {
        return customerRepository.countActiveCustomers();
    }

    @Override
    @Transactional(readOnly = true)
    public long getNewCustomersCount(LocalDateTime since) {
        return customerRepository.countNewCustomersSince(since);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return customerRepository.existsByEmailAndDeletedTimestampIsNull(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNicExists(String nicNo) {
        return customerRepository.existsByNicNoAndDeletedTimestampIsNull(nicNo);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
        return convertToDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDTO> getCustomersWithPagination(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(customer.getDateOfBirth())
                .sex(customer.getSex())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .country(customer.getCountry())
                .zipCode(customer.getZipCode())
                .mobileNumber(customer.getMobileNumber())
                .nicNo(customer.getNicNo())
                .createdTimestamp(customer.getCreatedTimestamp())
                .updatedTimestamp(customer.getUpdatedTimestamp())
//                .status(customer.getStatus().getValue())
//                .username(customer.getUser().getUsername())
                .build();
    }
}
