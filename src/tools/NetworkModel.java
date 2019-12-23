/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author Younes
 */
public class NetworkModel {

    public int N;
    public int m;
    public Edge_ edge[] = null;
    public int head[] = null;
    public int top;
    public int nbrAttribsPerNode;    //number of attributs per node
    public int[] classes;
    public double[] node_weight; // Node weight  
    public double[] inNode_weight; // input Node weight  
    public double[] outNode_weight; // output Node weight  
    public int[] degrees;
    public int[] inDegrees;
    public int[] outDegrees;
    public double totalEdgeWeigth;
    public double[][] attributs;

    public NetworkModel(int N, int m) {
        this.N = N;
        this.m = m;
        node_weight = new double[N];
        edge = new Edge_[m];
        head = new int[N];
        for (int i = 0; i < N; i++) {
            head[i] = -1;
        }
        top = 0;
        totalEdgeWeigth = 0;
        degrees = new int[N];
        inDegrees = new int[N];
        outDegrees = new int[N];
        inNode_weight = new double[N];
        outNode_weight = new double[N];
        classes = new int[N];
    }

    public void addEdge(int u, int v, double weight) {
        if (edge[top] == null) {
            edge[top] = new Edge_();
        }
        edge[top].v = v;
        edge[top].weight = weight;
        edge[top].next = head[u];
        head[u] = top++;
        degrees[u]++;
        inDegrees[v]++;
        outDegrees[u]++;
        inNode_weight[v] += weight;
        outNode_weight[u] += weight;
    }

    public void addNodeWeigth(int u, double w) {
        node_weight[u] += w;
        totalEdgeWeigth += w;
    }

    //setters
//    public void setEdge(Edge_[] edge) {
//        for (int i = 0; i < edge.length; i++) {
//            this.edge[i] = edge[i];
//        }
//    }
//
//    public void setHead(int[] head) {
//        for (int i = 0; i < head.length; i++) {
//            this.head[i] = head[i];
//        }
//    }
//
//    public void setTop(int top) {
//        this.top = top;
//    }
//
//    public void setNode_weight(double[] node_weight) {
//        for (int i = 0; i < N; i++) {
//            this.node_weight[i] = node_weight[i];
//        }
//    }
//
//    public void setDegrees(int[] degrees) {
//        for (int i = 0; i < N; i++) {
//            this.degrees[i] = degrees[i];
//        }
//    }
//
//    public void setTotalEdgeWeigth(double totalEdgeWeigth) {
//        this.totalEdgeWeigth = totalEdgeWeigth;
//    }
//
//    public void setAttributs(double[][] attributs) {
//        this.attributs = attributs;
//    }
    //getters
    public Edge_[] getEdges() {
        return edge;
    }

    public int[] getHead() {
        return head;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return N;
    }

    public int getTop() {
        return top;
    }

    public double[] getInNode_weight() {
        return inNode_weight;
    }

    public double[] getOutNode_weight() {
        return outNode_weight;
    }

    public int[] getInDegrees() {
        return inDegrees;
    }

    public int[] getOutDegrees() {
        return outDegrees;
    }

    public double[] getNode_weight() {
        return node_weight;
    }

    public double getTotalEdgeWeigth() {
        return totalEdgeWeigth;
    }

    public int[] getDegrees() {
        return degrees;
    }

    public double[][] getAttributs() {
        return attributs;
    }

}
