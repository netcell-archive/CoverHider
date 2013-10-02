/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Codes;

import exception.MatrixInputException;
import matrix.Vector;
import matrix.VectorArray;

/**
 *
 * @author tebaomang
 */
public class Module3189 {
    
    private static final int infoBlockSize = 9;
    private static final int plainBlockSize = 18;
    private static final int[] Wi = new int[]{39,30,119,2,4,448,128,11,8,51,320,29,16,32,192,384,1,256};
    private static final VectorArray W = new VectorArray ( Wi, 9 );
    private static final Vector K = new Vector ( "101110000101110101");
    private static final int[] L = new int[]{67,180,244,308,372,436,500};
    private static final int [][] lF = new int[][]{
        {8,9,18,11},
        {8,3,9,15},
        {8,3,9,7},
        {8,3,9,11},
        {8,3,9,18},
        {8,3,9,6},
        {8,3,9,16}
    };
     
    /**
     * Embed method
     * Not important, only for debug
     * Written separately to test and minimize the risk
     */
    public static byte[] embed(byte[] Fi, byte[] info) throws MatrixInputException{
        Vector changes = new Vector(embedChanges(Fi,info));
        return new Vector(Fi).add(changes).tobyte();
    }
    
    /**
     * Embed method
     * Return the changes needed
     */
    
    public static int[] flipPosition(Vector x) throws MatrixInputException{
        for(int i=0;i<7;i++){
            if (x.isEqual(new Vector(L[i],infoBlockSize))){
                return lF[i];
            }
        }
        
        Vector combination;
        int[] result = new int[3];
        int i,j,k;
        
        for(i=0;i<Wi.length;i++){
            combination = new Vector(infoBlockSize);
            combination = combination.add(W.getVector(i));
            if(combination.isEqual(x)){
                return new int[]{i+1,0,0};
            }
        }
        
        for(i=0;i<Wi.length;i++){
            for(j=i+1;j<Wi.length;j++){
                combination = new Vector(infoBlockSize);
                combination = combination.add(W.getVector(i)).add(W.getVector(j));
                if(combination.isEqual(x)){
                    return new int[]{i+1,j+1,0};
                }
            }
        }
        
        for(i=0;i<Wi.length;i++){
            for(j=i+1;j<Wi.length;j++){
                for(k=j+1;k<Wi.length;k++){
                    combination = new Vector(infoBlockSize);
                    combination = combination.add(W.getVector(i)).add(W.getVector(j)).add(W.getVector(k));
                    if(combination.isEqual(x)){
                        return new int[]{i+1,j+1,k+1};
                    }
                }
            }
        }
        
        return result;
    }
    
    public static byte[] embedChanges(byte[] Fi, byte[] info) throws MatrixInputException{
        
        if (info.length!=infoBlockSize) {
            throw new MatrixInputException();
        }
        
        Vector F = new Vector(Fi).add(K);
        Vector b = new Vector(info);
        
        Vector s = W.pairWiseMultiply(F);
        
        if (s.isEqual(b)){
            return new Vector(plainBlockSize).tobyte();
        } else {
            Vector result = new Vector(plainBlockSize);
            Vector x = s.add(b);
            int[] flipPosition = flipPosition(x);
            for(int i=0;i<flipPosition.length;i++){
                if (flipPosition[i]!=0){
                    result.set(flipPosition[i]-1, (byte) 1);
                }
            }
            return result.tobyte();
        }
    }
    
    /**
     * Extract method
     * Return the information extracted
     */
    public static byte[] extract(byte[] Fi) throws MatrixInputException{
        
        Vector result = new Vector(plainBlockSize);
        
        Vector F = new Vector(Fi).add(K);
        
        Vector s = W.pairWiseMultiply(F);
        
        /* Return s as a binary array with length r */
        return s.tobyte();
        
    }
}
