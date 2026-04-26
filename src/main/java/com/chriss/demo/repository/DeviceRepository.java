package com.chriss.demo.repository;

import com.chriss.demo.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    Optional<Device> findByAssetTag(String assetTag);
    
    Optional<Device> findBySerialNumber(String serialNumber);
    
    List<Device> findByStatus(String status);
    
    List<Device> findByDeviceType(String deviceType);
    
    List<Device> findByCondition(String condition);
    
    List<Device> findByManufacturer(String manufacturer);
    
    @Query("SELECT d FROM Device d WHERE LOWER(d.assetTag) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(d.serialNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(d.model) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(d.manufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Device> searchDevices(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(d) FROM Device d WHERE d.status = :status")
    long countByStatus(@Param("status") String status);
    
    @Query("SELECT d.deviceType, COUNT(d) FROM Device d GROUP BY d.deviceType")
    List<Object[]> countByDeviceType();
}
