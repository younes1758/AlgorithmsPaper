/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Younes
 */
public class GraphManip {

//    Edge_ edge[] = null;
//    int head[] = null;
//    int top;
//    double[] node_weight; // Node weight  
//    double[] inNode_weight; // input Node weight  
//    double[] outNode_weight; // output Node weight  
//    int[] degrees;
//    int[] inDegrees;
//    int[] outDegrees;
//    double totalEdgeWeigth;
    public static NetworkModel loadGraph(String filepath) {

        NetworkModel graph = null;
        int N, m;

        /*
         * upload the network from an existed file
         */
        File file = new File(filepath);
        if (file.exists() && file.isFile()) {
            InputStreamReader read = null;
            try {
                String encoding = "UTF-8";
                // Determine if the file exists
                read = new InputStreamReader(new FileInputStream(file), encoding); // Considering the encoding format
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                lineTxt = bufferedReader.readLine();
                String cur2[] = lineTxt.split(" ");

                /**
                 * if there is an attributs and the number of the nodes of the
                 * new graph is different from the number of the nodes fo the
                 * attributs the use would change the graph file.
                 */
                N = Integer.parseInt(cur2[0]);
                m = Integer.parseInt(cur2[1]);
                graph = new NetworkModel(N, m);
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String cur[] = lineTxt.split(" ");

                    int u = Integer.parseInt(cur[0]);
                    int v = Integer.parseInt(cur[1]);
                    double curw;
                    if (cur.length > 2) {
                        curw = Double.parseDouble(cur[2]);
                    } else {
                        curw = 1.0;
                    }
                    graph.addEdge(u, v, curw);
                    graph.addNodeWeigth(u, curw);
                }
                bufferedReader.close();
                read.close();
            } catch (FileNotFoundException ex) {
                System.out.println(" -------------------- Graph: FileNotFoundException -----------------------------");
                Logger.getLogger(GraphManip.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                System.out.println(" -------------------- Graph: UnsupportedEncodingException -----------------------------");
                Logger.getLogger(GraphManip.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println(" -------------------- Graph: IOException -----------------------------");
                Logger.getLogger(GraphManip.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return graph;
    }

    public static double[][] loadAttributs(String filepath) {
        double[][] attrs = null;
        int N, r;  // r is number of attributs per node
        String encoding = "UTF-8";
        File file = new File(filepath);
        if (file.exists() && file.isFile()) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTxt = bufferedReader.readLine();
                N = Integer.parseInt(lineTxt.split(" ")[0]);
                r = Integer.parseInt(lineTxt.split(" ")[1]);
                String[] cut;
                attrs = new double[N][r];
                int idx = 0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    cut = lineTxt.split(" ");
                    for (int i = 0; i < cut.length; i++) {
                        attrs[idx][i] = Double.parseDouble(cut[i]);
                    }
                    idx++;
                }
            } catch (FileNotFoundException ex) {
                System.out.println(" -------------------- Attributs: FileNotFoundException -----------------------------");
                Logger.getLogger(GraphManip.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                System.out.println(" -------------------- Attributs: UnsupportedEncodingException -----------------------------");
                Logger.getLogger(GraphManip.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println(" -------------------- Attributs: IOException -----------------------------");
                Logger.getLogger(GraphManip.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return attrs;
    }

    public static int[] loadClasses(String filepath) {
                
        int[] classes = null;
        String encoding = "UTF-8";
        File file = new File(filepath);
        if (file.exists() && file.isFile()) {

            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTxt = bufferedReader.readLine();
                classes = new int[Integer.parseInt(lineTxt.split(" ")[0])];
                String[] cut;
                int idx = 0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    cut = lineTxt.split(" ");
                    classes[idx] = Integer.parseInt(cut[0]);
                    idx++;
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GraphManip.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(GraphManip.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GraphManip.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(GraphManip.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return classes;
    }
}
