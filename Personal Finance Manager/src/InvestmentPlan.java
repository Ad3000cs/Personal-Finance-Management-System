package src;
import java.sql.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class InvestmentPlan {

    /*
     * Class sets all parameters including allocation of funds, return rates given by user using UserID to respective
     * instances of classes and computes a basic InvestmentPlan for the user spanning across small-cap, large-cap, mid-cap and fixed deposit (FD) (emergency fund).
     * 
     * Arguments:
     * amountToInvest = the remaining amount to invest after cutting all expenses.
     * currentStrategy = AllocationStrategy instance for allocation of funds to different investing options.
     * 
     * Returns:
     * result = A formatted string giving a summary of the plan.
     * 
     * Raises:
     * N/A
     */

    private double amountToInvest;
    private AllocationStrategy currentStrategy;    
    
    public InvestmentPlan (double amountToInvest){
        this.amountToInvest = amountToInvest;
    }

    public void setStrategy(int years){
        this.currentStrategy = Allocation.getStrategy(years);
        currentStrategy.display(amountToInvest);
    }

    public String calculateFV(int years) {
        String Step_up_input = JOptionPane.showInputDialog("Enter the annual step up in percentage, usually equal to salary hike since expenses remain same (ideal): ");
        int Step_up_percentage = Integer.parseInt(Step_up_input);
        Double Step_up = Double.valueOf(Step_up_percentage) / 100;
        setStrategy(years);

        double small_cap_returns = 0.18;
        double mid_cap_returns = 0.15;
        double large_cap_returns = 0.12;
        double fd_returns = 0.06;

        double small_cap = amountToInvest * currentStrategy.smallCapAlloc;
        double mid_cap = amountToInvest * currentStrategy.midCapAlloc;
        double large_cap = amountToInvest * currentStrategy.largeCapAlloc;
        double fd = amountToInvest * currentStrategy.fdAlloc;

        double fvsmall = new StepUpSIPCalculator(small_cap, Step_up, small_cap_returns, years,12).Calculate();
        double fvmid = new StepUpSIPCalculator(mid_cap, Step_up, mid_cap_returns, years,12).Calculate();
        double fvlarge = new StepUpSIPCalculator(large_cap, Step_up, large_cap_returns, years,12).Calculate();
        double fvFD = new StepUpSIPCalculator(fd, Step_up, fd_returns, years,12).Calculate();
        
        Double totalFV = fvsmall + fvmid + fvlarge + fvFD;

        StringBuilder result = new StringBuilder();
        result.append("Allocation Strategy: \n");
        result.append("Investment Duration: "+years+" years\n");
        result.append("Each investment option to be increased by " + Step_up_input+"%" + " Every 12 months\n");
        result.append(currentStrategy.display(amountToInvest));
        result.append("\n Estimated Future Value: \n");
        result.append(String.format("Small Cap FV: EUR%.2f\n", fvsmall));
        result.append(String.format("Mid Cap FV: EUR%.2f\n", fvmid));
        result.append(String.format("Large Cap FV: EUR%.2f\n", fvlarge));
        result.append(String.format("FD Emergency FV: EUR%.2f\n", fvFD));
        result.append("\nTotal Future Value: EUR"+ String.format("%.2f", totalFV));

        return result.toString();
        

    }    
        
public static InvestmentPlan fromUserID(int userID){
    final double[] amount = new double[1];

    DatabaseUtils.executeQuery(
        "SELECT Amount_to_invest FROM Current WHERE ID = ?",
        rs -> {
            if (rs.next()){
                amount[0] = rs.getDouble("Amount_to_invest");
            } else {
                throw new SQLException("No user found with ID: "+userID);
            }
        },
        userID
    );

    return new InvestmentPlan(amount[0]);
}

}