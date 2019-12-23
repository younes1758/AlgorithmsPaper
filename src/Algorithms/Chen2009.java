/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import tools.Edge_;
import tools.NetworkModel;
import tools.Node_;
import tools.ToolsForChen2009;

/**
 *
 * @author Younes
 */
public class Chen2009 {

    public Edge_ edge[]; //Adjacency table  
    public int head[]; // Head node subscript

    int[] degrees;

    NetworkModel graph;

    public Chen2009(NetworkModel graph) {

        if (graph != null) {
            this.graph = graph;
            this.edge = graph.getEdges();
            this.head = graph.getHead();
            this.degrees = graph.getDegrees();
        }

    }

    public int[] getCommunity(int i0) {

        if (graph == null) {
            return null;
        }

        int n0 = i0;

        ToolsForChen2009 tools = new ToolsForChen2009();

        Map<Integer, Integer> LID;   //Number of Links between node in D and other nodes of D.
        Map<Integer, Integer> LIB;   //Number of Links between node in S and other nodes of D, LIB.get(n) it is the number of links between n, a node of S, and other nodes of B.
        double Lin;
        double Lex;
        int a = -1;
        double L;

        double delta;

        ArrayList<Integer> D, S, B;

        //initialisaton
        D = new ArrayList<>();
        S = new ArrayList<>();
        B = new ArrayList<>();
        LIB = new HashMap<>();
        LID = new HashMap<>();

        D.add(n0);
        B.add(n0);
        Lin = 0;
        Lex = degrees[n0];
        L = 0;
        delta = 0;

        //succ of n0    
        for (int n = head[n0]; n != -1; n = edge[n].next) {
            S.add(edge[n].v);
            LIB.put(edge[n].v, 1);
        }

        LID.put(n0, 0);

        int step = 0;

        /*
        the conditon of stoping the while loop is that
        delta is negativ or after visiting all nodes in S.
        But we con have Lex zero, so we chould find an 
        equivalent condition:
            delta = (Lin1/Lex1) - (Lin/Lex) > 0
            ===> Lin1 * Lex > Lin * Lex1
        so the stoping condition is : 
            ![(Lin1 * Lex - Lin * Lex1 > 0) && |S| > 0]
        we put delta = Lin1 * Lex - Lin * Lex1, we have:
             ![delta > 0 && |S| > 0]
         */
        int S_size;
        do {

            //System.out.println(" step " + step);
            step++;

            //********************* Discovery Phase **************************//
            /*
            Because the the set of the comman neighbors between 
            a node and D is calculated in tools.calculL(), so we
            dont need to calculate it again, it is the raison of
            using commanNeighborsWithD.            
             */
            ArrayList<Integer> commanNeighborsWithD = new ArrayList<>();

            //get the max
            double[] L1 = null;

            /*
            we start by L1Max = 0, to do that, we put 
            L1Max[0] = 0 and L1Max[1] = 1 to avoide division by 0.
             */
            double[] L1Max = {0.0, 1.0};

            for (Integer n : S) {
                ArrayList<Integer> tmp = new ArrayList<>();

                L1 = tools.calculL(graph, B, D, tmp, n, LID, LIB, S);

                if ((double) L1[0] / (double) L1[1] > (double) L1Max[0] / (double) L1Max[1]) {
                    L1Max[0] = L1[0];
                    L1Max[1] = L1[1];
                    a = n;
                    commanNeighborsWithD = tmp;
                }
            }//end for

            //if(first or third case):
            //if((L1Max[0] > Lin && L1Max[1] > Lex) || (L1Max[0] > Lin && L1Max[1] < Lex)) {
            if (L1Max[0] > Lin) {

                //update D:
                D.add(a);

                //update S:
                /*
                    add any neighbor of a that is not in  D ans S to S
                 */
                //succ of a                
                for (int n = head[a]; n != -1; n = edge[n].next) {
                    if (!S.contains(edge[n].v) && !D.contains(edge[n].v)) {
                        S.add(edge[n].v);
                    }
                }

                //update LID and B 
                for (Integer n : commanNeighborsWithD) {
                    LID.replace(n, LID.get(n) + 1);

                    /*
                        if all neighbors of n are in D, so n
                        chould be removed from B, i.e. it will
                        not be a boundary node anymore.
                     */
                    if (LID.get(n) == degrees[n]) {
                        B.remove((Integer) n);
                    }
                }
                LID.put(a, commanNeighborsWithD.size());

                /*
                    We remove number of links of a with B to avoide 
                    visiting it and consequensly reduce a little the complexity.
                 */
                LIB.remove((Integer) a);

                /*
                    If all neighbors of a are in D, so a chould not 
                    be added to B, else it chould be added to B
                 */
                if (LID.get(a) < degrees[a]) {
                    B.add(a);

                    for (int n = head[a]; n != -1; n = edge[n].next) {
                        if (S.contains(edge[n].v)) {
                            if (LIB.containsKey(edge[n].v)) {
                                LIB.replace(edge[n].v, LIB.get(edge[n].v) + 1);

                            } else {
                                LIB.put(edge[n].v, 1);
                            }
                        }
                    }
                }

                //update L and delta
                delta = L1Max[0] * Lex - L1Max[1] * Lin;
                Lin = L1Max[0];
                Lex = L1Max[1];
                L = Lin / Lex;
            }//end if 1st or 3rd case

            S.remove((Integer) a);

            S_size = S.size();


        } while (delta > 0 && S_size > 0);

        //*********************** Examination phase **************************//
        Iterator<Integer> iter = D.iterator();

        while (iter.hasNext()) {

            /*
            Because the the set of the comman neighbors between 
            a node and D is calculated in tools.calculL(), so we
            dont need to calculate it again, it is the raison of
            using commanNeighborsWithD.            
             */
            ArrayList<Integer> commanNeighborsWithD = new ArrayList<>();

            int n = iter.next();
            //calcule L'
            double[] L1 = tools.calculL1(graph, D, B, commanNeighborsWithD, n, LID, LIB, S);

            /*
            Cumpute L' after removing node n from D, if Lin reduce and 
            Lex raise, so it is the third case, wich means that adding 
            the node n has a reversing effect, i.e. Lin will raise ans 
            Lex will reduce wich is the first case.            
             */
            //if it is not third case
            if (!(L1[0] < Lin && L1[1] > Lex)) {

                //update D:
                iter.remove();

                /*
                if n was a boundary node, it chould be removed from B 
                after removing it from D.
                 */
                if (B.contains(n)) {
                    B.remove((Integer) n);
                }

                //reduce by 1 all neighbors of n that are in D:
                /*
                    possible imporvment of for loop: start 
                    with the community that has the lower 
                    number of node, choose beweent D and n.succ.
                 */
                for (Integer t : commanNeighborsWithD) {
                    LID.replace(t, LID.get(t) - 1);

                    /*
                        in this case, if t become a boundary
                        node after removing one link between it
                        and n, t chould be added to B.
                     */
                    if (LID.get(t) < degrees[t] && !B.contains(t)) {
                        B.add(t);
                    }

                }

                //update Lin and Lex:
                Lin = L1[0];
                Lex = L1[1];
            }
        }

        boolean exists = false;
        int[] local_community = null;
        if (D != null) {
            local_community = new int[D.size()];
            for (int i = 0; i < D.size(); i++) {
                local_community[i] = D.get(i);
                if (local_community[i] == i0) {
                    exists = true;
                }
            }
            /*
            if(!exists){
                local_community = null;
            }
             */
        }

        return local_community;
    }

}
