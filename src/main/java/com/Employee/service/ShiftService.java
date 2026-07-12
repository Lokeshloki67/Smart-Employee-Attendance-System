package com.Employee.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Employee.entity.Employee;
import com.Employee.entity.Shift;
import com.Employee.repository.EmployeeRepo;
import com.Employee.repository.ShiftRepository;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private EmployeeRepo employeeRepo;

    public Shift saveShift(UUID employeeId, Shift shift) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        shift.setEmployee(employee);
        return shiftRepository.save(shift);
    }

    public List<Shift> getShiftsByEmployee(UUID empId) {
        return shiftRepository.findByEmployeeEmployeeId(empId);
    }
}