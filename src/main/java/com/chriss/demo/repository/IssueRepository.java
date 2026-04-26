package com.chriss.demo.repository;

import com.chriss.demo.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    
    List<Issue> findByStatus(String status);
    
    List<Issue> findByDeviceId(Long deviceId);
    
    List<Issue> findByIssueType(String issueType);
    
    List<Issue> findBySeverity(String severity);
    
    @Query("SELECT i FROM Issue i WHERE i.reportedDate BETWEEN :startDate AND :endDate")
    List<Issue> findIssuesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT i.severity, COUNT(i) FROM Issue i WHERE i.status != 'CLOSED' GROUP BY i.severity")
    List<Object[]> countOpenIssuesBySeverity();
    
    @Query("SELECT COUNT(i) FROM Issue i WHERE i.status = :status")
    long countByStatus(@Param("status") String status);
    
    List<Issue> findByDeviceIdOrderByReportedDateDesc(Long deviceId);
}
