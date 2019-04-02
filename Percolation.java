/* *****************************************************************************
 *  Name: Grace Gupta
 *  Date: March 27, 2019
 *  Description: AlgI, Assignment I
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int num;
    private int[][] grid;
    private int bottom1DIndex;
    private final WeightedQuickUnionUF quickUnionUF;
    private int numOpenSites;


    public Percolation(int n) {

        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        num = n;
        numOpenSites = 0;
        quickUnionUF = new WeightedQuickUnionUF((num * num) + 2);
        bottom1DIndex = (num * num) + 1;
        grid = new int[num][num];
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                grid[i][j] = 0;
            }
        }

    }

    public void open(int row, int col) {

        validateIndices(row, col);
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = 1;
            numOpenSites++;

            int index1D = xyto1D(row, col);

            if (isValidAndOpenNeighbor(row, col - 1)) {
                quickUnionUF.union(xyto1D(row, col - 1), index1D);  // left
            }
            if (isValidAndOpenNeighbor(row, col + 1)) {
                quickUnionUF.union(xyto1D(row, col + 1), index1D);  // right
            }
            if (isValidAndOpenNeighbor(row - 1, col)) {
                quickUnionUF.union(xyto1D(row - 1, col), index1D);  // up
            }
            if (isValidAndOpenNeighbor(row + 1, col)) {
                quickUnionUF.union(xyto1D(row + 1, col), index1D);  // down
            }

            if (row == 1) {
                // if top row, connect to virtual top site
                quickUnionUF.union(index1D, 0);
            }
            if (row == num) {
                // if bottom, connect to virtual bottom site
                quickUnionUF.union(index1D, bottom1DIndex);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return (grid[row - 1][col - 1] == 1);
    }

    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        if (row < num) {
            return ((quickUnionUF.connected(0, xyto1D(row, col))));
        }
        else {
            return ((quickUnionUF.connected(0, xyto1D(row, col))) && (quickUnionUF
                    .connected(0, xyto1D(row - 1, col))));
        }
        // return ((quickUnionUF.connected(0, xyto1D(row, col))));
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        return (quickUnionUF.connected(0, bottom1DIndex));
    }

    private boolean isValidAndOpenNeighbor(int r, int c) {
        return (isValidIndices(r, c) && (grid[r - 1][c - 1] == 1));
    }

    private int xyto1D(int x, int y) {
        int index = (num * (x - 1)) + y;
        return index;
    }

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
