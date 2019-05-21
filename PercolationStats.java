import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * The PercolationStats class estimates the percolation threshold of an n-by-n grid by performing
 * independent experimental trials. In each trial, all sites are initialized to be blocked.
 * A site is chosen uniformly at random to be opened until the system percolates. The fraction of open sites
 * when the system percolates provides an estimate for the percolation threshold of the system.
 * The sample mean and standard deviation of the percolation threshold for a specified number of trials
 * is reported along with the 95% confidence interval.
 */

public class PercolationStats {

    private final int size;                 // dimensions of grid (size*size)
    private final int t;                    // number of trials
    private double mean;                    // sample mean of percolation threshold
    private double stddev;                  // sample standard deviation of percolation threshold
    private double confLo;                  // low endpoint of 95% confidence interval
    private double confHi;                  // high endpoint of 95% confidence interval
    private final double[] numsToPercolate; // numsToPercolate[i] = fraction of open sites at time of percolation

    /**
     * Initializes a PercolationStats object that estimates the percolation threshold of a
     * {@code n} by {@code n} grid over {@code trials} specified number of trials.
     *
     * @param n      the dimensions of the Percolation object grid
     * @param trials the number of trials to estimate the percolation threshold
     */
    public PercolationStats(int n, int trials) {

        if ((n <= 0) || (trials <= 0)) {
            throw new java.lang.IllegalArgumentException();
        }

        size = n;
        t = trials;
        numsToPercolate = new double[t];

        for (int run = 0; run < t; run++) {
            Percolation perc = new Percolation(size);
            while (!perc.percolates()) {
                boolean siteIsOpen = true;
                while (siteIsOpen) {
                    int r = StdRandom.uniform(1, size + 1);
                    int c = StdRandom.uniform(1, size + 1);
                    if (!perc.isOpen(r, c)) {
                        siteIsOpen = false;
                        perc.open(r, c);
                    }
                }
            }
            numsToPercolate[run] = (double) perc.numberOfOpenSites();
        }
        convertToFractionOfSites();
        mean = StdStats.mean(numsToPercolate);
        stddev = StdStats.stddev(numsToPercolate);
        confLo = (mean - ((1.96 * stddev)) / (Math.sqrt(t)));
        confHi = (mean + ((1.96 * stddev)) / (Math.sqrt(t)));

    }

    /**
     * Returns the sample mean of the percolation threshold.
     *
     * @return the mean
     */
    public double mean() {
        return mean;
    }

    /**
     * Returns the sample standard deviation of the percolation threshold.
     *
     * @return the standard deviation
     */
    public double stddev() {
        return stddev;
    }

    /**
     * Returns the low endpoint of the 95% confidence interval.
     *
     * @return the low endpoint of the confidence interval
     */
    public double confidenceLo() {
        return confLo;
    }

    /**
     * Returns the high endpoint of the 95% confidence interval.
     *
     * @return the high endpoint of the confidence interval
     */
    public double confidenceHi() {
        return confHi;
    }

    /**
     * Estimates the percolation threshold by calculating the fraction of open sites when the
     * system percolates.
     */
    private void convertToFractionOfSites() {
        for (int i = 0; i < t; i++) {
            double openSites = numsToPercolate[i];
            double totalSites = (double) (size * size);
            numsToPercolate[i] = openSites / totalSites;
        }
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trials);

        String m = "mean";
        String s = "stddev";
        String c = "95% confidence interval";
        double mean = ps.mean();
        double stddev = ps.stddev();
        double confLo = ps.confidenceLo();
        double confHi = ps.confidenceHi();

        System.out.format("%-24s", m);
        System.out.print("= ");
        System.out.format("%.16f%n", mean);

        System.out.format("%-24s", s);
        System.out.print("= ");
        System.out.format("%.16f%n", stddev);

        System.out.format("%-24s", c);
        System.out.print("= " + "[");
        System.out.format("%.16f", confLo);
        System.out.print(", ");
        System.out.printf("%.16f", confHi);
        System.out.print("]\n");

    }
}
