package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByEmployeeNoAndDeletedTimestampIsNull(String employeeNo);

    Optional<Employee> findByNicNoAndDeletedTimestampIsNull(String nicNo);

    List<Employee> findByDeletedTimestampIsNull();

//    boolean existsByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.deletedTimestamp IS NULL AND e.isActive = true")
    List<Employee> findActiveEmployees();

//    @Query("SELECT e FROM Employee e WHERE e.deletedTimestamp IS NULL AND e.role.value = :value")
//    List<Employee> findByRoleAndNotDeleted(@Param("roleName") String roleName);

    @Query("SELECT e FROM Employee e WHERE e.deletedTimestamp IS NULL AND " +
            "(LOWER(e.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.employeeNo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.nicNo) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Employee> searchEmployees(@Param("searchTerm") String searchTerm);

    @Query("SELECT e.employeeNo FROM Employee e WHERE e.id = :id")
    String findEmployeeNoById(@Param("id") Integer id);

    Optional<Employee> findByEmployeeNo(String employeeNo);

}
