package com.Employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Employee.entity.Shift;
import com.Employee.repository.ShiftRepository;

import java.util.List;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    public Shift saveShift(Shift shift) {
        return shiftRepository.save(shift);
    }

    public List<Shift> getShiftsByEmployee(Long empId) {
        return shiftRepository.findByEmployeeId(empId);
    }
}
