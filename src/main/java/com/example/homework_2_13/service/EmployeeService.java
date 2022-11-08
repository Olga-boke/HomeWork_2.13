package com.example.homework_2_13.service;

import com.example.homework_2_13.exception.EmployeeAlreadyAddedException;
import com.example.homework_2_13.exception.EmployeeNotFoundException;
import com.example.homework_2_13.exception.EmployeeStorageIsFullException;
import com.example.homework_2_13.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class EmployeeService {

    private static final int LIMIT = 10;

    private final Map<String, Employee> employees = new HashMap<>();

    private final ValidatorService validatorService;
    public EmployeeService(ValidatorService validatorService){
        this.validatorService = validatorService;
    }

    private String getKey(String name, String surname){ return name + "|" + surname;}


    public Employee add(String name,
                        String surname,
                        int department,
                        double salary){
        Employee employee = new Employee(validatorService.validateName(name),
                validatorService.validateSurname(surname),
                department,
                salary);
        String key = getKey( name, surname); {
        if (employees.containsKey(key)) {
            throw new EmployeeAlreadyAddedException(" такой сотрудник уже есть ");
        }
        if (employees.size()< LIMIT){
            employees.put(key, employee);
            return employee;
        }
        throw new EmployeeStorageIsFullException();

        }
    }

    public Employee remove (String name, String surname){
        String key = getKey(name, surname);
        if (!employees.containsKey(key)) {
            throw new EmployeeNotFoundException(" Сотрудник не найден ");
        }
        return employees.remove(key);
    }

    public Employee find(String name, String surname) {
        String key = getKey(name, surname);
        if (!employees.containsKey(key)) {
            throw new EmployeeNotFoundException(" Сотрудник не найден ");
        }
        return employees.get(key);
    }

    public List<Employee> getAll() { return new ArrayList<>(employees.values());}
}
