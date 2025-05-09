package src;

public class StepUpSIPCalculator {

    /* 
     * The class calculates future value of SIP (Systematic Investment Plan) according to formula (Given and explained in word document)
     * 
     * Arguments:
     * initialSIP = The value with which user starts the SIP.
     * annualStepUp = The value (in %) with which the SIP amount will increase after 12 months
     * annualRate = The approximate return rate of SIP.
     * years = Number of years user will do monthly SIP for.
     * stepUpFrequencyInMonths = The frequency with which the SIP amount will increase in months usually 12 months. (say, we do a
     * SIP for 1000 EUR a month for the first year and increase it by 200EUR the next year making the amount 1200EUR for the second year.)
     * 
     * Returns:
     * futureValue = The compounded wealth (Future Value) of the SIP.
     * 
     * Raises:
     * N/A
     */

    private double initialSIP;
    private double annualStepUp;
    private double annualRate;
    private int years;
    private int stepUpFrequencyInMonths;

    public StepUpSIPCalculator(double initalSIP, double annualStepUp, double annualRate, int years, int stepUpFrequencyInMonths){
        this.initialSIP = initalSIP;
        this.annualStepUp = annualStepUp;
        this.annualRate = annualRate;
        this.years = years;
        this.stepUpFrequencyInMonths = stepUpFrequencyInMonths;
    }

    public double Calculate(){
        double MonthlyRate = annualRate/12/100;
        int totalMonths = years * 12;
        int steps = totalMonths/stepUpFrequencyInMonths;
        double futureValue = 0;

        for (int k = 0; k < steps; k++){
            double currentSIP = initialSIP + k * annualStepUp;
            int monthsRemaining = totalMonths - k * stepUpFrequencyInMonths;

            double futureValue_current_batch = currentSIP * ((Math.pow(1+MonthlyRate, monthsRemaining)-1)/MonthlyRate) * (1+MonthlyRate);
            futureValue += futureValue_current_batch;
        }

        return futureValue;
    }
}
