import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * The {@code Percolation } class models a percolation system using and n-by-n grid of sites.
 * Each site is open or blocked. A full site is one that is connected to the top row via a chain
 * of neighboring open sites. The system percolates if there is a full site in the bottom row.
 * Connectivity between sites is tracked using two WeightedQuickUnionUF objects. The WeightedQuickUnionUF
 * represents a union-find data type that determines whether two sites are in the same component.
 * One WeightedQuickUnionUF object contains both virtual top and virtual bottom site to determine
 * percolation. Another contains only a virtual top site to determine whether individual sites on
 * the bottom row are full.
 */

public class Percolation {

    private boolean[][] grid;
    private final int num;                                  // grid dimension (num*num)
    private int bottom1DIndex;                              // index of bottom site
    private int numOpenSites;                               // number of open sites
    private final WeightedQuickUnionUF quickUnionUF;        // contains top and bottom sites
    private final WeightedQuickUnionUF freeBottomSitesUF;   // contains top site only

    /**
     * Initializes a Percolation grid with {@code n} rows and columns, {@code 0} through
     * {@code n-1}. Each site is originally closed.
     *
     * @param n the dimension of the grid
     * @throws IllegalArgumentException if {@code n <= 0}
     */
    public Percolation(int n) {

        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        num = n;
        numOpenSites = 0;
        quickUnionUF = new WeightedQuickUnionUF((num * num) + 2);
        freeBottomSitesUF = new WeightedQuickUnionUF((num * num) + 1);
        bottom1DIndex = (num * num) + 1;
        grid = new boolean[num][num];
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                grid[i][j] = false;
            }
        }
    }

    /**
     * Opens the site at grid[row, col] and connects it to any neighboring sites
     * that are also open. Updates the number of open sites.
     *
     * @throws IllegalArgumentException unless both {@code  num < r <= 0} and {@code num < c <= 0}
     */
    public void open(int row, int col) {

        validateIndices(row, col);
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            numOpenSites++;

            int index1D = xyto1D(row, col);

            if (isValidAndOpenNeighbor(row, col - 1)) {
                quickUnionUF.union(xyto1D(row, col - 1), index1D);  // left
                freeBottomSitesUF.union(xyto1D(row, col - 1), index1D);
            }
            if (isValidAndOpenNeighbor(row, col + 1)) {
                quickUnionUF.union(xyto1D(row, col + 1), index1D);  // right
                freeBottomSitesUF.union(xyto1D(row, col + 1), index1D);
            }
            if (isValidAndOpenNeighbor(row - 1, col)) {
                quickUnionUF.union(xyto1D(row - 1, col), index1D);  // up
                freeBottomSitesUF.union(xyto1D(row - 1, col), index1D);
            }
            if (isValidAndOpenNeighbor(row + 1, col)) {
                quickUnionUF.union(xyto1D(row + 1, col), index1D);  // down
                freeBottomSitesUF.union(xyto1D(row + 1, col), index1D);
            }

            if (row == 1) {
                // if top row, connect to virtual top site
                quickUnionUF.union(index1D, 0);
                freeBottomSitesUF.union(index1D, 0);
            }
            if (row == num) {
                // if bottom, connect to virtual bottom site
                quickUnionUF.union(index1D, bottom1DIndex);
            }
        }
    }

    /**
     * Returns true if grid[row, col] is open.
     *
     * @throws IllegalArgumentException unless both {@code  num < r <= 0} and {@code num < c <= 0}
     */
    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return (grid[row - 1][col - 1]);
    }

    /**
     * Returns true if the site grid[row, col] is full.
     *
     * @throws IllegalArgumentException unless both {@code  num < r <= 0} and {@code num < c <= 0}
     */
    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        return isOpen(row, col) && freeBottomSitesUF.connected(0, xyto1D(row, col));
    }

    /**
     * Returns the number of open sites.
     *
     * @return numOpenSites
     */
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    /**
     * Returns true if the system percolates.
     *
     * @return {@code true} if the top and bottom sites are in the same component
     * within the quickUnionUF object; {@code false} otherwise
     */
    public boolean percolates() {
        return (quickUnionUF.connected(0, bottom1DIndex));
    }

    /**
     * Returns true if grid[r, c] is both valid and open.
     *
     * @throws IllegalArgumentException unless both {@code  num < r <= 0} and {@code num < c <= 0}
     */
    private boolean isValidAndOpenNeighbor(int r, int c) {
        return (isValidIndices(r, c) && (grid[r - 1][c - 1]));
    }

    /**
     * Returns the one-dimensional index corresponding to grid[x,y] in a quickUnionUF object.
     *
     * @return
     */
    private int xyto1D(int x, int y) {
        int index = (num * (x - 1)) + y;
        return index;
    }

    /**
     * Validate that grid[r, c] is a valid index.
     *
     * @throws IllegalArgumentException unless both {@code  num < r <= 0} and {@code num < c <= 0}
     */
    private void validateIndices(int r, int c) {
        if ((r <= 0 || r > num) || (c <= 0 || c > num)) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    private boolean isValidIndices(int r, int c) {
        if ((r <= 0 || r > num) || (c <= 0 || c > num)) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

        // Percolation p = new Percolation(10);


        // System.out.println("All tests passed");

    }

}
