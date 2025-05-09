package src;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame{

    /*
     * Class takes input from user for the parameters: Name, Monthly Salary, Rent and Bills, Groceries, Miscellaneous expenses (in EUR).
     * 
     * It gives back a Swing interface with a summary of the user's expenses and remaining funds for investment. It also stores data into
     * a PostgreSQL database named Personal Finance Manager and a table named Current.
     * 
     * Arguments:
     * N/A
     * 
     * Raises:
     * Error if input is wrong/unreadable or database error.
     * 
     * Returns:
     * A Java Swing Interface with a summary of user's current expenses and remaining investable funds.
     */

    private JTextField nameField, Monthly_SalaryField, Rent_and_BillsField, GroceriesField, MISC_expenseField;
    private JTextArea outputArea;

    public Main(){

        /* Method takes input from the user using Swing's Text Fields. */

        setTitle("Personal Finance Manager");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6,2));

        inputPanel.add(new JLabel("Enter your Name: "));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Enter your Monthly Salary: "));
        Monthly_SalaryField = new JTextField();
        inputPanel.add(Monthly_SalaryField);

        inputPanel.add(new JLabel("Enter your Rent and Bills: "));
        Rent_and_BillsField = new JTextField();
        inputPanel.add(Rent_and_BillsField);

        inputPanel.add(new JLabel("Enter your Groceries: "));
        GroceriesField = new JTextField();
        inputPanel.add(GroceriesField);

        inputPanel.add(new JLabel("Enter your MISC expense: "));
        MISC_expenseField = new JTextField();
        inputPanel.add(MISC_expenseField);

        JButton submitBtn = new JButton("Submit");
        inputPanel.add(submitBtn);

        add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                handleSubmit();
            }
        });
    }

    private void handleSubmit(){

        /* Method calculates Total expenses, Total expenses percentage of salary, remaining salary, remaining percentage of salary.
         * Tells the user if it is maintaining a balance between expenses and saving for investment in a 50:50 ratio. If not, warns user
         * and shows ideal investment plan considering if user saves 50% of income for investment.
         * 
         * It takes all the data including the calculation results and puts it in Current table in PostgreSQL database Personal Finance Manager.
         */

        try {
            String name = nameField.getText();
            int Monthly_Salary = Integer.parseInt(Monthly_SalaryField.getText());
            int Rent_and_Bills = Integer.parseInt(Rent_and_BillsField.getText());            
            int Groceries = Integer.parseInt(GroceriesField.getText());            
            int MISC_expense = Integer.parseInt(MISC_expenseField.getText());            
          
            Integer Total_expense = Rent_and_Bills + Groceries + MISC_expense;
            Double Percentage_of_salary = (Double.valueOf(Total_expense)/Double.valueOf(Monthly_Salary)) * 100;
            Integer Remaining = Monthly_Salary - Total_expense;
            Double Remaining_percentage = (Double.valueOf(Remaining)/Double.valueOf(Monthly_Salary)) * 100;

            outputArea.setText("");

            outputArea.append("Summary: \n");

            outputArea.append("Monthly Salary: EUR"+Monthly_Salary + "\n");
            outputArea.append("Expenses: EUR"+Total_expense + " " + Percentage_of_salary +"%"+ "\n");
            outputArea.append("Remaining: EUR"+Remaining+ " "+ Remaining_percentage + "%"+ "\n");        

            outputArea.append("Ideal split between expenses and remaining should be 50% each. Your split: "+ "\n");

            Double Amount_to_invest = 0.0;
            Double Amount_to_invest_Percentage = 0.0;

            if (Remaining_percentage >= 50){
                Amount_to_invest = Double.valueOf(Remaining);
                Amount_to_invest_Percentage = Remaining_percentage;
                outputArea.append("Amount left to invest: "+Amount_to_invest+ "\n");
                outputArea.append("Percentage of Salary: "+Amount_to_invest_Percentage +"%"+ "\n");

                outputArea.append("Great! You are maintaining a good balance. You have an ideal surplus to invest."+ "\n");

            }else{
                Amount_to_invest = Double.valueOf(Remaining);
                Amount_to_invest_Percentage = (Amount_to_invest/Monthly_Salary)*100;
                outputArea.append("Total expenses: "+Total_expense+ "\n");
                outputArea.append("Total expenses percentage: "+Percentage_of_salary + "%"+ "\n");
                outputArea.append("""
                        I am giving a plan you can follow on your current income to expense ratio. Although,
                        ideally, you should have 50% of monthly salary to invest. Please check your expense. 
                        
                        """+ "\n");
            }

            DatabaseUtils.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Current (
                        ID SERIAL PRIMARY KEY,
                        Name VARCHAR(100),
                        Monthly_Salary integer,
                        Rent_and_Bills integer,
                        Groceries integer,
                        MISC_expense integer,
                        Total_expenses integer,
                        Expense_percent double precision,
                        Remaining integer,
                        Remaining_percent double precision,
                        Amount_to_invest integer
                    )
                    """);

            int userId = DatabaseUtils.insertAndReturnID("INSERT INTO Current (Name, Monthly_Salary, Rent_and_bills, Groceries, MISC_expense, Total_expenses, Expense_percent, Remaining, Remaining_percent, Amount_to_invest) VALUES (?,?,?,?,?,?,?,?,?,?) RETURNING ID"
            , name, Monthly_Salary, Rent_and_Bills, Groceries, MISC_expense, Total_expense, Percentage_of_salary, Remaining, Remaining_percentage, Amount_to_invest);

            outputArea.append("Your data has been saved. Your unique ID is: "+ userId);

            JButton viewplanbtn = new JButton("View Investment Plan");
            viewplanbtn.addActionListener(ev -> {
                InvestmentPlan plan = InvestmentPlan.fromUserID(userId);
                new InvestmentPlanGUI(plan);
            });

            outputArea.append("\n\n Click below to view your personalized investment plan: \n");
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(viewplanbtn);
            add(buttonPanel, BorderLayout.SOUTH);
            revalidate();
            

        }catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Invalid input or DB error: " + ex.getMessage());
        }

        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
        
}
}