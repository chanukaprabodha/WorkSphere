package lk.ijse.worksphere.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lk.ijse.worksphere.dto.LeaveDTO;
import lk.ijse.worksphere.entity.Employee;
import lk.ijse.worksphere.entity.Leave;
import lk.ijse.worksphere.repository.EmployeeRepo;
import lk.ijse.worksphere.repository.LeaveRepo;
import lk.ijse.worksphere.service.EmailService;
import lk.ijse.worksphere.service.LeaveService;
import lk.ijse.worksphere.util.IdGenerator;
import lk.ijse.worksphere.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lk.ijse.worksphere.entity.Leave.Status.*;

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

    @Autowired
    private EmailService emailService;

    private void sendLeaveStatusEmail(Employee employee, Leave leave, boolean accepted) {
        String subject = "Leave Request " + (accepted ? "Approved" : "Rejected");
        String content = buildHtmlEmailContent(employee, leave, accepted);
        emailService.sendHtmlEmail(employee.getEmail(), subject, content);
    }


    private String buildHtmlEmailContent(Employee employee, Leave leave, boolean accepted) {
        String statusColor = accepted ? "#28a745" : "#dc3545"; // green or red
        String statusText = accepted ? "APPROVED" : "REJECTED";

        return """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;">
            <h2 style="text-align: center; color: #343a40;">Leave Request Update</h2>
            <p>Dear <strong>%s</strong>,</p>
            <p>Your leave request from <strong>%s</strong> to <strong>%s</strong> has been:</p>
            <h3 style="color: %s; text-align: center;">%s</h3>
            <p>If you have any questions, please reach out to your supervisor or HR.</p>
            <hr style="margin: 30px 0;">
            <p style="font-size: 14px; color: #6c757d;">This is an automated message from the WorkSphere.</p>
        </div>
        """.formatted(
                employee.getFirstName() + " " + employee.getLastName(),
                leave.getStartDate(),
                leave.getEndDate(),
                statusColor,
                statusText
        );
    }


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

    @Override
    public int getPendingLeaveCount() {
        return leaveRepo.countPendingLeaves();
    }

    @Override
    public List<LeaveDTO> getPendingLeave() {
        List<Leave> leaves = leaveRepo.findByStatus(PENDING);
        return leaves.stream()
                .map(leave -> {
                   LeaveDTO dto = modelMapper.map(leave, LeaveDTO.class);
                   if (leave.getEmployee() != null) {
                       dto.setEmployeeId(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName());
                   }
                   return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateLeaveStatus(String leaveId, Leave.Status status) {
        System.out.println("Updating leave status for ID: " + leaveId + " to " + status);
        Leave leave = leaveRepo.findById(leaveId.trim())
                .orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus(status);
        leaveRepo.save(leave);

        Employee employee = leave.getEmployee();
        boolean accepted = (Leave.Status.valueOf(leave.getStatus().name()) == APPROVED);
        sendLeaveStatusEmail(employee, leave, accepted);

    }
}
