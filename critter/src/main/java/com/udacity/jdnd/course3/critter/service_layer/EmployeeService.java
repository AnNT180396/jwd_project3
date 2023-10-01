package com.udacity.jdnd.course3.critter.service_layer;

import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.repository_layer.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * saveEmployee
     * @param employee
     * @return employee
     */
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * getEmployeesByService
     * @param employeeDTO
     * @return list employee
     */
    public List<Employee> findEmployeesForService(EmployeeRequestDTO employeeDTO){
        List<Employee> employees = employeeRepository
                .findEmployeeByDaysAvailable(employeeDTO.getDate().getDayOfWeek());
        if (!CollectionUtils.isEmpty(employees)) {
            return employees.stream().filter(employee -> employee.getSkills().containsAll(employeeDTO.getSkills()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * getEmployee
     * @param employeeId
     * @return employee
     */
    public Employee getEmployee(Long employeeId) {
        return employeeRepository.getOne(employeeId);
    }

    /**
     * setEmployeeAvailability
     * @param daysAvailable
     * @param employeeId
     */
    public void setAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

}
