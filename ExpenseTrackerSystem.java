
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


class Expense {
    private String description;
    private double amount;
    private Date dateTime;

    public Expense(String description, double amount, Date dateTime) {
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDateTime() {
        return dateTime;
    }
}

class ExpenseTracker {
    private List<Expense> expenses;

    public ExpenseTracker() {
        expenses = new ArrayList<>();
    }

    public void addExpense(Scanner scanner) {
        System.out.print("Enter expense description: ");
        String description = scanner.nextLine();
        System.out.print("Enter expense amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        Date dateTime = new Date(); // Capture the current date and time

        Expense expense = new Expense(description, amount, dateTime);
        expenses.add(expense);
   }

    public void displayExpenses(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            int character;
            while ((character = reader.read()) != -1) {
                System.out.print((char) character);
            }
            System.out.println("\nExpenses displayed successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }

    public double calculateTotalExpenses(String filename) throws NumberFormatException {
        double total = 0;
        try (FileReader reader = new FileReader(filename)) {
            Scanner scanner = new Scanner(reader);
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(","); // Split the line using the tab delimiter
                if (parts.length != 3) { // Check if there are three columns
                    throw new IllegalArgumentException("Invalid CSV file format. Each line should have three columns.");
                
                }
                double amount = Double.parseDouble(parts[1]);
                total += amount;
            }
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        return total;
    }
    
    

    public void saveExpensesToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename, true)) { // Append mode
            File file = new File(filename);
            if (!file.exists() || file.length() == 0) {
                writer.write("Description,Amount\n"); // Write header only if the file is empty
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Expense expense : expenses) {
                writer.write(expense.getDescription() + "," + expense.getAmount() + "," + dateFormat.format(expense.getDateTime()) + "\n");
            }
            System.out.println("Expenses saved to " + filename + " successfully.");
            writer.flush(); // Flush the writer to ensure the data is written to the file
            expenses.clear(); // Clear the expenses list after saving
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
}
public class ExpenseTrackerSystem {
    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Expense Tracker System");
            System.out.println("1. Add Expense");
            System.out.println("2. Display Expenses");
            System.out.println("3. Calculate Total Expenses");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character
            switch (choice) {
                case 1:
                    tracker.addExpense(scanner);
                    String filename = "expense.csv";
                    tracker.saveExpensesToFile(filename);
                    System.out.println("Expense added successfully.");
                    break;
                case 2:
                    String filename1 = "expense.csv";
                    tracker.displayExpenses(filename1);
                    break;
                case 3:
                    String filename2 = "expense.csv";
                    double totalExpenses = tracker.calculateTotalExpenses(filename2);
                    System.out.println("Total expenses: $" + totalExpenses);
                    break;
                case 4:
                    System.out.println("Exiting the program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            System.out.println();
        }
    }
}