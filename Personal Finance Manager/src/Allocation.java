package src;
public class Allocation {
    /*
     * Class gets allocation from AllocationStrategy with respect to number of years the user wants to invest for and
     * the average return each fund gets. Usually, as per market trends, small-cap funds give 18%, mid-cap funds give 15% and
     * large-cap funds give 12% and an FD (Fixed Deposit) gives 6% annual returns respectively.
     * 
     * Arguments:
     * years: Number of years 
     * 
     * Returns:
     * Allocation Strategy based on the mentioned parameters.
     * 
     * Raises:
     * N/A
     */
    public static AllocationStrategy getStrategy(int years) {
        if (years <=2){
            return new AllocationStrategy(0.0, 0.10,0.30,0.60);
        }else if (years <= 5){
            return new AllocationStrategy(0.10, 0.25, 0.35, 0.30);
        }else if (years <= 10){
            return new AllocationStrategy(0.20, 0.30, 0.35, 0.15);
        }else {
            return new AllocationStrategy(0.25, 0.35, 0.30, 0.10);
        }
    }
}
