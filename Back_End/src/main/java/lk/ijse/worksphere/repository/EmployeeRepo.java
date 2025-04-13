package lk.ijse.worksphere.repository;

import lk.ijse.worksphere.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-17
 * Time: 10:13 PM
 */

public interface EmployeeRepo extends JpaRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);

    @Query("SELECT e " +
            "FROM Employee e " +
            "WHERE MONTH(e.birthday) = :month " +
            "AND DAY(e.birthday) >= :day " +
            "ORDER BY MONTH(e.birthday), DAY(e.birthday)"+
            "LIMIT 3")
    List<Employee> findUpcomingBirthdays(@Param("month") int month, @Param("day") int day);

    @Query("SELECT e " +
            "FROM Employee e " +
            "ORDER BY MONTH(e.birthday), DAY(e.birthday)")
    List<Employee> findAllByBirthdayOrder();
}
