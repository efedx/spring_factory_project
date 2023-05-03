package com.example.project.services;

import com.example.project.model.Employee;
import com.example.project.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private EntityManager entityManager;

    @Autowired
    EmployeeService(EmployeeRepository employeeRepository, EntityManager entityManager) {
        this.employeeRepository = employeeRepository;
        this.entityManager = entityManager;
    }

//    public List<Employee> getAllEmployees(Boolean isDeleted) {
//
//        Session session = entityManager.unwrap(Session.class);
//        Filter filter = session.enableFilter("deletedEmployeeFilter");
//        filter.setParameter("isDeleted", isDeleted);
//        List<Employee> deletedEmployees = employeeRepo.findAll();
//        session.disableFilter("deletedEmployeeFilter");
//        return deletedEmployees;
//    }

    public List<Employee> getNonDeletedEmployees() {
        List<Employee> nonDeletedEmployees = employeeRepository.findByDeletedFalse();
        return nonDeletedEmployees;
    }

    public List<Employee> getDeletedEmployees() {
        List<Employee> deletedEmployees = employeeRepository.findByDeletedTrue();
        return deletedEmployees;
    }

    public void registerNewEmployee(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(employee.getEmail());
        if(employeeOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        if(!employeeRepository.existsById(id)) throw new IllegalStateException(id + " does not exists");
        else employeeRepository.deleteById(id);
    }

    @Transactional
    public void updateEmployee(Long id, String username, String email) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalStateException("employee with id " + id + " does not exist"));
        employee.setUsername(username);
        employee.setEmail(email);
    }
}