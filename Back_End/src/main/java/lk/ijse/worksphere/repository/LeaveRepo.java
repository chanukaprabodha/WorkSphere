package lk.ijse.worksphere.repository;

import lk.ijse.worksphere.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-23
 * Time: 02:48 PM
 */
public interface LeaveRepo extends JpaRepository<LeaveRequest, String> {
}
