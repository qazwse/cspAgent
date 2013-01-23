/*
  Curtis Rasmussen
  2012/10/13

  Node.java
*/

import java.util.ArrayList;

public class Node
{
    public int index;
    public String label;
    
    public int numStates;
    public ArrayList<String> states;
    
    public int numNeighs;
    public ArrayList<Integer> neighs;
    
    public int numConstrs;
    public ArrayList<Constraint> constrs;
    
    public Node() {
        this.states = new ArrayList<String>();
        this.neighs = new ArrayList<Integer>();
        this.constrs = new ArrayList<Constraint>();
    }

    public void print() {
        System.out.println("Node " + index);
        System.out.println("Label " + label);

        System.out.println("NumStates " + numStates);
        for ( String s : states ) {
            System.out.println("--" + s);
        }
        System.out.println("NumNeighs " + numNeighs);
        for ( Integer s : neighs ) {
            System.out.println("--" + s);
        }
        // System.out.println("NumConstraints " + numConstrs);
        // for ( Constraint s : constrs ) {
        //     System.out.print(s.constrsFor + " " + s.constrs.size() );
        //     System.out.print(s);
        // }
    }
}
