package com.Employee.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.Employee.entity.Shift;
import com.Employee.service.ShiftService;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @PostMapping("/{employeeId}")
    @PreAuthorize("hasRole('MANAGER')")
    public Shift addShift(@PathVariable UUID employeeId, @RequestBody Shift shift) {
        return shiftService.saveShift(employeeId, shift);
    }

    @GetMapping("/{empId}")
    public List<Shift> getEmployeeShifts(@PathVariable UUID empId) {
        return shiftService.getShiftsByEmployee(empId);
    }
}