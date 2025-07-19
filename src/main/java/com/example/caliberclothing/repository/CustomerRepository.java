package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmailAndDeletedTimestampIsNull(String email);

    Optional<Customer> findByNicNoAndDeletedTimestampIsNull(String nicNo);

    List<Customer> findByDeletedTimestampIsNull();

    @Query("SELECT c FROM Customer c WHERE c.deletedTimestamp IS NULL AND c.user.isActive = true")
    List<Customer> findActiveCustomers();

    @Query("SELECT c FROM Customer c WHERE c.deletedTimestamp IS NULL AND c.status.value = :value")
    List<Customer> findByStatusAndNotDeleted(@Param("value") String statusName);

    @Query("SELECT c FROM Customer c WHERE c.deletedTimestamp IS NULL AND " +
            "(LOWER(c.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.mobileNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.nicNo) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Customer> searchCustomers(@Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM Customer c WHERE c.deletedTimestamp IS NULL AND " +
            "c.createdTimestamp BETWEEN :startDate AND :endDate")
    List<Customer> findCustomersCreatedBetween(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.deletedTimestamp IS NULL")
    long countActiveCustomers();

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.deletedTimestamp IS NULL AND " +
            "c.createdTimestamp >= :startDate")
    long countNewCustomersSince(@Param("startDate") LocalDateTime startDate);

    boolean existsByEmailAndDeletedTimestampIsNull(String email);

    boolean existsByNicNoAndDeletedTimestampIsNull(String nicNo);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByNicNo(String nicNo);

//    boolean existsByEmail(String email);
//
//    boolean existsByNicNo(String nicNo);

    @Query("SELECT c FROM Customer c WHERE c.deletedTimestamp IS NULL AND c.id = :id")
    Optional<Customer> findActiveCustomerById(@Param("id") Integer id);

    @Query("SELECT c FROM Customer c WHERE c.deletedTimestamp IS NULL AND " +
            "(LOWER(c.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "c.nicNo LIKE CONCAT('%', :searchTerm, '%'))")
    List<Customer> searchActiveCustomers(@Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM Customer c WHERE c.deletedTimestamp IS NULL AND c.user.username = :username")
    Optional<Customer> findByUserUsernameAndDeletedTimestampIsNull(@Param("username") String username);

    Optional<Customer> findByUserId(Integer userId);
}
