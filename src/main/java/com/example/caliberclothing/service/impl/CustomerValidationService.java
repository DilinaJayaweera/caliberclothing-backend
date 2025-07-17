package com.example.caliberclothing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CustomerValidationService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    private static final Pattern MOBILE_PATTERN =
            Pattern.compile("^\\d{10}$");

    private static final Pattern NIC_PATTERN =
            Pattern.compile("^([0-9]{9}[x|X|v|V]|[0-9]{12})$");

    private static final Pattern ZIP_CODE_PATTERN =
            Pattern.compile("^\\d{5}$");

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidMobile(String mobile) {
        return mobile != null && MOBILE_PATTERN.matcher(mobile).matches();
    }

    public boolean isValidNic(String nic) {
        return nic != null && NIC_PATTERN.matcher(nic).matches();
    }

    public boolean isValidZipCode(String zipCode) {
        return zipCode != null && ZIP_CODE_PATTERN.matcher(zipCode).matches();
    }

    public boolean isValidAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return false;

        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age >= 0 && age <= 120;
    }

    public int calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
