# Dataset Processing System

A JavaFX application that demonstrates advanced Java features including Function interfaces, Streams API, and functional programming concepts for processing large employee datasets.

## Features

### Core Functionality
- **Generate Random Datasets**: Create large datasets (5MB+) with 45,000+ employee records
- **Upload Custom Datasets**: Import your own JSON datasets with validation
- **Advanced Data Processing**: Demonstrate Function interfaces and Stream operations
- **Real-time Processing**: Asynchronous operations with progress updates

### Technical Demonstrations
- **Function Interface**: Single abstract method `R apply(T t)` implementation
- **Streams API**: Functional operations on collections with lazy evaluation
- **Data Analysis**: Salary statistics, department grouping, age filtering
- **Performance Optimization**: Single-pass operations and efficient data structures

## Prerequisites

- **Java**: JDK 24 or later
- **Gradle**: 9.0.0 or later (included with wrapper)
- **JavaFX**: 24.0.2 (automatically managed by Gradle)

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd dataset-processing-system
```

### 2. Build the Project
```bash
./gradlew build
```

### 3. Run the Application
```bash
./gradlew :app:run
```

## Usage Guide

### Starting the Application
The application launches with a clean interface showing three main buttons:

1. **Generate Random Dataset**
2. **Upload Custom Dataset** 
3. **Process Dataset**

### Generating Random Datasets

Click **"Generate Random Dataset"** to create a large dataset with:
- **45,000 employee records**
- **5MB+ file size**
- **Random data** including:
  - Names (30 first names × 30 last names combinations)
  - Ages (22-80 years)
  - Departments (Engineering, Marketing, HR, Finance, Sales, Operations, IT, Legal)
  - Salaries ($30,000-$150,000)

The generated dataset is automatically saved to `app/src/main/resources/dataset.json`.

### Uploading Custom Datasets

Click **"Upload Custom Dataset"** to import your own JSON file:

#### Required JSON Structure
```json
[
  {
    "name": "Employee Name",
    "age": 25,
    "department": "Department Name", 
    "salary": 50000.0
  }
]
```

#### Validation Rules
- **Name**: Required, non-empty string
- **Age**: Integer between 1-120
- **Department**: Required string
- **Salary**: Non-negative number

#### Error Handling
The application provides helpful error messages and shows the expected JSON structure if validation fails.

### Processing Datasets

Click **"Process Dataset"** to run comprehensive analysis:

#### Analysis Features
1. **Dataset Statistics**: Record count and basic information
2. **Function Interface Demo**: Name-department concatenation
3. **Stream Operations**: 
   - Average salary calculation
   - Age-based filtering (employees > 30)
   - Department grouping
   - Highest paid employee by department
4. **Performance Metrics**: Optimized single-pass operations

## Project Structure

```
dataset-processing-system/
├── app/
│   ├── build.gradle                 # Gradle configuration with dependencies
│   └── src/main/
│       ├── java/app/
│       │   ├── App.java             # Main JavaFX application class
│       │   ├── AppController.java   # FXML controller with business logic
│       │   ├── Employee.java        # Employee data model
│       │   └── Dataset.java         # Dataset generation utilities
│       └── resources/
│           ├── index.fxml           # JavaFX UI layout
│           └── dataset.json         # Generated/uploaded datasets
├── gradle/
├── gradlew                          # Gradle wrapper
└── README.md                        # This documentation
```

## Technical Implementation

### Key Classes

#### `App.java`
- Main JavaFX application entry point
- Loads FXML layout and initializes the UI

#### `AppController.java`
- Handles all user interactions and business logic
- Implements async operations using `CompletableFuture`
- Manages dataset generation, validation, and processing
- Optimized for performance with single-pass operations

#### `Employee.java`
- Data model representing employee information
- Immutable design with getters/setters
- Proper `equals()`, `hashCode()`, and `toString()` implementations

#### `Dataset.java`
- Utility class for dataset generation
- Implements large dataset creation algorithms
- File I/O operations for JSON persistence

### Performance Optimizations

#### Dataset Generation
- **Pre-allocated capacity**: `ArrayList.ensureCapacity(45000)`
- **Efficient random generation**: Reusable arrays for names/departments
- **Optimized loops**: Minimal object creation

#### Data Processing
- **Single-pass operations**: `DoubleSummaryStatistics` for salary analysis
- **Stream optimization**: Combined operations to reduce iterations
- **Memory efficiency**: Minimal intermediate collections

#### UI Performance
- **Async operations**: Non-blocking UI with `CompletableFuture`
- **Batch updates**: Single UI update per operation
- **Progress feedback**: Real-time status updates

### Dependencies

```gradle
dependencies {
    implementation libs.guava                    // Google Guava utilities
    implementation 'com.google.code.gson:gson:2.10.1'  // JSON processing
}

javafx {
    version = '24.0.2'
    modules = [ 'javafx.controls', 'javafx.fxml' ]
}
```

## Data Analysis Examples

### Function Interface Usage
```java
Function<Employee, String> nameDepartmentFunction = employee -> 
    employee.getName() + " - " + employee.getDepartment();
```

### Stream Operations
```java
// Average salary calculation
double averageSalary = employees.stream()
    .mapToDouble(Employee::getSalary)
    .average()
    .orElse(0.0);

// Department grouping
Map<String, List<Employee>> byDepartment = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));

// Age filtering
List<Employee> over30 = employees.stream()
    .filter(emp -> emp.getAge() > 30)
    .collect(Collectors.toList());
```

## Troubleshooting

### Common Issues

#### Application Won't Start
- Ensure Java 24+ is installed: `java -version`
- Check Gradle wrapper: `./gradlew --version`
- Verify JavaFX modules are available

#### Dataset Generation Fails
- Check available disk space (requires 5MB+)
- Verify write permissions in resources directory
- Monitor memory usage for large datasets

#### Upload Validation Errors
- Ensure JSON structure matches required format
- Check for valid data types (numbers vs strings)
- Verify all required fields are present

#### Performance Issues
- Large datasets may take time to process
- Monitor system resources during operations
- Consider reducing dataset size for testing

### Error Messages

| Error | Cause | Solution |
|-------|-------|----------|
| "Location is not set" | FXML file not found | Check file paths and build |
| "ClassNotFoundException" | Missing dependencies | Run `./gradlew build` |
| "Invalid JSON structure" | Wrong format | Follow JSON schema above |
| "Out of memory" | Dataset too large | Reduce dataset size |


## Learning Objectives

This project demonstrates:

- **JavaFX Application Development**: Modern UI with FXML
- **Functional Programming**: Function interfaces and lambda expressions
- **Streams API**: Collection processing and data transformation
- **Asynchronous Programming**: Non-blocking operations with CompletableFuture
- **Performance Optimization**: Efficient algorithms and data structures
- **JSON Processing**: Data serialization/deserialization
- **Error Handling**: Comprehensive validation and user feedback
- **Software Architecture**: Separation of concerns and modular design

