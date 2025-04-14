package lk.ijse.worksphere.repository;

import lk.ijse.worksphere.entity.Attendance;
import lk.ijse.worksphere.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepo extends JpaRepository<Attendance, String> {
    Optional<Attendance> findTopByEmployeeIdOrderByDateDescInTimeDesc(String employeeId);
    List<Attendance> findTop2ByEmployeeIdOrderByDateDescInTimeDesc(String employeeId);
    List<Attendance> findByEmployeeIdAndDateBetween(String employeeId, LocalDate from, LocalDate to);
    boolean existsByEmployeeIdAndDate(String id, LocalDate today);
    Optional<Attendance> findTopByEmployeeIdAndDateOrderByInTimeDesc(String employeeId, LocalDate date);

    @Query("SELECT a FROM Attendance a " +
            "WHERE a.employee.id = :employeeId " +
            "AND a.date = CURRENT_DATE " +
            "AND a.outTime IS NULL " +
            "ORDER BY a.inTime DESC")
    List<Attendance> findAllClockInTodayWithoutOut(@Param("employeeId") String employeeId);

    @Query("SELECT a FROM Attendance a " +
            "WHERE a.employee.id = :employeeId " +
            "AND a.date = CURRENT_DATE " +
            "AND a.inTime IS NOT NULL AND a.outTime IS NOT NULL")
    Optional<Attendance> findCompletedAttendanceToday(@Param("employeeId") String employeeId);
}
