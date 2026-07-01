package com.Employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Employee.entity.LeaveRequest;
import com.Employee.repository.LeaveRequestRepository;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository repository;

    // ✅ Apply Leave
    public LeaveRequest applyLeave(LeaveRequest leave) {
        leave.setStatus("PENDING");
        return repository.save(leave);
    }

    // ✅ Get all
    public List<LeaveRequest> getAll() {
        return repository.findAll();
    }

    // ✅ Get by employee
    public List<LeaveRequest> getByEmployeeId(Long employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    // ✅ Approve
    public LeaveRequest approve(Long id) {
        LeaveRequest leave = repository.findById(id).orElse(null);
        if (leave != null) {
            leave.setStatus("APPROVED");
            return repository.save(leave);
        }
        return null;
    }

    // ✅ Reject
    public LeaveRequest reject(Long id) {
        LeaveRequest leave = repository.findById(id).orElse(null);
        if (leave != null) {
            leave.setStatus("REJECTED");
            return repository.save(leave);
        }
        return null;
    }

    // ✅ Delete
    public String delete(Long id) {
        repository.deleteById(id);
        return "Deleted successfully";
    }
}