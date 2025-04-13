package lk.ijse.worksphere.repository;

import lk.ijse.worksphere.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-23
 * Time: 02:48 PM
 */
public interface LeaveRepo extends JpaRepository<Leave, String> {
    @Query("SELECT l " +
            "FROM Leave l " +
            "WHERE l.employee.id = :empId " +
            "AND :today " +
            "BETWEEN l.startDate " +
            "AND l.endDate")
    Optional<Leave> findByEmployeeIdAndDate(@Param("empId") String empId, @Param("today") LocalDate today);

    @Query("SELECT COALESCE(SUM(l.days), 0) " +
            "FROM Leave l " +
            "WHERE l.employee = :empId " +
            "AND l.status = :status")
    int sumLeaveDaysByEmployeeIdAndStatus(@Param("empId") String empId, @Param("status") String status);

}
