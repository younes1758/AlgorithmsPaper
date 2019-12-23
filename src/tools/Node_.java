/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Younes
 */
public class Node_ {
    
    /*
    [*] id: identifiant of the node.
    [*] Sx: Similarity specific to a node, it is one value,
        but we used an ArrayList because it will be calculated 
        juste if we need it, so Sx is empty in begining.           
    [*] Sxy: an array that has for each a neighbor y the similarity of edge between this node and y.
    [*] succ: ann array of the neighbors.
    */
    public int id;   
//    public ArrayList<Double> Sx;
//    public Map<Node_, Double> Sxy;
    public ArrayList<Node_> succ = null;

    public Node_(int id){
        this.id = id;  
//        Sxy = new HashMap<>();       
//        Sx = new ArrayList<>();
        
        succ = new ArrayList<>();
    }
    
    



    
}
