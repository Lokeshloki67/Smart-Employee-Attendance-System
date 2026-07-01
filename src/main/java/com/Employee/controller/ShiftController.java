package com.Employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.Employee.entity.Shift;
import com.Employee.service.ShiftService;

import java.util.List;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @PostMapping
    public Shift addShift(@RequestBody Shift shift) {
        return shiftService.saveShift(shift);
    }

    @GetMapping("/{empId}")
    public List<Shift> getEmployeeShifts(@PathVariable Long empId) {
        return shiftService.getShiftsByEmployee(empId);
    }
}
