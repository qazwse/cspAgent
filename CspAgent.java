/*
  Curtis Rasmussen
  2012/10/13

  CspAgent.java
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class CspAgent
{
    public int numNodes;
    public ArrayList<Node> nodes;
    public int[]  assigned;
    //public ArrayList<Node> nassigned;
    public int[] nassigned;
    
    public CspAgent( String cspFile ) {
        //Load cspFile
        nodes = new ArrayList<Node>();
        if ( loadCSP( cspFile ) ) {
            //printNodes();
            solveCSP();
        }
    }

    public void printNodes() {
        for ( Node n : nodes ) {
            n.print();
            System.out.println(" ");
            System.out.println(" ");
        }
    }

    public void solveCSP() {
        assigned = new int[numNodes];
        //nassigned = new ArrayList<Node>(numNodes);
        nassigned = new int[numNodes];

        // for ( Node n : nodes )
        //     nassigned.add(n);
        for ( int i = 0; i < numNodes; i++ ) {
            assigned[i]  = -1;
            nassigned[i] = 1;
        }
        if ( backtracking() ) {
            printState();
            System.out.println("Solution found!");
        }
        else
            System.out.println("No solution.");
    }

    public Boolean backtracking() {
        Boolean done = true;
        int test = 0;
        for ( int i = 0; i < numNodes; i++ ) {
            if ( assigned[i] == -1 ) {
                done = false;
            }
            else
                test++;
        }
        if ( done == true )
            return true;

        Node v = getVariable();
        System.out.println("Assigning node: " + v.label);

        for ( int i = 0; i < v.numStates; i++ ) {
            assigned[v.index] = i;
            System.out.println("--Assigned: " + v.states.get(i) );
            Boolean legal = checkLegality(v);
            System.out.println("----" + legal + " assignment.");
            if ( legal == true ) {
                System.out.println("\n");
                if ( backtracking() ) {
                    return true;
                }
                else
                    System.out.println("Assigning node: " + v.label);
            }
        }

        //Not the right spot to re-add nodes! Maybe
        assigned[v.index] = -1;
        retVariable(v);
        System.out.println("\nNo consistent solution at this branch, moving back...\n");
        return false;
    }

    public void printState() {
        for ( int i = 0; i < numNodes; i++ ) {
            if ( assigned[i] != -1 ) {
                System.out.println("Node " + nodes.get(i).label);
                System.out.println("--" + nodes.get(i).states.get(assigned[i]));
            }
        }
        System.out.println();
    }

    public Boolean checkLegality(Node v) {
        for ( Constraint c : v.constrs ) {
            //System.out.println("Checking node " + v.label + " against " + nodes.get(c.neighbour).label);
            int an = assigned[c.neighbour];
            int av = assigned[v.index];
            Boolean check = false;
            
            if ( an == -1 )
                break;
            for ( int[] c2 : c.constrs ) {
                if ( c2[0] == av && c2[1] == an ) { 
                    check = true;
                }
            }
            if ( check == false )
                return false;
        }
        return true;
    }

    public void retVariable(Node n) {
        nassigned[n.index] = 1;
    }

    public Node getVariable() {
        for ( int i = 0; i < numNodes; i++ ) {
            if ( nassigned[i] != -1 ) {
                Node n = nodes.get(i);
                nassigned[i] = -1;
                return n;
            }
        }
        return null;
    }

    private String[] readSplitLine( BufferedReader bf ) throws java.io.IOException {
        String line = bf.readLine();
        String[] parts = line.split(" ");
        return ( parts );
    }

    public Boolean loadCSP ( String cspFile ) {
        try {
            BufferedReader bf = new BufferedReader( new FileReader(cspFile) );
            String line = null;
            String[] parts = null;

            parts = readSplitLine(bf);
            numNodes = Integer.parseInt(parts[0]);

            for ( int nodesRead = 0; nodesRead < numNodes; nodesRead++ ) {
                Node n = new Node();
                n.index = nodesRead;

                //Empty line between each node
                line = bf.readLine();

                //Number of states line
                parts = readSplitLine(bf);
                n.numStates = Integer.parseInt(parts[0]);

                //Node label
                parts = readSplitLine(bf);
                n.label = parts[0];

                //Get all states
                for ( int i = 0; i < n.numStates; i++ ) {
                    line = bf.readLine();
                    n.states.add(line);
                }

                //Neighbours
                parts = readSplitLine(bf);
                n.numNeighs = Integer.parseInt(parts[0]);
                for ( int i = 0; i < n.numNeighs; i++ ) {
                    n.neighs.add( Integer.parseInt(parts[i + 2]) );
                }

                //Constraints
                parts = readSplitLine(bf);
                n.numConstrs = Integer.parseInt(parts[0]);
                for ( int i = 0; i < n.numConstrs; i++ ) {
                    Constraint c = new Constraint();
                    int legal;
                    parts = readSplitLine(bf);
                    c.neighbour = Integer.parseInt( parts[0] );
                    legal = Integer.parseInt( parts[2] );

                    for ( int j = 0; j < legal; ) {
                        line = bf.readLine();
                        parts = line.split(";");
                        for ( String s : parts ) {
                            if ( s.equals(" ") )
                                break;

                            int[] l = new int[2];
                            String[] k;

                            s = s.trim();
                            k = s.split(" ");

                            l[0] = Integer.parseInt(k[0]);
                            l[1] = Integer.parseInt(k[1]);

                            c.constrs.add(l);
                            j += 1;
                        }
                    }
                    n.constrs.add(c);
                }

                //Coordinate line
                bf.readLine();

                //Add node to nodelist
                nodes.add(n);
            }

        }
        catch ( Exception e ) {
            System.out.println("Error reading csp file.");
            e.printStackTrace(System.err);
            return(false);
        }
        return(true);
    }

    public static void main( String[] args ) {
        if ( args.length != 1 )
            System.out.println("USAGE: java CspAgent [cspfile]");
        else
            new CspAgent(args[0]);
    }
}
