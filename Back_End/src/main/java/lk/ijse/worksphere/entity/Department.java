package lk.ijse.worksphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 09:49 AM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "department")
public class Department {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees;

    @Override
    public String toString() {
        return "Department{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
