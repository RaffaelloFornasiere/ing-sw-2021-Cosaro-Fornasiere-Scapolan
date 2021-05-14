package it.polimi.ingsw.ui.cli;

import java.io.PrintWriter;
import java.util.Scanner;

public class Panel {
    private PrintWriter out;
    private Scanner in;
    private int[] initXY;
    private int[] finXY;


    public Panel(int[] initXY, int[] finXY, Scanner in, PrintWriter out) {
        this.initXY= initXY;
        this.finXY= finXY;
        this.in = in;
        this.out = out;
    }
}
