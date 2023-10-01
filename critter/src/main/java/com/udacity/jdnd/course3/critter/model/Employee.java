package com.udacity.jdnd.course3.critter.model;

import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Table(name="employee")
public class Employee {
    @Id
    @GeneratedValue
    @Column(name="employee_id")
    private Long id;

    @Type(type="nstring")
    @Column(name="employee_name", length = 255)
    @Nationalized // should use @Nationalized instead of @Type=nstring
    private String name;

    @ElementCollection
    @CollectionTable(
            name="employee_skills",
            joinColumns = @JoinColumn(name="employee_employee_id"), uniqueConstraints = @UniqueConstraint(columnNames = {"employee_employee_id", "skills"}))
    @Column(name="skills")
    private Set<EmployeeSkill> skills;

    @ElementCollection
    @CollectionTable(
            name="employee_days_available",
            joinColumns = @JoinColumn(name="employee_employee_id"), uniqueConstraints = @UniqueConstraint(columnNames = {"employee_employee_id", "daysAvailable"}))
    @Column(name="daysAvailable")
    private Set<DayOfWeek> daysAvailable;

    public Employee() {}

    public Employee(EmployeeDTO employeeDTO) {
        this.id = employeeDTO.getId();
        this.name = employeeDTO.getName();
        this.skills = employeeDTO.getSkills();
        this.daysAvailable = employeeDTO.getDaysAvailable();
    }

    public Employee(Long id, String name, Set<EmployeeSkill> skills, Set<DayOfWeek> daysAvailable) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.daysAvailable = daysAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EmployeeSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public Set<DayOfWeek> getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(Set<DayOfWeek> daysAvailable) {
        this.daysAvailable = daysAvailable;
    }
}
