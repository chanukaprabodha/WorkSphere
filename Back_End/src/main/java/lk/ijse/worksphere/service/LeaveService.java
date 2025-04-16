package lk.ijse.worksphere.service;

import lk.ijse.worksphere.dto.LeaveDTO;
import lk.ijse.worksphere.entity.Leave;

import java.util.List;
import java.util.Map;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-23
 * Time: 02:51 PM
 */
public interface LeaveService {

    void applyLeave(String token, LeaveDTO leaveDTO);

    void approveLeave(String leaveId);

    void rejectLeave(String token, String leaveId);

    List<String> getLeaveTypes();

    List<LeaveDTO> getRecentLeave(String token);

    List<LeaveDTO> getLeaveHistory(String token);

    int getPendingLeaveCount();

    List<LeaveDTO> getPendingLeave();

    void updateLeaveStatus(String leaveId, Leave.Status status);
}
