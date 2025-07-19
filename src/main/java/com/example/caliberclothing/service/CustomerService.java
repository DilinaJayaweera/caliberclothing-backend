package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.CustomerDTO;
import com.example.caliberclothing.dto.UserDTO;
import com.example.caliberclothing.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO, UserDTO userDTO);
    CustomerDTO updateCustomer(int customerId, CustomerDTO customerDTO);
    void deleteCustomer(int customerId);
    CustomerDTO getCustomerById(int customerId);
    List<CustomerDTO> getAllCustomers();
    List<CustomerDTO> getActiveCustomers();
    List<CustomerDTO> getCustomersByStatus(String statusName);
    List<CustomerDTO> searchCustomers(String searchTerm);
    List<CustomerDTO> getCustomersCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);
//    List<Status> getAllStatuses();
//    List<Province> getAllProvinces();
    long getTotalCustomersCount();
    long getNewCustomersCount(LocalDateTime since);
    boolean isEmailExists(String email);
    boolean isNicExists(String nicNo);
    public CustomerDTO getCustomerByUsername(String username);

//    CustomerResponseDto registerCustomer(CustomerRegistrationDto registrationDto);

    CustomerDTO getCustomerByEmail(String email);

    Page<CustomerDTO> getCustomersWithPagination(Pageable pageable);

//    Integer getCustomerIdByUsername(String username);
    Optional<Customer> findByUserId(Integer userId);


}

