/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Younes
 */
public class ToolsForChen2009 {

    public double[] calculL(NetworkModel graph, ArrayList<Integer> B, ArrayList<Integer> D, ArrayList<Integer> commanNeighborsWithD, int a, Map<Integer, Integer> LID, Map<Integer, Integer> LIB, ArrayList<Integer> S) {

        double[] LRet = new double[2];

        int X = 0;
        for (int i : D) {
            X += LID.get(i);
        }

        //Lin': 
        LRet[0] = (double) (X * D.size() + 2 * LIB.get(a)) / (double) (D.size() + 1.0);

        //Lex'
        //1) initialize the size of B
        int B_size = B.size();
        if (graph.getDegrees()[a] > LIB.get(a)) {
            B_size++;
        }

        /*
        2) for all neighbors of node a that are in
           D we add to theire LID 1, if the LID of 
           any of them become exactly the size of 
           his neighbors, so it will not be a boudary
           node after adding the node a to D.           
         */
        if (!commanNeighborsWithD.isEmpty()) {
            commanNeighborsWithD.clear();
        }
        int p;
        for (int n = graph.getHead()[a]; n != -1; n = graph.getEdges()[n].next) {
            p = graph.getEdges()[n].v;
            if (D.contains(p)) {
                commanNeighborsWithD.add(p);
                if (LID.get(p) == graph.getDegrees()[p] - 1) {
                    B_size--;
                }
            }
        }

        int Y = 0;
        for (int i : S) {
            Y += LIB.get(i);
        }

        /*
            Indi = LIB.get(a)			
            outdi= a.succ.size() -LIB.get(a) 
         */
        if (B_size <= 0) {
            LRet[1] = 0;
        } else if (graph.getDegrees()[a] > LIB.get(a)) {
            LRet[1] = (double) (Y * B.size() + graph.getDegrees()[a] - 2 * LIB.get(a)) / (double) B_size;
        } else {
            LRet[1] = (double) (Y * B.size() - graph.getDegrees()[a]) / (double) B_size;
        }

        //System.out.println("          calculL() id7412: L("+a.id+") = "+LRet[0]+"/"+LRet[1]);
        return LRet;
    }

    public double[] calculL1(NetworkModel graph, ArrayList<Integer> D, ArrayList<Integer> B, ArrayList<Integer> commanNeighborsWithD, int a, Map<Integer, Integer> LID, Map<Integer, Integer> LIB, ArrayList<Integer> S) {

        double[] LRet = new double[2];

        int X = 0;
        for (int i : D) {
            X += LID.get(i);
        }

        //Lin'':
        LRet[0] = (double) (X * D.size() - 2 * LID.get(a)) / (double) (D.size() - 1);

        //Lex'':
        //1) initialize the size of B
        int B_size = B.size();
        //node in B
        if (graph.getDegrees()[a] > LID.get(a)) {
            B_size--;
        }

        /*
        2) for all neighbors of node a that are in
           D we remove from theire LID 1, if any of
           them is not in B it will become a boudary
           node after removing the node a from D.           
         */
        if (!commanNeighborsWithD.isEmpty()) {
            commanNeighborsWithD.clear();
        }

        int p;
        for (int n = graph.getHead()[a]; n != -1; n = graph.getEdges()[n].next) {
            p = graph.getEdges()[n].v;
            if (D.contains(p)) {
                commanNeighborsWithD.add(p);
                if (LID.get(p) == graph.getDegrees()[p]) {
                    B_size++;
                }
            }
        }

        int Y = 0;
        for (int i : S) {
            Y += LIB.get(i);
        }

        if (B_size == 0) {
            LRet[1] = 0;
        } else if (graph.getDegrees()[a] > LID.get(a)) {
            LRet[1] = (double) (Y * B.size() + 2 * LID.get(a) - graph.getDegrees()[a]) / (double) B_size;
        } else {
            LRet[1] = (double) (Y * B.size() + graph.getDegrees()[a]) / (double) B_size;
        }

        return LRet;
    }

    //*************************************************************************************************************************************
    //*************************************************************************************************************************************
}
