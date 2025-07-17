package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.ApiResponse;
import com.example.caliberclothing.dto.CustomerDTO;
import com.example.caliberclothing.dto.UserDTO;
import com.example.caliberclothing.entity.Province;
import com.example.caliberclothing.entity.Status;
import com.example.caliberclothing.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

//    @PostMapping
//    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
//    public ResponseEntity<CustomerDTO> createCustomer(
//            @Valid @RequestBody CustomerDTO customerDTO, UserDTO userDTO,
//            @RequestAttribute("currentUserId") int createdBy) {
//
//        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO, userDTO);
//        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
//    }

    @PostMapping("/register")
    public ResponseEntity<CustomerDTO> registerCustomer(
            @Valid @RequestBody CustomerDTO customerDTO,
            @RequestBody UserDTO userDTO) {

        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO, userDTO); // or -1 if unauthenticated
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

//    @PostMapping("/register")
//    public ResponseEntity<CustomerDTO> registerCustomer(
//            @Valid @RequestBody Map<String, Object> registrationData) {
//
//        try {
//            CustomerDTO customerDTO = // Extract from registrationData
//                    UserDTO userDTO = // Extract from registrationData
//
//                    CustomerDTO createdCustomer = customerService.createCustomer(customerDTO, userDTO);
//            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable int id,
            @Valid @RequestBody CustomerDTO customerDTO) {

        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable int id) {

        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable int id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<List<CustomerDTO>> getActiveCustomers() {
        List<CustomerDTO> customers = customerService.getActiveCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/status/{statusName}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<List<CustomerDTO>> getCustomersByStatus(@PathVariable String statusName) {
        List<CustomerDTO> customers = customerService.getCustomersByStatus(statusName);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String searchTerm) {
        List<CustomerDTO> customers = customerService.searchCustomers(searchTerm);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/created-between")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<CustomerDTO>> getCustomersCreatedBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<CustomerDTO> customers = customerService.getCustomersCreatedBetween(startDate, endDate);
        return ResponseEntity.ok(customers);
    }

//    @GetMapping("/statuses")
//    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
//    public ResponseEntity<List<Status>> getAllStatuses() {
//        List<Status> statuses = customerService.getAllStatuses();
//        return ResponseEntity.ok(statuses);
//    }
//
//    @GetMapping("/provinces")
//    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
//    public ResponseEntity<List<Province>> getAllProvinces() {
//        List<Province> provinces = customerService.getAllProvinces();
//        return ResponseEntity.ok(provinces);
//    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Long> getTotalCustomersCount() {
        long count = customerService.getTotalCustomersCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/new")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Long> getNewCustomersCount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {

        long count = customerService.getNewCustomersCount(since);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/check-email")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = customerService.isEmailExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-nic")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<Boolean> checkNicExists(@RequestParam String nicNo) {
        boolean exists = customerService.isNicExists(nicNo);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerByEmail(@PathVariable String email) {
        CustomerDTO customer = customerService.getCustomerByEmail(email);

        ApiResponse<CustomerDTO> response = ApiResponse.<CustomerDTO>builder()
                .success(true)
                .message("Customer retrieved successfully")
                .data(customer)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<ApiResponse<Page<CustomerDTO>>> getCustomersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CustomerDTO> customers = customerService.getCustomersWithPagination(pageable);

        ApiResponse<Page<CustomerDTO>> response = ApiResponse.<Page<CustomerDTO>>builder()
                .success(true)
                .message("Customers retrieved successfully")
                .data(customers)
                .build();

        return ResponseEntity.ok(response);
    }
}