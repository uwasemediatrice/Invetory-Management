package com.chriss.demo.repository;

import com.chriss.demo.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByStatus(String status);
    
    List<Assignment> findByEmployeeId(String employeeId);
    
    List<Assignment> findByDepartment(String department);
    
    List<Assignment> findByDeviceId(Long deviceId);
    
    @Query("SELECT a FROM Assignment a WHERE a.device.id = :deviceId AND a.status = 'ACTIVE'")
    List<Assignment> findActiveAssignmentsByDevice(@Param("deviceId") Long deviceId);
    
    @Query("SELECT a FROM Assignment a WHERE a.assignedDate BETWEEN :startDate AND :endDate")
    List<Assignment> findAssignmentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a.department, COUNT(a) FROM Assignment a WHERE a.status = 'ACTIVE' GROUP BY a.department")
    List<Object[]> countAssignmentsByDepartment();
    
    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.status = 'ACTIVE'")
    long countActiveAssignments();
}
