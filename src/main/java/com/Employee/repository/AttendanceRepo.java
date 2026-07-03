package com.Employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Employee.entity.Attendance;

public interface AttendanceRepo
        extends JpaRepository<Attendance, Long> {

    List<Attendance> findByEmployeeEmployeeId(Long employeeId);

    Optional<Attendance>
            findTopByEmployeeEmployeeIdOrderByClockInTimeDesc(
                    Long employeeId);
}