package app;

import java.util.Objects;

/**
 * Employee class representing an employee in the company
 * Contains basic employee information: name, age, department, and salary
 */
public class Employee {
    private String name;
    private int age;
    private String department;
    private double salary;

    /**
     * Constructor for Employee class
     * @param name Employee's name
     * @param age Employee's age
     * @param department Employee's department
     * @param salary Employee's salary
     */
    public Employee(String name, int age, String department, double salary) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.salary = salary;
    }

    // Getters
    public String getName()       { return name; }
    public int getAge()           { return age; }
    public String getDepartment() { return department; }
    public double getSalary()     { return salary; }

    // Setters
    public void setName(String name)             { this.name = name; }
    public void setAge(int age)                  { this.age = age; }
    public void setDepartment(String department) { this.department = department; }
    public void setSalary(double salary)         { this.salary = salary; }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return age == employee.age &&
                Double.compare(employee.salary, salary) == 0 &&
                Objects.equals(name, employee.name) &&
                Objects.equals(department, employee.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, department, salary);
    }
}