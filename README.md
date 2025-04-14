# Usage Translator Application

This is my solution to a coding exercise for an email and web hosting provider. The purpose of the application is to process usage data from a CSV file and a mapping from a JSON file, and then generate SQL INSERT statements for two normalized tables: **chargeable** and **domains**.

## Overview

The application reads usage records from a CSV file and uses a JSON file to map part numbers to product values. It then applies basic business rules—such as skipping certain records, cleaning account GUIDs, and adjusting usage values based on unit reduction factors—and finally produces SQL INSERT statements. The output is logged to the console.

## Features

- **CSV Processing:** Uses Apache Commons CSV to handle CSV parsing.
- **JSON Mapping:** Leverages Jackson to load a mapping of part numbers to product values.
- **Data Validation:** Filters out records with missing part numbers, non-positive usage counts, or partner IDs in a predefined skip list.
- **Data Transformation:** Cleans account GUIDs and applies unit reduction rules based on the part number.
- **SQL Generation:** Generates SQL INSERT statements for both the `chargeable` and `domains` tables, with basic measures to guard against SQL injection.
- **External Configuration:** Uses external configuration (via `application.properties`) for values like the partner ID skip list and unit reduction factors, making it easy to adjust settings without changing the code.

## Prerequisites

- **Java:** JDK 17 (or later)
- **Build Tool:** Maven 3.x
- **Git:** For version control and GitHub usage
- **IDE/Text Editor:** Your choice (I used IntelliJ IDEA)

## Getting Started

1. **Clone or Download the Repository:**

   ```bash
   git clone https://github.com/JayeshYeola/usage-translator.git
   cd usage-translator
   ```
2. **Build the Project:**

    Run the following command in the terminal from the project directory:
    ```bash
    mvn clean install
    ```
    This will compile the project and run all tests. Make sure everything passes before you proceed.

3. **Run the Application:**
  
    The application is set up as a batch process using a CommandLineRunner. To run it, use:
    ```bash
    mvn spring-boot:run
    ```
    The application reads the data files from the `src/main/resources/data` directory (ensure `Sample_Report.csv` and `typemap.json` are in that folder) and prints the generated SQL to the console.

4. **Test the Application:**

    To run the unit tests:
    ```bash
    mvn test
    ```
## Configuration

The application uses external configuration to control:

    Partner ID Skip List: A list of partner IDs to skip processing.

    Unit Reduction Factors: Mappings that determine how to adjust the usage value based on the part number.

These settings can be found (and adjusted) in the `application.properties` file located in the `src/main/resources/` directory.

## Future Enhancements
- Enhanced Error Handling: Implement more robust logging or error reporting, potentially via custom exceptions.
- Parameterized SQL Queries: For production, consider using parameterized queries with Spring Data JPA or JDBC to eliminate SQL injection risks entirely.
- Improved Testing: Add more unit and integration tests, including tests for the CSV and JSON utilities with larger data sets.

Thanks for reviewing my solution!
