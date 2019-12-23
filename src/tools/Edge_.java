package tools;

public class Edge_ implements Cloneable{  
    public int v;     //v indicates the number of the connection point, and w indicates the weight of this edge
    public double weight;  
    public int next;    //next Responsible for connecting the edge associated with this point
    public Edge_(){}  
    public Object clone(){  
        Edge_ temp=null;  
        try{    
            temp = (Edge_)super.clone();   //浅复制    
        }catch(CloneNotSupportedException e) {    
            e.printStackTrace();    
        }     
        return temp;  
    }  
}  