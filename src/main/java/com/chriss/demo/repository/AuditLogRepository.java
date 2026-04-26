package com.chriss.demo.repository;

import com.chriss.demo.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    List<AuditLog> findByDeviceId(Long deviceId);
    
    List<AuditLog> findByAction(String action);
    
    List<AuditLog> findByPerformedBy(String performedBy);
    
    @Query("SELECT a FROM AuditLog a WHERE a.actionDate BETWEEN :startDate AND :endDate ORDER BY a.actionDate DESC")
    List<AuditLog> findAuditsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM AuditLog a WHERE a.device.id = :deviceId ORDER BY a.actionDate DESC")
    List<AuditLog> findDeviceAuditHistory(@Param("deviceId") Long deviceId);
    
    @Query("SELECT a.action, COUNT(a) FROM AuditLog a GROUP BY a.action ORDER BY COUNT(a) DESC")
    List<Object[]> countActionsByType();
}
