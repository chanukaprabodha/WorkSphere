package lk.ijse.worksphere.repository;

import lk.ijse.worksphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-19
 * Time: 09:43 AM
 */

public interface UserRepo extends JpaRepository<User, String> {

    User findByEmail(String userName);

    boolean existsByEmail(String userName);

    int deleteByEmail(String userName);

}
