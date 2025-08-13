package app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Controller class for the main application FXML
 */
public class AppController {
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private TextArea outputArea;
    
    private List<Employee> currentEmployees = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    @FXML
    public void initialize() {
        System.out.println("AppController initialized");
        updateStatus("Ready to process datasets");
    }
    
    @FXML
    private void generateRandomDataset() {
        updateStatus("Generating random dataset...");
        outputArea.clear();
        
        CompletableFuture.runAsync(() -> {
            try {
                List<Employee> employees = generateLargeDataset();
                currentEmployees = employees;
                
                // Save to dataset.json
                String json = gson.toJson(employees);
                String resourcePath = getClass().getResource("/dataset.json").getPath();
                try (FileWriter writer = new FileWriter(resourcePath)) {
                    writer.write(json);
                }
                
                Platform.runLater(() -> {
                    updateStatus("Random dataset generated successfully! Size: " + formatFileSize(json.length()));
                    outputArea.appendText("Generated " + employees.size() + " employee records\n");
                    outputArea.appendText("Dataset saved to: " + resourcePath + "\n\n");
                });
                
            } catch (Exception e) {
                Platform.runLater(() -> {
                    updateStatus("Error generating dataset: " + e.getMessage());
                    outputArea.appendText("Error: " + e.getMessage() + "\n");
                });
            }
        });
    }
    
    @FXML
    private void uploadCustomDataset() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Dataset JSON File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            updateStatus("Loading custom dataset...");
            outputArea.clear();
            
            CompletableFuture.runAsync(() -> {
                try {
                    // Read and validate the file
                    String content = new String(java.nio.file.Files.readAllBytes(selectedFile.toPath()));
                    List<Employee> employees = gson.fromJson(content, new TypeToken<List<Employee>>(){}.getType());
                    
                    if (employees == null || employees.isEmpty()) {
                        throw new IllegalArgumentException("Invalid JSON structure or empty dataset");
                    }
                    
                    // Validate each employee
                    for (int i = 0; i < employees.size(); i++) {
                        Employee emp = employees.get(i);
                        if (emp.getName() == null || emp.getName().trim().isEmpty()) {
                            throw new IllegalArgumentException("Employee at index " + i + " has invalid name");
                        }
                        if (emp.getAge() <= 0 || emp.getAge() > 120) {
                            throw new IllegalArgumentException("Employee " + emp.getName() + " has invalid age: " + emp.getAge());
                        }
                        if (emp.getSalary() < 0) {
                            throw new IllegalArgumentException("Employee " + emp.getName() + " has invalid salary: " + emp.getSalary());
                        }
                    }
                    
                    currentEmployees = employees;
                    
                    // Copy to resources folder
                    String resourcePath = getClass().getResource("/dataset.json").getPath();
                    try (FileWriter writer = new FileWriter(resourcePath)) {
                        writer.write(content);
                    }
                    
                    Platform.runLater(() -> {
                        updateStatus("Custom dataset loaded successfully! Size: " + formatFileSize(content.length()));
                        outputArea.appendText("Loaded " + employees.size() + " employee records from: " + selectedFile.getName() + "\n");
                        outputArea.appendText("Dataset copied to: " + resourcePath + "\n\n");
                    });
                    
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        updateStatus("Error loading dataset: " + e.getMessage());
                        outputArea.appendText("Error: " + e.getMessage() + "\n");
                        outputArea.appendText("Please ensure your JSON file has the correct structure:\n");
                        outputArea.appendText("[\n");
                        outputArea.appendText("  {\n");
                        outputArea.appendText("    \"name\": \"Employee Name\",\n");
                        outputArea.appendText("    \"age\": 25,\n");
                        outputArea.appendText("    \"department\": \"Department Name\",\n");
                        outputArea.appendText("    \"salary\": 50000.0\n");
                        outputArea.appendText("  }\n");
                        outputArea.appendText("]\n");
                    });
                }
            });
        }
    }
    
    @FXML
    private void processDataset() {
        if (currentEmployees.isEmpty()) {
            updateStatus("No dataset loaded. Please generate or upload a dataset first.");
            outputArea.appendText("No dataset available for processing.\n");
            return;
        }
        
        updateStatus("Processing dataset...");
        outputArea.clear();
        
        CompletableFuture.runAsync(() -> {
            try {
                StringBuilder output = new StringBuilder();
                List<Employee> employees = currentEmployees;
                
                // 1. Dataset info
                output.append("1. Dataset loaded with ").append(employees.size()).append(" employees\n\n");

                // 2. Function interface demonstration
                Function<Employee, String> nameDepartmentFunction = employee -> 
                    employee.getName() + " - " + employee.getDepartment();
                
                output.append("2. Function interface demonstration:\n");
                output.append("Sample output: ").append(nameDepartmentFunction.apply(employees.get(0))).append("\n\n");

                // 3. Stream operations - optimized to avoid multiple iterations
                output.append("3. Stream operations:\n");
                
                // Single pass to collect all data
                Map<String, List<Employee>> employeesByDepartment = employees.stream()
                        .collect(Collectors.groupingBy(Employee::getDepartment));
                
                // Calculate statistics in one pass
                DoubleSummaryStatistics salaryStats = employees.stream()
                        .mapToDouble(Employee::getSalary)
                        .summaryStatistics();
                
                // Filter employees above 30 in one pass
                List<Employee> employeesAbove30 = employees.stream()
                        .filter(employee -> employee.getAge() > 30)
                        .collect(Collectors.toList());
                
                // Find highest paid engineer
                Optional<Employee> highestPaidEngineer = employees.stream()
                        .filter(emp -> "Engineering".equals(emp.getDepartment()))
                        .max(Comparator.comparingDouble(Employee::getSalary));
                
                // Build output efficiently
                output.append("Average salary: $").append(String.format("%.2f", salaryStats.getAverage())).append("\n");
                output.append("Employees above 30: ").append(employeesAbove30.size()).append("\n");
                
                highestPaidEngineer.ifPresent(emp -> 
                    output.append("Highest paid engineer: ").append(emp.getName()).append(" - $").append(emp.getSalary()).append("\n"));
                
                output.append("\nEmployees by department:\n");
                employeesByDepartment.forEach((dept, empList) -> {
                    output.append(dept).append(": ").append(empList.size()).append(" employees\n");
                });

                // Calculate average salary by department efficiently
                output.append("\nAverage salary by department:\n");
                employeesByDepartment.forEach((dept, empList) -> {
                    double avgSalary = empList.stream()
                            .mapToDouble(Employee::getSalary)
                            .average()
                            .orElse(0.0);
                    output.append(String.format("%s: $%.2f%n", dept, avgSalary));
                });

                // 4. Summary
                output.append("\n=== SUMMARY ===\n");
                output.append("Function Interface: Single abstract method R apply(T t)\n");
                output.append("Streams: Functional operations on collections with lazy evaluation\n");
                output.append("Performance: Optimized with single-pass operations\n");
                
                final String result = output.toString();
                Platform.runLater(() -> {
                    outputArea.setText(result);
                    updateStatus("Dataset processing completed successfully!");
                });
                
            } catch (Exception e) {
                Platform.runLater(() -> {
                    updateStatus("Error processing dataset: " + e.getMessage());
                    outputArea.appendText("Error: " + e.getMessage() + "\n");
                });
            }
        });
    }
    
    private List<Employee> generateLargeDataset() {
        ArrayList<Employee> employees = new ArrayList<>();
        Random random = new Random();
        
        String[] firstNames = {"John", "Sarah", "Michael", "Emily", "David", "Lisa", "Robert", "Jennifer", 
                              "Christopher", "Amanda", "James", "Jessica", "William", "Ashley", "Daniel", 
                              "Stephanie", "Matthew", "Nicole", "Anthony", "Elizabeth", "Joshua", "Helen", 
                              "Andrew", "Deborah", "Ryan", "Lisa", "Jacob", "Nancy", "Gary", "Karen"};
        
        String[] lastNames = {"Smith", "Johnson", "Brown", "Davis", "Wilson", "Anderson", "Taylor", "Martinez", 
                             "Garcia", "Rodriguez", "Miller", "Moore", "Jackson", "Martin", "Lee", "Thompson", 
                             "White", "Harris", "Clark", "Lewis", "Robinson", "Walker", "Young", "Allen", 
                             "King", "Wright", "Scott", "Torres", "Nguyen", "Hill"};
        
        String[] departments = {"Engineering", "Marketing", "HR", "Finance", "Sales", "Operations", "IT", "Legal"};
        
        // Pre-allocate capacity for better performance
        int targetEmployees = 45000;
        employees.ensureCapacity(targetEmployees);
        
        for (int i = 0; i < targetEmployees; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String name = firstName + " " + lastName;
            
            int age = 22 + random.nextInt(58);
            String department = departments[random.nextInt(departments.length)];
            double salary = 30000 + random.nextDouble() * 120000;
            
            employees.add(new Employee(name, age, department, salary));
        }
        
        return employees;
    }
    
    private void updateStatus(String message) {
        Platform.runLater(() -> statusLabel.setText(message));
    }
    
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}
