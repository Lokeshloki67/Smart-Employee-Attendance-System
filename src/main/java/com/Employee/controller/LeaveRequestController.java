package com.Employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.Employee.entity.LeaveRequest;
import com.Employee.service.LeaveRequestService;

@RestController
@RequestMapping("/leave")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService service;

    @GetMapping("/test")
    public String test() {
        return "Leave Request Controller Working";
    }

    @PostMapping("/apply")
    public LeaveRequest applyLeave(@RequestBody LeaveRequest leaveRequest) {

        return service.applyLeave(leaveRequest);
    }

    @GetMapping("/all")
    public List<LeaveRequest> getAll() {

        return service.getAll();
    }

    @GetMapping("/{employeeId}")
    public List<LeaveRequest> getByEmployee(@PathVariable Long employeeId) {

        return service.getByEmployeeId(employeeId);
    }

    @PutMapping("/approve/{id}")
    public LeaveRequest approveLeave(@PathVariable Long id) {

        return service.approve(id);
    }

    @PutMapping("/reject/{id}")
    public LeaveRequest rejectLeave(@PathVariable Long id) {

        return service.reject(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteLeave(@PathVariable Long id) {

        return service.delete(id);
    }
}
