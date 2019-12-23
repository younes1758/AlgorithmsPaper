/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import tools.Edge_;
import tools.NetworkModel;

/**
 *
 * @author Younes
 */
public class Zhao2016 {

    public NetworkModel graph;
    public Edge_ edge[]; //Adjacency table  
    public int head[]; // Head node subscript

    int[] degrees;

    //added variables      
    double[] Sx;
    double[][] Sxy;
    double[][] WSxy;
    double[][] Wxy;
    double[] simD;   //sim(x, D)
    double[] simN;   //sim(x, N)
    boolean[] Sx_is_calculated;
    boolean[][] Sxy_is_calculated;
    boolean[][] WSxy_is_calculated;
    boolean[][] Wxy_is_calculated;
    boolean[] simD_is_calculated;
    boolean[] simN_is_calculated;
    boolean[] visited;

    public Zhao2016(NetworkModel graph) {
        if (graph != null) {
            this.graph = graph;
            this.edge = graph.getEdges();
            this.head = graph.getHead();
            this.degrees = graph.getDegrees();

            Sx = new double[graph.getN()];
            simD = new double[graph.getN()];
            simN = new double[graph.getN()];
            Sxy = new double[graph.getN()][graph.getN()];
            WSxy = new double[graph.getN()][graph.getN()];
            Wxy = new double[graph.getN()][graph.getN()];
            Sx_is_calculated = new boolean[graph.getN()];
            simD_is_calculated = new boolean[graph.getN()];
            simN_is_calculated = new boolean[graph.getN()];
            Sxy_is_calculated = new boolean[graph.getN()][graph.getN()];
            WSxy_is_calculated = new boolean[graph.getN()][graph.getN()];
            Wxy_is_calculated = new boolean[graph.getN()][graph.getN()];
            visited = new boolean[graph.getN()];
        }
    }

    int step = 0;

    public int[] getCommunity(int i0) {

        int[] local_community = null;

        int n0 = i0;

        ArrayList<Integer> D = null;
        ArrayList<Integer> N = null;
        double simMax;                      //to calculate the maximum of simiarities between nodes in N and nodes in D.
        int node_of_simMax;       //ro preserve the last node added to D, to use it for calculating sim.
        double S1;                          // the numerator of CI = S1
        double S2;                          // the denomerator of CI = 1 + S2
        double S1_, S2_;   //S1' and S2'
        double CI, CI2;

        // ************ allocation of variables        
        //simList = new HashMap<Node_, Double>();
        D = new ArrayList<>();
        N = new ArrayList<>();

        // ************* initialize phase       
        D.add(n0);

        //add neighbors of n0 to N
        for (int j = graph.getHead()[n0]; j != -1; j = graph.getEdges()[j].next) {
            N.add(graph.getEdges()[j].v);
        }

        if (N.size() > 0) {

            //initialize simD:
            /*
        The reccurent formular for sim(a, D) is: 
        sim(v, D) = sim(v, D/{a})+w(a, v), so we
        have to sotre the old value of similarities 
        in simList to evoid visiting all nodes each
        step. But, for the first step, when D = {n0},
        there is no similarities, so we initialize 
        them:
                sim(v, {n0}) = w(v, n0)        
             */
            simMax = 0.0;
            node_of_simMax = -1;
            for (int n : N) {

                //calcul sim(n, {n0}):
                calculeWxy(n, n0);
                Wxy_is_calculated[n][n0] = true;
                Wxy_is_calculated[n0][n] = true;

                simD[n] = Wxy[n][n0];
                if (simD[n] >= simMax) {
                    simMax = simD[n];
                    node_of_simMax = n;
                }
                simD_is_calculated[n] = true;
            }

            visited[node_of_simMax] = true;

            //initializ simN
            for (int n : N) {
                simN[n] = 0;
                for (int i = graph.getHead()[n]; i != -1; i = graph.getEdges()[i].next) {
                    if (!Wxy_is_calculated[n][graph.getEdges()[i].v]) {
                        calculeWxy(n, graph.getEdges()[i].v);
                        Wxy_is_calculated[n][graph.getEdges()[i].v] = true;
                        Wxy_is_calculated[graph.getEdges()[i].v][n] = true;
                    }
                    simN[n] += Wxy[n][graph.getEdges()[i].v];
                    simN_is_calculated[n] = true;
                }
            }

            //initialize S1 and S2 and CI    
            S1 = 0;  // S1 = Wxy(n0, n0)
            S2 = 0;  // S2 = sim(n0, N) = sim(n0, neighbors(n0))
            calculeWxy(n0, n0);
            Wxy_is_calculated[n0][n0] = true;
            S1 = Wxy[n0][n0];
            for (int i = graph.getHead()[n0]; i != -1; i = graph.getEdges()[i].next) {
                S2 += Wxy[n0][graph.getEdges()[i].v];
            }

            CI = S1 / (double) (1.0 + S2);

            // ************ While loop
            int N_size = N.size();
            int step = 0;
            do {

                System.out.println(" step " + step);
                step++;

                //check phase:            
                //calcul S1' and S2'
                S1_ = S1 + simMax;
                S2_ = S2 - simMax + simN[node_of_simMax];
                CI2 = S1_ / (double) (1.0 + S2_);

                if (CI2 > CI) {

                    //make the updates:
                    S1 = S1_;
                    S2 = S2_;
                    CI = CI2;

                    D.add(node_of_simMax);

                    //add the nieghbors of a that are not in D and N to N:
                    for (int l = graph.getHead()[node_of_simMax]; l != -1; l = graph.getEdges()[l].next) {

                        //if t is not in D and N                    
                        if (!D.contains(graph.getEdges()[l].v) && !N.contains(graph.getEdges()[l].v) && !visited[graph.getEdges()[l].v]) {
                            N.add(graph.getEdges()[l].v);
                        }
                    }

                    //update simD
                    for (int i = 0; i < graph.getN(); i++) {
                        if (simD_is_calculated[i]) {
                            if (!Wxy_is_calculated[i][node_of_simMax]) {
                                calculeWxy(i, node_of_simMax);
                                Wxy_is_calculated[node_of_simMax][i] = true;
                                Wxy_is_calculated[i][node_of_simMax] = true;
                            }
                            simD[i] += Wxy[node_of_simMax][i];
                        }
                    }
                }

                N.remove((Integer) node_of_simMax);
                N_size = N.size();

                //update simN
                for (int i = 0; i < graph.getN(); i++) {
                    if (simN_is_calculated[i]) {
                        if (!Wxy_is_calculated[i][node_of_simMax]) {
                            calculeWxy(i, node_of_simMax);
                            Wxy_is_calculated[node_of_simMax][i] = true;
                            Wxy_is_calculated[i][node_of_simMax] = true;
                        }

                        simN[i] -= Wxy[node_of_simMax][i];
                    }
                }

                //compute simMax     
                simMax = 0.0;
                node_of_simMax = -1;

                for (int n : N) {
                    if (!simD_is_calculated[n]) {

                        //clacule simD(n)
                        simD[n] = 0;
                        for (int i = graph.getHead()[n]; i != -1; i = graph.getEdges()[i].next) {
                            if (D.contains((Integer) graph.getEdges()[i].v)) {
                                if (!Wxy_is_calculated[n][graph.getEdges()[i].v]) {
                                    calculeWxy(n, graph.getEdges()[i].v);
                                    Wxy_is_calculated[n][graph.getEdges()[i].v] = true;
                                    Wxy_is_calculated[graph.getEdges()[i].v][n] = true;
                                }
                                simD[n] += Wxy[n][graph.getEdges()[i].v];
                            }
                        }
                    }

                    if (simMax <= simD[n]) {
                        simMax = simD[n];
                        node_of_simMax = n;
                    }

                }
                //System.out.println("\n 478542 N "+N.size()+", simMax = " + simMax + ", n = " + node_of_simMax);

            } while (N_size > 0);

            local_community = new int[D.size()];

            for (int i = 0; i < D.size(); i++) {
                local_community[i] = D.get(i);
            }
        } else {
            local_community = new int[1];
            local_community[0] = i0;
        }
        return local_community;
    }

    private void calculeSxy(int x, int y) {

        if (graph.getDegrees()[x] * graph.getDegrees()[x] != 0) {
            int node1 = x;
            int node2 = y;
            if (graph.getDegrees()[x] > graph.getDegrees()[y]) {
                node1 = y;
                node2 = x;
            }
            double sum = 0.0;
            for (int j = graph.getHead()[node1]; j != -1; j = graph.getEdges()[j].next) {
                for (int l = graph.getHead()[node2]; l != -1; l = graph.getEdges()[l].next) {
                    if (graph.getEdges()[j].v == graph.getEdges()[l].v) {
                        sum += 1.0;
                    }
                }
            }

            Sxy[x][y] = (double) sum / (double) Math.sqrt(graph.getDegrees()[x] * graph.getDegrees()[y]);
            Sxy[y][x] = Sxy[x][y];
        }

    }

    private void calculSx(int x) {

        double sum = 0;
        for (int j = graph.getHead()[x]; j != -1; j = graph.getEdges()[j].next) {
            if (!Sxy_is_calculated[x][graph.getEdges()[j].v]) {
                calculeSxy(x, graph.getEdges()[j].v);
                Sxy_is_calculated[x][graph.getEdges()[j].v] = true;
                Sxy_is_calculated[graph.getEdges()[j].v][x] = true;
            }
            sum += Sxy[x][graph.getEdges()[j].v];
        }
        Sx[x] = sum;
    }

    private void calculeWSxy(int x, int y) {
        int sum = 0;
        for (int i = graph.getHead()[x]; i != -1; i = graph.getEdges()[i].next) {
            for (int j = graph.getHead()[y]; j != -1; j = graph.getEdges()[j].next) {
                if (graph.getEdges()[i].v == graph.getEdges()[j].v) {
                    sum += Sxy[graph.getEdges()[i].v][graph.getEdges()[j].v];
                }
            }
        }

        if (!Sx_is_calculated[x]) {
            calculSx(x);
        }
        if (!Sx_is_calculated[y]) {
            calculSx(y);
        }
        if (Sx[x] + Sx[y] > 0.0) {
            WSxy[x][y] = (double) sum / (double) (Sx[x] + Sx[y]);
            WSxy[x][y] = WSxy[y][x];
        } else {
            WSxy[x][y] = 0.0;
            WSxy[x][y] = 0.0;
        }
    }

    private void calculeWxy(int x, int y) {
        if (!WSxy_is_calculated[x][y]) {
            calculeWSxy(x, y);
            WSxy_is_calculated[x][y] = true;
            WSxy_is_calculated[y][x] = true;
        }

        Wxy[x][y] = WSxy[x][y] + (double) (graph.getDegrees()[x] * graph.getDegrees()[y]) / (double) (graph.getM());
    }

}
