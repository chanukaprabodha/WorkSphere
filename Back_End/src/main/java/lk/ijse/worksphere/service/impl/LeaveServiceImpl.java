package lk.ijse.worksphere.service.impl;

import lk.ijse.worksphere.dto.LeaveDTO;
import lk.ijse.worksphere.entity.Employee;
import lk.ijse.worksphere.entity.Leave;
import lk.ijse.worksphere.repository.EmployeeRepo;
import lk.ijse.worksphere.repository.LeaveRepo;
import lk.ijse.worksphere.service.LeaveService;
import lk.ijse.worksphere.util.IdGenerator;
import lk.ijse.worksphere.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lk.ijse.worksphere.entity.Leave.Status.APPROVED;
import static lk.ijse.worksphere.entity.Leave.Status.REJECTED;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-23
 * Time: 02:51 PM
 */

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveRepo leaveRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void applyLeave(String token,LeaveDTO leaveDTO) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token.substring(7));
        Employee employee = employeeRepo.findById(employeeIdFromToken)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        int leaveDays = (int) ChronoUnit.DAYS.between(leaveDTO.getStartDate(), leaveDTO.getEndDate());
        System.out.println(leaveDays);

        switch (leaveDTO.getLeaveType().toString().toLowerCase()) {
            case "annual":
                if (employee.getAnnualLeaves() < leaveDays) {
                    throw new RuntimeException("Insufficient annual leave balance");
                }
                employee.setAnnualLeaves(employee.getAnnualLeaves() - leaveDays);
                break;

            case "casual":
                if (employee.getCasualLeave() < leaveDays) {
                    throw new RuntimeException("Insufficient casual leave balance");
                }
                employee.setCasualLeave(employee.getCasualLeave() - leaveDays);
                break;

            case "sick":
                if (employee.getSickLeave() < leaveDays) {
                    throw new RuntimeException("Insufficient sick leave balance");
                }
                employee.setSickLeave(employee.getSickLeave() - leaveDays);
                break;

            default:
                throw new RuntimeException("Invalid leave type");
        }

        //update leave balance
        employee.setLeaveBalance(
                employee.getAnnualLeaves()+ employee.getCasualLeave()+ employee.getSickLeave()
        );

        String generatedId;
        do {
            generatedId = IdGenerator.generateId("LEV");
        } while (leaveRepo.existsById(generatedId));
        leaveDTO.setId(generatedId);
        leaveDTO.setEmployeeId(employeeIdFromToken);
        leaveDTO.setLeaveDays(leaveDays);
        try {
            leaveRepo.save(modelMapper.map(leaveDTO, Leave.class));
            employeeRepo.save(employee);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void approveLeave(String leaveId) {
        Leave leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus(APPROVED);

        leaveRepo.save(leave);
    }

    @Override
    public void rejectLeave(String token, String leaveId) {
        Leave leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        int leaveDays = (int) ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;

        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token);
        Employee employee = employeeRepo.findById(employeeIdFromToken)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        switch (leave.getLeaveType().toString().toLowerCase()){
            case "annual":
                employee.setAnnualLeaves(employee.getAnnualLeaves() + leaveDays);
                break;

            case "casual":
                employee.setCasualLeave(employee.getCasualLeave() + leaveDays);
                break;

            case "sick":
                employee.setSickLeave(employee.getSickLeave() + leaveDays);
                break;

            default:
                throw new RuntimeException("Invalid leave type");
        }
        leave.setStatus(REJECTED);
        leaveRepo.save(leave);
        employeeRepo.save(employee);
    }

    @Override
    public List<String> getLeaveTypes() {
        return Arrays.stream(Leave.Type.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveDTO> getRecentLeave(String token) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token.substring(7));
        return leaveRepo.findRecentLeaves(employeeIdFromToken)
                .stream()
                .limit(3)
                .map(leave -> modelMapper.map(leave, LeaveDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveDTO> getLeaveHistory(String token) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token.substring(7));
        return leaveRepo.findByEmployeeId(employeeIdFromToken)
                .stream()
                .map(leave -> modelMapper.map(leave, LeaveDTO.class))
                .collect(Collectors.toList());
    }
}
