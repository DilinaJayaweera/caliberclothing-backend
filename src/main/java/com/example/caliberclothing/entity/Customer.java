package com.example.caliberclothing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fullname", nullable = false)
    @NotBlank(message = "Full Name is required")
    private String fullName;

    @Column(name = "firstname", nullable = false)
    @NotBlank(message = "First Name is required")
    private String firstName;

    @Column(name = "lastname", nullable = false)
    @NotBlank(message = "Last Name is required")
    private String lastName;

    @Column(name = "dob", nullable = false)
    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @NotBlank(message = "Define sex is required")
    private String sex;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

//    @Column(name = "civil_status", nullable = false)
//    @NotBlank(message = "Civil status is required")
//    private String civilStatus;

    @Column(nullable = false)
    @NotBlank(message = "Address is required")
    private String address;

    @Column(nullable = false)
    @NotBlank(message = "Country is required")
    private String country;

    @Column(name = "zip_code", nullable = false)
    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "\\d{5}", message = "Zip code must be exactly 5 digits")
    private String zipCode;

    @Column(name = "mobile_no", nullable = false, length = 10)
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be exactly 10 digits")
    private String mobileNumber;

    @Column(name = "nic_no", nullable = false, unique = true)
    @NotBlank(message = "NIC No is required")
    private String nicNo;

//    @Column(name = "is_active", nullable = false)
//    private Boolean isActive;

    @Column(name = "created_timestamp", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdTimestamp;

    @Column(name = "updated_timestamp")
    @UpdateTimestamp
    private LocalDateTime updatedTimestamp;

    @Column(name = "deleted_timestamp")
    private LocalDateTime deletedTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

}
