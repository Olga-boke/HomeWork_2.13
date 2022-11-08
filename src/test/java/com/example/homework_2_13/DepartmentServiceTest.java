package com.example.homework_2_13;

import com.example.homework_2_13.exception.EmployeeNotFoundException;
import com.example.homework_2_13.model.Employee;
import com.example.homework_2_13.service.DepartmentService;
import com.example.homework_2_13.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach()
    public void beforeEach () {
        List<Employee> employees = List.of(
                new Employee("Алла", "Нестерова", 1, 40000),
                new Employee("Иван", "Иванов", 1, 75000),
                new Employee("Петр", "Петров", 2, 80000),
                new Employee("Мария", "Кулькова", 2, 65000),
                new Employee("Александ", "Невский", 2, 90000)
        );
        when(employeeService.getAll()).thenReturn(employees);
    }

    @ParameterizedTest
    @MethodSource("employeeWithMaxSalaryParams")
    public void employeeWithMaxSalaryPositiveTest(int departmentId, Employee expected) {
        assertThat(departmentService.findEmployeeWithMaxSalaryFromDepartment(departmentId)).isEqualTo(expected);
    }

    @Test
    public void employeeWithMaxSalaryNegativeTest() {
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> departmentService.findEmployeeWithMaxSalaryFromDepartment(3));
    }

    @ParameterizedTest
    @MethodSource("employeeWithMinSalaryParams")
    public void employeeMinSalaryPositiveTest(int departmentId, Employee expected) {
        assertThat(departmentService.findEmployeeWithMinSalaryFromDepartment(departmentId)).isEqualTo(expected);
    }
    @Test
    public void employeeWithMinSalaryNegativeTest() {
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> departmentService.findEmployeeWithMinSalaryFromDepartment(3));
    }
    @ParameterizedTest
    @MethodSource("employeesFromDepartmentParams")
    public void employeesGroupedByDepartmentPositiveTest(int departmentId, List<Employee> expected) {
        assertThat(departmentService.findEmployeesFromDepartment(departmentId)).containsExactlyElementsOf(expected);
    }
    @Test
    public void employeesGroupedByDepartmentTest() {
        assertThat(departmentService.findEmployees()).containsAllEntriesOf(
                Map.of(
                        1, List.of(new Employee("Алла", "Нестерова", 1, 40000), new Employee("Иван", "Иванов", 1, 75000)),
                        2,List.of(new  Employee("Петр", "Петров", 2, 80000),new Employee("Мария", "Кулькова", 2, 65000), new Employee("Александ", "Невский", 2, 90000))
                )
        );
    }
    public static Stream<Arguments> employeeWithMaxSalaryParams() {
        return Stream.of(
                Arguments.of (1,new Employee("Иван", "Иванов", 1, 75000)),
                Arguments.of(2, new Employee("Александ", "Невский", 2, 90000))
        );
    }
    public static Stream<Arguments> employeeWithMinSalaryParams(){
        return Stream.of(
                Arguments.of(1, new Employee("Алла", "Нестерова", 1, 40000)),
                Arguments.of(2,new Employee("Мария", "Кулькова", 2, 65000))

        );
    }
    public static Stream<Arguments> employeesFromDepartmentParams() {
        return Stream.of(
                Arguments.of(1, List.of(new Employee("Алла", "Нестерова", 1, 40000), new Employee("Иван", "Иванов", 1, 75000))),
                Arguments.of(2, List.of(new Employee("Петр", "Петров", 2, 80000), new Employee("Мария", "Кулькова", 2, 65000), new Employee("Александ", "Невский", 2, 90000))),
                Arguments.of(3, Collections.emptyList())
        );
    }


}
