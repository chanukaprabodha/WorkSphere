package lk.ijse.worksphere.service;

import lk.ijse.worksphere.dto.LeaveDTO;

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
}
