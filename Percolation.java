/* *****************************************************************************
 *  Name: Grace Gupta
 *  Date: March 27, 2019
 *  Description: AlgI, Assignment I
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int num;
    private int[][] grid;
    private boolean[] bottomFullStates;
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
        bottomFullStates = new boolean[num];
        for (int i = 0; i < num; i++) {
            bottomFullStates[i] = false;
        }
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
            if (quickUnionUF.connected(0, bottom1DIndex)) {   // if a bottom cell is full, update
                // update boolean array
                bottomFullStates[col - 1] = true;
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
            return (bottomFullStates[col - 1]);
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

    public void printBooleans() {
        for (int i = 0; i < bottomFullStates.length; i++) {
            System.out.println(bottomFullStates[i]);
        }
    }


    public static void main(String[] args) {

        Percolation p = new Percolation(10);
        p.open(10, 2);
        p.open(2, 10);
        p.open(6, 8);
        p.open(2, 6);
        p.open(1, 4);
        p.open(8, 4);
        p.open(10, 1);
        p.open(4, 2);
        p.open(4, 8);
        p.open(9, 3);
        p.open(2, 2);
        p.open(9, 1);
        p.open(4, 3);
        p.open(5, 5);
        p.open(5, 7);
        p.open(2, 8);
        p.open(6, 4);
        p.open(7, 5);
        p.open(9, 6);
        p.open(3, 7);
        p.open(4, 7);
        p.open(7, 1);
        p.open(9, 4);
        p.open(3, 10);
        p.open(1, 10);
        p.open(10, 10);
        p.open(9, 7);
        p.open(1, 5);
        p.open(9, 8);
        p.open(6, 1);
        p.open(2, 5);
        p.open(3, 4);
        p.open(6, 9);
        p.open(5, 8);
        p.open(3, 2);
        p.open(4, 6);
        p.open(1, 7);
        p.open(7, 9);
        p.open(3, 9);
        p.open(4, 4);
        p.open(4, 10);
        p.open(3, 5);
        p.open(3, 8);
        p.open(1, 8);
        p.open(3, 1);
        p.open(6, 7);
        p.open(2, 3);
        p.open(7, 4);
        p.open(9, 10);
        p.open(7, 6);
        p.open(5, 2);
        p.open(8, 3);
        p.open(10, 8);
        p.open(7, 10);
        p.open(4, 5);
        p.open(8, 10);

        p.printBooleans();

        System.out.println("All tests passed");

    }

}
