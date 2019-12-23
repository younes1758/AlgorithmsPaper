/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paperalgorithms;

import Algorithms.Chen2009;
import Algorithms.Clauset2005;
import Algorithms.FMeseaur;
import Algorithms.Zhao2016;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.GraphManip;
import tools.NetworkModel;

/**
 *
 * @author Younes
 */
public class PaperAlgorithms {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //method  graph
        double[] recall = new double[4];
        double[] precision = new double[4];
        double[] fmeasure = new double[4];

        int nbr_classes = 5;
        String data_name[] = new String[]{"wisconsin", "washington", "texas", "cornell"};
        for (int str = 0; str < 4; str++) {
            String graph_path = "DataSet\\" + data_name[str] + ".cites.txt";
            String classes_path = "DataSet\\" + data_name[str] + ".content_Classes.txt";
            String attrs_path = "DataSet\\" + data_name[str] + ".content.txt";
            NetworkModel graph = tools.GraphManip.loadGraph(graph_path);
            graph.attributs = tools.GraphManip.loadAttributs(attrs_path);
            if (graph.attributs.length > 0) {
                graph.nbrAttribsPerNode = graph.attributs[0].length;
            } else {
                graph.nbrAttribsPerNode = 0;
            }
            graph.classes = tools.GraphManip.loadClasses(classes_path);

            //preparer les classes reeles
            Map<Integer, int[]> classes_réelles = new HashMap<>();
            int[] nbr_nodes_par_class = new int[nbr_classes];
            for (int j = 0; j < graph.N; j++) {
                nbr_nodes_par_class[graph.classes[j]]++;
            }
            for (int i = 0; i < nbr_classes; i++) {
                int tmp[] = new int[nbr_nodes_par_class[i]];
                int idx = 0;
                for (int j = 0; j < graph.N; j++) {
                    if (graph.classes[j] == i) {
                        tmp[idx++] = j;
                    }
                }
                for (int k = 0; k < nbr_nodes_par_class[i]; k++) {
                    classes_réelles.put(tmp[k], tmp);
                }
            }

            Map<Integer, int[]> classes_claculées = new HashMap<>();
//            Chen2009 chen = new Chen2009(graph);
//            Clauset2005 clauset = new Clauset2005(graph);
            Zhao2016 zhao = new Zhao2016(graph);
            for (int i = 0; i < graph.N; i++) {
//                classes_claculées.put(i, chen.getCommunity(i));
//                classes_claculées.put(i, clauset.getCommunity(i, 20));
                classes_claculées.put(i, zhao.getCommunity(i));
            }

            //calcule f measure:
            double[] tmp = new double[2];
            for (int i = 0; i < graph.N; i++) {
                tmp = FMeseaur.fmeseaur(classes_réelles.get(i), classes_claculées.get(i));
                recall[str] += tmp[0];
                precision[str] += tmp[1];
            }

            recall[str] = recall[str] / (double) graph.N;
            precision[str] = precision[str] / (double) graph.N;

            if (recall[str] > 0.0 && precision[str] > 0.0) {
                fmeasure[str] = 2.0 * recall[str] * precision[str] / (double) (recall[str] + precision[str]);
            } else {
                fmeasure[str] = 0.0;
            }

        }
        //write result in file            
        try {
            PrintWriter out = new PrintWriter("Results\\Zhao2016.txt");
            out.write("Methode Zhao 2016\n");

            int x;

            for (int str = 0; str < 4; str++) {
                out.write("\n " + data_name[str] + " recall : " + ((Math.round(recall[str] * 100000)) / 100000.0));
                out.write("\n " + data_name[str] + " precision : " + ((Math.round(precision[str] * 100000)) / 100000.0));
                out.write("\n " + data_name[str] + " f_measure : " + ((Math.round(fmeasure[str] * 100000)) / 100000.0));
                out.write("\n");
            }
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PaperAlgorithms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
