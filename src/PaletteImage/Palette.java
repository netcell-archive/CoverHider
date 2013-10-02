/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PaletteImage;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

/**
 *
 * @author tebaomang
 */
public class Palette {
    private byte[] reds, greens, blues;
    private int size; /* Number of indices in the pallete*/
    
    /* Constructors */
    public Palette(BufferedImage imageFile){
        
        IndexColorModel model = (IndexColorModel) imageFile.getColorModel();
        
        size = model.getMapSize();
        
        reds = new byte[size];
        greens = new byte[size];
        blues = new byte[size];
        model.getReds(reds);
        model.getGreens(greens);
        model.getBlues(blues);
    }
    
    /* Copier */
    public Palette(Palette that){
        size = that.size;
        reds = new byte[size];
        greens = new byte[size];
        blues = new byte[size];
        System.arraycopy(that.reds, 0, reds, 0, size);
        System.arraycopy(that.greens, 0, greens, 0, size);
        System.arraycopy(that.blues, 0, blues, 0, size);
    }
    
    public IndexColorModel getModel(){
        return new IndexColorModel(8,size,reds,greens,blues);
    }
    
    /* Get color values of an index */
    public int getRed(int i){
        return (int) reds[i] & 0xff;
    }
    
    public int getGreen(int i){
        return (int) greens[i] & 0xff;
    }
    
    public int getBlue(int i){
        return (int) blues[i] & 0xff;
    }
    
    public int getSize(){
        return size;
    }
    
    /* Get the index it self*/
    public Index getIndex(int i){
        return new Index(reds[i] & 0xff,greens[i] & 0xff,blues[i] & 0xff);
    }
    
    /* Distance between two indices */
    public double distance(int i, int j){
        return getIndex(i).distance(getIndex(j));
    }
    
    /* Find the nearest index */
    public int nearestIndex(int i){
        
        int result = 0;
        
        if (i==0) result = 1; /* Initialization at the first index*/
        
        double minDistance = distance(i,result);
        
        /* Search for index with minimum distance */
        for (int j=0;j<size;j++){
            if (i!=j && j!=result){
                double d = distance(i,j);
                if (d<minDistance){
                    minDistance = d;
                    result = j;
                }
            }
        }
        
        return result;
    }
    
    /* Generate Next[] */
    public int[] nearestMap(){
        
        int[] result = new int[size];
        
        for (int i=0; i<size; i++){
            result[i]=nearestIndex(i);
        }
        
        return result;
    }
    
    /* Generate Value[] */
    public byte[] value(int[] Next){
        
        byte[] result = new byte[size];
        
        int j; // Doesn't matter
        
        /* Initialization: no value for any index */
        for(j=0; j<size; j++) result[j]=2;
        
        /* Initialization: The first index */
        int i=0;
        result[i]=0;
        byte current=0; /* current value to fill */
        
        /* First Rho */
        while(true){
            j=i;
            
            i = Next[i];
            current= (byte) (1-current); 
            result[i] = current ;
            
            /* Control to stop when the rho is completed */
            if (Next[i]==j) break;
        }
        
        int k=0;
        
        /* Fill the rest */
        while(true){
            
            /* Every index filled*/
            if (k==size-1) break;
            
            /* Find an index not calc */
            j=0;
            while(j<size-1 && result[j]!=2) j++;
            k=j;
            
            /* Search for rho loop or root 
             * Keep going until find a loop or a root
             */
            while(result[j]==2){
                
                i=Next[j];
                
                /* Found a rho loop */
                if (Next[i]==j) {
                    result[i]=0;
                    result[j]=1;
                }
                
                /* Find a new root */
                if (result[i]==1) result[j]=0;
                if (result[i]==0) result[j]=1;
                
                j=i;
            }
            
        }
        
        return result;
    }
    
}
