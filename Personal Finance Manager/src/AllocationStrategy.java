package src;

public class AllocationStrategy {

    /*
     * Class sets allocation of funds based on total amount invested and the number of years it is invested for.
     * The allocation is based on a very basic plan followed by investors.
     * 
     * Arguments:
     * N/A
     * 
     * Returns:
     * It returns formatted strings with the allocation.
     * 
     * Raises:
     * N/A
     */

    public double smallCapAlloc;
    public double midCapAlloc;
    public double largeCapAlloc;
    public double fdAlloc;

    public AllocationStrategy(double small, double mid, double large, double fd) {
        this.smallCapAlloc = small;
        this.midCapAlloc = mid;
        this.largeCapAlloc = large;
        this.fdAlloc = fd;
    }

    public String display(double totalAmount) {
        return

        "Small Cap Allocation: EUR" + String.format("%.2f", totalAmount * smallCapAlloc) + "\n" +
        "Mid Cap Allocation: EUR" + String.format("%.2f", totalAmount * midCapAlloc) + "\n" +
        "Large Cap Allocation: EUR" + String.format("%.2f", totalAmount * largeCapAlloc) + "\n" +
        "FD Emergency Allocation: EUR" + String.format("%.2f", totalAmount * fdAlloc);
        
    }
}
