package com.Employee.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Employee.entity.Attendance;

public interface AttendanceRepo extends JpaRepository<Attendance, UUID> {
    List<Attendance> findByEmployeeEmployeeId(UUID employeeId);
    Optional<Attendance> findTopByEmployeeEmployeeIdOrderByClockInTimeDesc(UUID employeeId);
}