package com.example.caliberclothing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "employee")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "employee_no", nullable = false, unique = true)
    @NotBlank(message = "Employee No is required")
    private String employeeNo;

    @Column(name = "full_name", nullable = false)
    @NotBlank(message = "Full Name is required")
    private String fullName;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First Name is required")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last Name is required")
    private String lastName;

    @Column(name = "dob", nullable = false)
    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @NotBlank(message = "Define sex is required")
    private String sex;

    @Column(name = "civil_status", nullable = false)
    @NotBlank(message = "Civil status is required")
    private String civilStatus;

    @Column(nullable = false)
    @NotBlank(message = "Address is required")
    private String address;

    @Column(name = "mobile_no", nullable = false)
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be exactly 10 digits")
    private String mobileNumber;

    @Column(name = "telephone_n0", nullable = true, unique = false)
    @Pattern(regexp = "\\d{10}", message = "Enter a valid telephone number")
    private String telephoneNumber;

    @Column(name = "nic_no", nullable = false, unique = true)
    @NotBlank(message = "NIC No is required")
    private String nicNo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_timestamp", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdTimestamp;

    @Column(name = "updated_timestamp")
    @UpdateTimestamp
    private LocalDateTime updatedTimestamp;

    @Column(name = "deleted_timestamp")
    private LocalDateTime deletedTimestamp;

    @Column(name = "created_user_id", nullable = false, updatable = false)
    private Integer createdUser;

    @Column(name = "updated_user_id")
    private Integer updatedUser;

    @Column(name = "deleted_user_id")
    private Integer deletedUser;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id", nullable = false)
//    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
