package com.Employee.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Employee.entity.Attendance;
import com.Employee.entity.Employee;
import com.Employee.entity.LeaveBalance;
import com.Employee.entity.LeaveRequest;
import com.Employee.entity.Shift;
import com.Employee.repository.AttendanceRepo;
import com.Employee.repository.EmployeeRepo;
import com.Employee.repository.LeaveBalanceRepository;
import com.Employee.repository.LeaveRequestRepository;
import com.Employee.repository.ShiftRepository;

@Service
public class AnalyticsService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    public Map<String, Object> getEmployeeDashboard(UUID employeeId) {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<Attendance> attendanceList = attendanceRepo.findByEmployeeEmployeeId(employeeId);
        YearMonth thisMonth = YearMonth.now();

        List<Attendance> thisMonthAttendance = attendanceList.stream()
                .filter(a -> a.getClockInTime() != null && YearMonth.from(a.getClockInTime()).equals(thisMonth))
                .collect(Collectors.toList());

        int daysPresent = thisMonthAttendance.size();

        double totalHoursThisMonth = thisMonthAttendance.stream()
                .filter(a -> a.getWorkHours() != null)
                .mapToDouble(Attendance::getWorkHours)
                .sum();

        double avgHoursPerDay = daysPresent == 0 ? 0.0 : Math.round((totalHoursThisMonth / daysPresent) * 100.0) / 100.0;

        Attendance lastAttendance = attendanceList.stream()
                .filter(a -> a.getClockInTime() != null)
                .max(Comparator.comparing(Attendance::getClockInTime))
                .orElse(null);

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeEmployeeId(employeeId);

        List<LeaveRequest> leaves = leaveRequestRepository.findByEmployeeEmployeeId(employeeId);

        long pendingCount = leaves.stream().filter(l -> "PENDING".equals(l.getStatus())).count();
        long approvedCount = leaves.stream().filter(l -> "APPROVED".equals(l.getStatus())).count();
        long rejectedCount = leaves.stream().filter(l -> "REJECTED".equals(l.getStatus())).count();

        Map<String, Double> leaveByType = leaves.stream()
                .filter(l -> "APPROVED".equals(l.getStatus()))
                .collect(Collectors.groupingBy(
                        LeaveRequest::getLeaveType,
                        Collectors.summingDouble(l -> l.getNumberOfDays() == null ? 0.0 : l.getNumberOfDays())
                ));

        LeaveRequest upcomingLeave = leaves.stream()
                .filter(l -> "APPROVED".equals(l.getStatus()) && l.getStartDate() != null
                        && !l.getStartDate().isBefore(LocalDate.now()))
                .min(Comparator.comparing(LeaveRequest::getStartDate))
                .orElse(null);

        List<Shift> shifts = shiftRepository.findByEmployeeEmployeeId(employeeId);
        List<Shift> upcomingShifts = shifts.stream()
                .filter(s -> s.getShiftDate() != null && !s.getShiftDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(Shift::getShiftDate))
                .collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("employeeId", employee.getEmployeeId());
        result.put("name", employee.getName());
        result.put("designation", employee.getDesignation());

        Map<String, Object> attendanceStats = new LinkedHashMap<>();
        attendanceStats.put("daysPresentThisMonth", daysPresent);
        attendanceStats.put("totalHoursThisMonth", totalHoursThisMonth);
        attendanceStats.put("avgHoursPerDay", avgHoursPerDay);
        attendanceStats.put("lastClockIn", lastAttendance != null ? lastAttendance.getClockInTime() : null);
        attendanceStats.put("lastClockOut", lastAttendance != null ? lastAttendance.getClockOutTime() : null);
        result.put("attendance", attendanceStats);

        Map<String, Object> leaveStats = new LinkedHashMap<>();
        leaveStats.put("totalPaidLeaves", balance != null ? balance.getTotalPaidLeaves() : 24);
        leaveStats.put("usedPaidLeaves", balance != null ? balance.getUsedPaidLeaves() : 0.0);
        leaveStats.put("remainingPaidLeaves", balance != null ? balance.getRemainingPaidLeaves() : 24.0);
        leaveStats.put("unpaidLeavesTaken", balance != null ? balance.getUnpaidLeavesTaken() : 0.0);
        leaveStats.put("pendingCount", pendingCount);
        leaveStats.put("approvedCount", approvedCount);
        leaveStats.put("rejectedCount", rejectedCount);
        leaveStats.put("leaveByType", leaveByType);
        leaveStats.put("upcomingLeave", upcomingLeave);
        result.put("leave", leaveStats);

        Map<String, Object> shiftStats = new LinkedHashMap<>();
        shiftStats.put("upcomingShiftsCount", upcomingShifts.size());
        shiftStats.put("nextShift", upcomingShifts.isEmpty() ? null : upcomingShifts.get(0));
        result.put("shifts", shiftStats);

        return result;
    }

    public Map<String, Object> getManagerDashboard() {

        List<Employee> employees = employeeRepo.findAll().stream()
                .filter(e -> e.getRole() == Employee.Role.EMPLOYEE)
                .collect(Collectors.toList());

        LocalDate today = LocalDate.now();
        YearMonth thisMonth = YearMonth.now();

        List<Attendance> allAttendance = attendanceRepo.findAll();

        List<String> clockedInTodayNames = new ArrayList<>();
        List<String> notClockedInTodayNames = new ArrayList<>();

        for (Employee emp : employees) {
            boolean clockedInToday = allAttendance.stream()
                    .anyMatch(a -> a.getEmployee() != null
                            && a.getEmployee().getEmployeeId().equals(emp.getEmployeeId())
                            && a.getClockInTime() != null
                            && a.getClockInTime().toLocalDate().equals(today));

            if (clockedInToday) {
                clockedInTodayNames.add(emp.getName());
            } else {
                notClockedInTodayNames.add(emp.getName());
            }
        }

        List<Attendance> thisMonthAttendance = allAttendance.stream()
                .filter(a -> a.getClockInTime() != null && YearMonth.from(a.getClockInTime()).equals(thisMonth)
                        && a.getWorkHours() != null)
                .collect(Collectors.toList());

        double avgTeamHoursThisMonth = thisMonthAttendance.isEmpty() ? 0.0 :
                Math.round((thisMonthAttendance.stream().mapToDouble(Attendance::getWorkHours).sum()
                        / thisMonthAttendance.size()) * 100.0) / 100.0;

        List<LeaveRequest> allLeaves = leaveRequestRepository.findAll();

        long pendingLeaveRequests = allLeaves.stream().filter(l -> "PENDING".equals(l.getStatus())).count();
        long approvedTotal = allLeaves.stream().filter(l -> "APPROVED".equals(l.getStatus())).count();
        long rejectedTotal = allLeaves.stream().filter(l -> "REJECTED".equals(l.getStatus())).count();

        Map<String, Double> leaveTypeBreakdown = allLeaves.stream()
                .filter(l -> "APPROVED".equals(l.getStatus()))
                .collect(Collectors.groupingBy(
                        LeaveRequest::getLeaveType,
                        Collectors.summingDouble(l -> l.getNumberOfDays() == null ? 0.0 : l.getNumberOfDays())
                ));

        double totalPaidDaysConsumed = 0.0;
        double totalUnpaidDaysConsumed = 0.0;
        List<String> nearingExhaustion = new ArrayList<>();

        for (Employee emp : employees) {
            LeaveBalance balance = leaveBalanceRepository.findByEmployeeEmployeeId(emp.getEmployeeId());
            if (balance != null) {
                totalPaidDaysConsumed += balance.getUsedPaidLeaves();
                totalUnpaidDaysConsumed += balance.getUnpaidLeavesTaken();
                if (balance.getRemainingPaidLeaves() <= 3) {
                    nearingExhaustion.add(emp.getName() + " (" + balance.getRemainingPaidLeaves() + " left)");
                }
            }
        }

        List<Shift> allShifts = shiftRepository.findAll();
        LocalDate weekEnd = today.plusDays(7);

        List<Map<String, Object>> upcomingShiftsThisWeek = allShifts.stream()
                .filter(s -> s.getShiftDate() != null && !s.getShiftDate().isBefore(today) && s.getShiftDate().isBefore(weekEnd))
                .sorted(Comparator.comparing(Shift::getShiftDate))
                .map(s -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("employeeName", s.getEmployee() != null ? s.getEmployee().getName() : null);
                    m.put("shiftDate", s.getShiftDate());
                    m.put("shiftType", s.getShiftType());
                    return m;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalEmployees", employees.size());

        Map<String, Object> attendanceStats = new LinkedHashMap<>();
        attendanceStats.put("clockedInTodayCount", clockedInTodayNames.size());
        attendanceStats.put("notClockedInToday", notClockedInTodayNames);
        attendanceStats.put("avgTeamHoursThisMonth", avgTeamHoursThisMonth);
        result.put("attendance", attendanceStats);

        Map<String, Object> leaveStats = new LinkedHashMap<>();
        leaveStats.put("pendingLeaveRequests", pendingLeaveRequests);
        leaveStats.put("approvedTotal", approvedTotal);
        leaveStats.put("rejectedTotal", rejectedTotal);
        leaveStats.put("leaveTypeBreakdown", leaveTypeBreakdown);
        leaveStats.put("totalPaidLeaveDaysConsumed", totalPaidDaysConsumed);
        leaveStats.put("totalUnpaidLeaveDaysConsumed", totalUnpaidDaysConsumed);
        leaveStats.put("employeesNearingLeaveExhaustion", nearingExhaustion);
        result.put("leave", leaveStats);

        Map<String, Object> shiftStats = new LinkedHashMap<>();
        shiftStats.put("upcomingShiftsThisWeek", upcomingShiftsThisWeek);
        result.put("shifts", shiftStats);

        return result;
    }
}