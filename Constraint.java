/*
  Curtis Rasmussen
  2012/10/13

  Constraint.java
*/

import java.util.ArrayList;

public class Constraint {
    public int neighbour;
    public ArrayList<int[]> constrs;

    public Constraint() {
        this.constrs = new ArrayList<int[]>();
    }

    @Override public String toString() {
        String ret = "\n";
        for ( int[] i : constrs )
            ret += i[0] + " " + i[1] + " ; ";
        ret += "\n";
        return ret;
    }
}