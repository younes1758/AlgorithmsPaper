/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

/**
 *
 * @author Younes
 */
public class FMeseaur {

    public static double[] fmeseaur(int[] CT, int[] CF) {
        double[] f_measure = new double[]{0.0, 0.0};

        int CT_inter_CF = 0;
        double precision;
        double recall;

        for (int i = 0; i < CT.length; i++) {
            for (int j = 0; j < CF.length; j++) {
                if (CT[i] == CF[j]) {
                    CT_inter_CF++;
                }
            }
        }

        if (CF.length > 0 && CT.length > 0 && CT_inter_CF > 0) {
            precision = (double) CT_inter_CF / (double) CF.length;
            recall = (double) CT_inter_CF / (double) CT.length;
        } else {
            precision = 0.0;
            recall = 0.0;
        }
        f_measure[0] = recall;
        f_measure[1] = precision;

        return f_measure;
    }
}
