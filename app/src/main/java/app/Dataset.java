package app;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Dataset {
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void generateDataset() {
        try {
            List<Employee> employees = generateLargeDataset();
            String json = gson.toJson(employees);
            
            // Save to dataset.json in resources
            String resourcePath = Dataset.class.getResource("/dataset.json").getPath();
            try (FileWriter writer = new FileWriter(resourcePath)) {
                writer.write(json);
            }
            
            System.out.println("Dataset generated successfully with " + employees.size() + " employees");
            System.out.println("File size: " + formatFileSize(json.length()));
            
        } catch (Exception e) {
            System.err.println("Error generating dataset: " + e.getMessage());
        }
    }
    
    private static List<Employee> generateLargeDataset() {
        List<Employee> employees = new ArrayList<>();
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
        
        // Generate enough employees to create a 5MB+ file
        int targetEmployees = 45000;
        
        for (int i = 0; i < targetEmployees; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String name = firstName + " " + lastName;
            
            int age = 22 + random.nextInt(58); // Age between 22-80
            String department = departments[random.nextInt(departments.length)];
            double salary = 30000 + random.nextDouble() * 120000; // Salary between 30k-150k
            
            employees.add(new Employee(name, age, department, salary));
        }
        
        return employees;
    }
    
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}
