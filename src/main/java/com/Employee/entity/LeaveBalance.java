package com.Employee.entity;

import java.util.UUID;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "leave_balance")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID balanceId;

    @OneToOne
    @JoinColumn(name = "employee_id", unique = true)
    private Employee employee;

    private Integer totalPaidLeaves = 24;
    private Double usedPaidLeaves = 0.0;
    private Double remainingPaidLeaves = 24.0;
    private Double unpaidLeavesTaken = 0.0;

    public UUID getBalanceId() { return balanceId; }
    public void setBalanceId(UUID balanceId) { this.balanceId = balanceId; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public Integer getTotalPaidLeaves() { return totalPaidLeaves; }
    public void setTotalPaidLeaves(Integer totalPaidLeaves) { this.totalPaidLeaves = totalPaidLeaves; }
    public Double getUsedPaidLeaves() { return usedPaidLeaves; }
    public void setUsedPaidLeaves(Double usedPaidLeaves) { this.usedPaidLeaves = usedPaidLeaves; }
    public Double getRemainingPaidLeaves() { return remainingPaidLeaves; }
    public void setRemainingPaidLeaves(Double remainingPaidLeaves) { this.remainingPaidLeaves = remainingPaidLeaves; }
    public Double getUnpaidLeavesTaken() { return unpaidLeavesTaken; }
    public void setUnpaidLeavesTaken(Double unpaidLeavesTaken) { this.unpaidLeavesTaken = unpaidLeavesTaken; }
}