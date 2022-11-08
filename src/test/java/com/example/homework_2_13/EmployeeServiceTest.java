package com.example.homework_2_13;

import com.example.homework_2_13.exception.*;
import com.example.homework_2_13.model.Employee;
import com.example.homework_2_13.service.EmployeeService;
import com.example.homework_2_13.service.ValidatorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class EmployeeServiceTest {
    private final EmployeeService employeeService = new EmployeeService(new ValidatorService());

    @AfterEach
    public void afterEach() {
        employeeService.getAll().forEach(employee -> employeeService.remove(employee.getName(), employee.getSurname()));
    }

    @ParameterizedTest
    @MethodSource("params")
    public void  addNegativeTest1(String name,
                                  String surname,
                                  int deprtment,
                                  double salary) {
        Employee expected = new Employee(name, surname, deprtment, salary);

        assertThat(employeeService.getAll()).isEmpty();
        employeeService.add(name, surname, deprtment,salary);
        assertThat(employeeService.getAll())
                .hasSize(1)
                .containsExactly(expected);
        assertThat(employeeService.find(expected.getName(), expected.getSurname()))
                .isNotNull()
                .isEqualTo(expected);
        assertThatExceptionOfType(EmployeeAlreadyAddedException.class)
                .isThrownBy(()-> employeeService.add(name,surname, deprtment,salary));
    }
    @ParameterizedTest
    @MethodSource("params")
    public void addNegativeTest2(String name,
                                 String surname,
                                 int department,
                                 double salary) {
        List<Employee> employees = generateEmployees( 10);
        employees.forEach(employee ->
                assertThat(employeeService.add(employee.getName(), employee.getSurname(), employee.getDepartment(), employee.getSalary())).isEqualTo(employee)
        );

        assertThatExceptionOfType(EmployeeStorageIsFullException.class)
                .isThrownBy(()-> employeeService.add(name,surname, department, salary));


    }
    @Test
    public void addNegativeTest3(){
        assertThatExceptionOfType(IncorrectNameException.class)
                .isThrownBy(() -> employeeService.add("Ivan#", "Ivanov", 1,40000));

        assertThatExceptionOfType(IncorrectSurnameException.class)
                .isThrownBy(() -> employeeService.add("Petr", "Petrov!",2,55000));

        assertThatExceptionOfType(IncorrectNameException.class)
                .isThrownBy(() -> employeeService.add(null,"Vasileva",3,80000));
    }

    @ParameterizedTest
    @MethodSource("params")
    public void removeNegativeTest(String name,
                                   String surname,
                                   int department,
                                   double salary) {
        assertThat(employeeService.getAll()).isEmpty();
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.remove("test", "test"));

        Employee expected = new Employee(name, surname, department, salary);
        employeeService.add(name, surname, department, salary);
        assertThat(employeeService.getAll())
                .hasSize(1)
                .containsExactly(expected);

        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.remove("test", "test"));
    }

    @ParameterizedTest
    @MethodSource("params")
    public void removePositiveTest(String name,
                                   String surname,
                                   int department,
                                   double salary){
        assertThat(employeeService.getAll().isEmpty());
        Employee expected = new Employee(name, surname, department, salary);
        assertThat(employeeService.add(name, surname, department, salary)).isEqualTo(expected);

        assertThat(employeeService.getAll())
                .hasSize(1)
                .containsExactly(expected);
        assertThat(employeeService.remove(name,surname)).isEqualTo(expected);
        assertThat(employeeService.getAll()).isEmpty();

    }

    @ParameterizedTest
    @MethodSource("params")
    public void findNegativeTest(String name,
                                 String surname,
                                 int department,
                                 double salary) {
        assertThat(employeeService.getAll()).isEmpty();
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(()-> employeeService.find( "test", "test"));

        Employee expected = new Employee(name,surname,department,salary);
        employeeService.add(name, surname, department, salary);
        assertThat(employeeService.getAll())
                .hasSize(1)
                .containsExactly(expected);

        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(()-> employeeService.find("test", "test"));
    }

    @ParameterizedTest
    @MethodSource("params")
    public void findPositiveTest( String name,
                                 String surname,
                                 int department,
                                 double salary) {
        assertThat(employeeService.getAll()).isEmpty();
        Employee expected = new Employee(name, surname, department,salary);
        employeeService.add(name, surname, department, salary);

        assertThat(employeeService.getAll())
                .hasSize(1)
                .containsExactly(expected);

        assertThat(employeeService.find(name, surname)).isEqualTo(expected);
    }


    private List<Employee> generateEmployees(int size) {
        return Stream.iterate(1, i-> i+1 )
                .limit(size)
                .map(i-> new Employee ("Name" + (char) ((int) 'a' + i), "Surname"+ (char) ((int) 'a' + i), i, 10000 + i))
                .collect(Collectors.toList());
    }
    public static Stream<Arguments> params(){
        return Stream.of(
                Arguments.of( "Ivan","Ivanov",1, 40000),
                Arguments.of("Petr", "Petrov",2,55000),
                Arguments.of("Mariya","Vasileva",3,80000)
        );
    }

}
