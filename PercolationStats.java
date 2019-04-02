/* *****************************************************************************
 *  Name: Grace Gupta
 *  Date: March 29, 2019
 *  Description: AlgI, Assignment I
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int size;
    private final int t;
    private double mean;
    private double stddev;
    private double confLo;
    private double confHi;
    private final double[] numsToPercolate;
    // private double[] fractionsToPercolate;

    public PercolationStats(int n, int trials) {

        if ((n <= 0) || (trials <= 0)) {
            throw new java.lang.IllegalArgumentException();
        }

        size = n;
        t = trials;
        numsToPercolate = new double[t];
        // fractionsToPercolate = new double[t];

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
        // mean = StdStats.mean(fractionsToPercolate);
        // stddev = StdStats.stddev(fractionsToPercolate);
        confLo = (mean - ((1.96 * stddev)) / (Math.sqrt(t)));
        confHi = (mean + ((1.96 * stddev)) / (Math.sqrt(t)));

    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confLo;
    }

    public double confidenceHi() {
        return confHi;
    }

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
        System.out.format("%.16f\n", mean);

        System.out.format("%-24s", s);
        System.out.print("= ");
        System.out.format("%.16f\n", stddev);

        System.out.format("%-24s", c);
        System.out.print("= " + "[");
        System.out.format("%.16f", confLo);
        System.out.print(", ");
        System.out.printf("%.16f", confHi);
        System.out.print("]\n");

    }
}
