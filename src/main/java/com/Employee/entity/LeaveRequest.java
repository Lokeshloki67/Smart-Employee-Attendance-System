package com.Employee.entity;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "leave_request")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID leaveId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String leaveType;
    private String reason;

    @Enumerated(EnumType.STRING)
    private DayType dayType;

    @Enumerated(EnumType.STRING)
    private HalfDaySession halfDaySession;

    private LocalDate startDate;
    private LocalDate endDate;

    private Double numberOfDays;

    @Enumerated(EnumType.STRING)
    private LeaveCategory leaveCategory;

    private String status;

    public enum DayType { FULL_DAY, HALF_DAY }
    public enum HalfDaySession { FIRST_HALF, SECOND_HALF }
    public enum LeaveCategory { PAID, UNPAID, PARTIALLY_PAID }

    public UUID getLeaveId() { return leaveId; }
    public void setLeaveId(UUID leaveId) { this.leaveId = leaveId; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public DayType getDayType() { return dayType; }
    public void setDayType(DayType dayType) { this.dayType = dayType; }
    public HalfDaySession getHalfDaySession() { return halfDaySession; }
    public void setHalfDaySession(HalfDaySession halfDaySession) { this.halfDaySession = halfDaySession; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Double getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(Double numberOfDays) { this.numberOfDays = numberOfDays; }
    public LeaveCategory getLeaveCategory() { return leaveCategory; }
    public void setLeaveCategory(LeaveCategory leaveCategory) { this.leaveCategory = leaveCategory; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}