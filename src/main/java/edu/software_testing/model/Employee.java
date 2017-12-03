package edu.software_testing.model;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "EMPLOYEES", schema = "C##ULTHWE")
public class Employee{

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    private int id = 0;

    @Basic
    @Column(name = "NAME", nullable = false, length = 45)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "HIRE_DATE", nullable = false)
    private LocalDate hireDate;

    @Basic
    @Column(name = "SALARY", nullable = false, precision = 0)
    private int salary;

    @ManyToOne
    @JoinColumn(name = "DEP_ID")
    private Department department;

    public Employee(String curName, int curSalary, LocalDate curHireDate, Department curDepartment) {
        this.name = curName;
        this.salary = curSalary;
        this.hireDate = curHireDate;
        this.department = curDepartment;
    }
    public Employee() {

    }

    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
        this.name = this.name.trim();
    }

    public LocalDate getHireDate() {
        return hireDate;
    }
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
}
