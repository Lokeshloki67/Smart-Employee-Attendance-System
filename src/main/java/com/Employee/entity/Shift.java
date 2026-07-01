package com.Employee.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shiftId;

    private Long employeeId;  // foreign key reference (future mapping)

    private LocalDate shiftDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String shiftType; // Morning / Night / General

	public Long getShiftId() {
		return shiftId;
	}

	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public LocalDate getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(LocalDate shiftDate) {
		this.shiftDate = shiftDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getShiftType() {
		return shiftType;
	}

	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}

	public Shift(Long shiftId, Long employeeId, LocalDate shiftDate, LocalTime startTime, LocalTime endTime,
			String shiftType) {
		super();
		this.shiftId = shiftId;
		this.employeeId = employeeId;
		this.shiftDate = shiftDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.shiftType = shiftType;
	}

    
}