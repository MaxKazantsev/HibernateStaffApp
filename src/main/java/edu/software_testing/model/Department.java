package edu.software_testing.model;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "DEPARTMENTS", schema = "C##ULTHWE")
public class Department {

    @Id
    @Column(name = "DEP_ID", nullable = false, precision = 0)
    private int depId;

    @Basic
    @Column(name = "DEP_NAME", nullable = false, length = 45)
    private String depName;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private Set<Employee> departmentStaff;

    public Department(String depName) {
        this.depName = depName;
    }
    public Department() {

    }

    public int getDepId() {
        return depId;
    }
    public void setDepId(int depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return depName;
    }
    public void setDepName(String depName) {
        this.depName = depName;
        this.depName = this.depName.trim();
    }

    public Set<Employee> getDepartmentStaff() {
        return departmentStaff;
    }

    public void setDepartmentStaff(Set<Employee> departmentStaff) {
        this.departmentStaff = departmentStaff;
    }

    @Override
    public String toString() {
        return depName;
    }
}
