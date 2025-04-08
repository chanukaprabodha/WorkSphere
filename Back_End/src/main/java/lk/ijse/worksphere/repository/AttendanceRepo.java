package lk.ijse.worksphere.repository;

import lk.ijse.worksphere.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepo extends JpaRepository<Attendance, String> {
    Optional<Attendance> findTopByEmployeeIdOrderByDateDescInTimeDesc(String employeeId);
    List<Attendance> findTop2ByEmployeeIdOrderByDateDescInTimeDesc(String employeeId);

}
