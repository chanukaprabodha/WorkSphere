package lk.ijse.worksphere.repository;

import lk.ijse.worksphere.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepo extends JpaRepository<Attendance, String> {
    Optional<Attendance> findTopByEmployeeIdOrderByInTimeDesc(String employeeId);
    List<Attendance> findByEmployeeIdAndInTimeAfter(String employeeId, LocalDateTime dateTime);
}
