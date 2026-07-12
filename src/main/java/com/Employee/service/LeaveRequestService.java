package com.Employee.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Employee.entity.Employee;
import com.Employee.entity.LeaveBalance;
import com.Employee.entity.LeaveRequest;
import com.Employee.repository.EmployeeRepo;
import com.Employee.repository.LeaveBalanceRepository;
import com.Employee.repository.LeaveRequestRepository;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository repository;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    public LeaveRequest applyLeave(UUID employeeId, LeaveRequest leave) {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        leave.setEmployee(employee);
        leave.setStatus("PENDING");

        if (leave.getDayType() == LeaveRequest.DayType.HALF_DAY) {
            if (!leave.getStartDate().equals(leave.getEndDate())) {
                throw new RuntimeException("Half day leave must have the same start and end date");
            }
            if (leave.getHalfDaySession() == null) {
                throw new RuntimeException("halfDaySession is required for half day leave");
            }
            leave.setNumberOfDays(0.5);
        } else {
            long days = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
            leave.setNumberOfDays((double) days);
        }

        return repository.save(leave);
    }

    public List<LeaveRequest> getAll() {
        return repository.findAll();
    }

    public List<LeaveRequest> getByEmployeeId(UUID employeeId) {
        return repository.findByEmployeeEmployeeId(employeeId);
    }

    public LeaveRequest approve(UUID id) {

        LeaveRequest leave = repository.findById(id).orElse(null);
        if (leave == null) {
            return null;
        }

        UUID employeeId = leave.getEmployee().getEmployeeId();

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeEmployeeId(employeeId);
        if (balance == null) {
            balance = new LeaveBalance();
            balance.setEmployee(leave.getEmployee());
            balance.setTotalPaidLeaves(24);
            balance.setUsedPaidLeaves(0.0);
            balance.setRemainingPaidLeaves(24.0);
            balance.setUnpaidLeavesTaken(0.0);
        }

        double duration = leave.getNumberOfDays();
        double availablePaid = balance.getRemainingPaidLeaves();
        double paidPortion = Math.min(availablePaid, duration);
        double unpaidPortion = duration - paidPortion;

        balance.setUsedPaidLeaves(balance.getUsedPaidLeaves() + paidPortion);
        balance.setRemainingPaidLeaves(balance.getRemainingPaidLeaves() - paidPortion);
        balance.setUnpaidLeavesTaken(balance.getUnpaidLeavesTaken() + unpaidPortion);

        if (unpaidPortion == 0) {
            leave.setLeaveCategory(LeaveRequest.LeaveCategory.PAID);
        } else if (paidPortion == 0) {
            leave.setLeaveCategory(LeaveRequest.LeaveCategory.UNPAID);
        } else {
            leave.setLeaveCategory(LeaveRequest.LeaveCategory.PARTIALLY_PAID);
        }

        leaveBalanceRepository.save(balance);

        leave.setStatus("APPROVED");
        return repository.save(leave);
    }

    public LeaveRequest reject(UUID id) {
        LeaveRequest leave = repository.findById(id).orElse(null);
        if (leave != null) {
            leave.setStatus("REJECTED");
            return repository.save(leave);
        }
        return null;
    }

    public String delete(UUID id) {
        repository.deleteById(id);
        return "Deleted successfully";
    }
}