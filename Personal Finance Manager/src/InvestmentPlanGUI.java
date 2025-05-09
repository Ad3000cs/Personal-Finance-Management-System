package src;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Dimension;


public class InvestmentPlanGUI {

    /* Class creates a Swing GUI to show options to the user for selecting the number of years the user
     * wants to stay invested for (2 years, 5 years, 10 years, 15 years and custom). For custom years,
     * it opens a dialog box for the user to enter preferred number of years.
     * 
     * It inherits the InvestmentPlan class for the data to be shown.
     * 
     * Arguments:
     * N/A
     * 
     * Raises:
     * NumberFormatException: If the user enters an absurd value for the time it wants to invest for.
     * 
     * Returns:
     * Shows the swing GUI for selecting number of years user wants to invest.
     */

    private InvestmentPlan plan;

    public InvestmentPlanGUI(InvestmentPlan plan){
        this.plan = plan;
        createAndShowGUI();
    }

    private void createAndShowGUI(){
        JFrame frame = new JFrame("Investment Planner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel instructionLabel = new JLabel("Select years you want to stay invested for: ");
        instructionLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(instructionLabel);
        panel.add(Box.createVerticalStrut(10));


        String[] options = {"2 years", "5 years", "10 years", "15 years", "Custom", "Exit"};

        for (String label : options){
            JButton button = new JButton(label);
            panel.add(button);

            button.addActionListener(e -> {
                if (label.equals("Exit")) {
                    System.exit(0);
                }else{
                    int years = switch (label){
                        case "2 years" -> 2;
                        case "5 years" -> 5;
                        case "10 years" -> 10;
                        case "15 years" -> 15;
                        case "Custom" -> getCustomYears();
                        default -> 0;
                    };
                    if (years > 0){
                        String result = plan.calculateFV(years);

                        JTextArea textArea = new JTextArea(result);
                        textArea.setEditable(false);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setPreferredSize(new Dimension(400, 300));

                        JOptionPane.showMessageDialog(frame, scrollPane, "Investment Breakdown", JOptionPane.INFORMATION_MESSAGE);

                    }
                }
            });

        }
        frame.add(panel);
        frame.setVisible(true);
    }

    private int getCustomYears(){
        String input = JOptionPane.showInputDialog("Enter the number of years you want to invest for: ");
        try{
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Number.");
            return 0;
        }
    }
}
