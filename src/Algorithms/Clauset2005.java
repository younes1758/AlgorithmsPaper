/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.ArrayList;
import tools.Edge_;
import tools.NetworkModel;

/**
 *
 * @author Younes
 */
public class Clauset2005 {

    public Edge_ edge[]; //Adjacency table  
    public int head[]; // Head node subscript

    int[] degrees;

    NetworkModel graph;

    ArrayList<Integer> D;   //the local community
    ArrayList<Integer> C;   //the heart of D
    ArrayList<Integer> B;   //the border nodes of D, (C U B = D)
    ArrayList<String> S;   //the neighbors of each node in B
    int k;      //size of local community
    int v0;     //starting node

    ClausetModularity R;

    boolean[] isInS; //each node in N, its value in isInS is true.
    boolean[] isInB; //each node in B, its value in isInB is true.
    boolean[] isInD; //each node in D, its value in isInB is true.

    public Clauset2005(NetworkModel graph) {

        if (graph != null) {
            this.graph = graph;
            this.edge = graph.getEdges();
            this.head = graph.getHead();
            this.degrees = graph.getDegrees();
        }

    }

    public int[] getCommunity(int i0, int k) {

        //** initialisation
        this.D = new ArrayList<>();
        this.C = new ArrayList<>();
        this.B = new ArrayList<>();
        this.S = new ArrayList<>();
        isInS = new boolean[graph.getN()];
        isInB = new boolean[graph.getN()];
        isInD = new boolean[graph.getN()];
        v0 = i0;

        R = new ClausetModularity();
        R.Bin = 0;
        R.Bout = degrees[v0];
        R.R = 0.0;

        int count = 1;  //counter for the size of D

        //init D and B and N
        D.add(v0);
        isInD[v0] = true;
        B.add(v0);
        isInB[v0] = true;
        for (int i = head[v0]; i != -1; i = edge[i].next) {
            S.add("" + edge[i].v);
            isInS[edge[i].v] = true;
        }

        int step = 0;
        while (count < k && !S.isEmpty()) {

            System.out.println(" step " + step);
            step++;
            
            //compute the deltaR max
            ClausetModularity RMax = new ClausetModularity(0, 0.0, 0);
            int choosenV = -1;
            ClausetModularity R1;
            for (String v : S) {
                R1 = computeR1(R, Integer.parseInt(v));
                if (RMax.R <= R1.R) {
                    RMax.R = R1.R;
                    RMax.Bin = R1.Bin;
                    RMax.Bout = R1.Bout;
                    choosenV = Integer.parseInt(v);
                    RMax.dNj = R1.dNj;
                }
            }

            if (choosenV >= 0) {

                //updates D and S
                D.add(choosenV);
                isInD[choosenV] = true;

                S.remove("" + choosenV);

                isInS[choosenV] = false;
                for (int i = head[choosenV]; i != -1; i = edge[i].next) {
                    if (!isInD[edge[i].v] && !isInS[edge[i].v]) {
                        S.add("" + edge[i].v);
                        isInS[edge[i].v] = true;
                    }
                }

                //update B and C:
                if (RMax.dNj == 0) {  //node added to C
                    C.add(choosenV);
                    isInB[choosenV] = false;
                } else {
                    B.add(choosenV);
                    isInB[choosenV] = true;
                }
                count++;
            } else {
                System.out.println(" error: choosenV is negative.");
            }

        }

        int[] localCommunity = new int[D.size()];

        for (int i = 0; i < D.size(); i++) {
            localCommunity[i] = D.get(i);
        }

        return localCommunity;
    }

    ClausetModularity computeR1(ClausetModularity R, int nj) { 
//ArrayList<String> S, ArrayList<Integer> B, ArrayList<Integer> C

        ClausetModularity R1 = new ClausetModularity();

        //calcule dnj:
        R1.dNj = 0;
        //for each n:N(nj)
        for (int i = head[nj]; i != -1; i = edge[i].next) {
            if (isInS[edge[i].v]) {
                R1.dNj++;
            }
        }

        //calculate dBj:  degree of j in B
        int dBj = 0;
        for (int i = head[nj]; i != -1; i = edge[i].next) {
            if (isInB[edge[i].v]) {
                dBj++;
            }
        }

        //calculate cj: degree of j in C
        int dCj = 0;
        if (R1.dNj != 0) {
            for (int i = head[nj]; i != -1; i = edge[i].next) {
                if (!isInS[edge[i].v] && !isInB[edge[i].v]) {
                    dCj++;
                }
            }
        }

        //calculate Bin' and Bout'
        R1.Bout = R.Bout + R1.dNj - dBj;
        if (R1.dNj != 0) {
            R1.Bin = 2 * dBj + dCj;

        } else {
            R1.Bin = dBj;
        }

        R1.R = (double) R1.Bin / (double) (R1.Bin + R1.Bout);

        return R1;
    }

    class ClausetModularity {

        public int Bin;
        public int Bout;
        double R;
        public int dNj;  //number of edges that one extremity is this ana
        // the other is in N

        public ClausetModularity(int Bout, double R, int dNj) {
            this.Bout = Bout;
            this.R = R;
            this.dNj = dNj;
        }

        public ClausetModularity() {
        }
    }

}
