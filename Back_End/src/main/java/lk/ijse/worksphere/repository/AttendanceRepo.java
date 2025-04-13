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
}
