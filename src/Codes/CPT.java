/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Codes;

import exception.MatrixInputException;
import matrix.Vector;

/**
 *
 * @author tebaomang
 */
public class CPT {
    
    private static String k = "0010110101011001000011000000111010010111000010001"
                            + "0011110111010101110010111000001001010011100110001"
                            + "0111110100010100110111101111101010100110101000010"
                            + "0101110000100001100000010000011110011110100110010"
                            + "0001111000000000001000101101001111000101011011000"
                            + "0100000011000101010100111011110011000001001110111"
                            + "0111100001111100100110101100111001001001011111000"
                            + "0101100011011011101001111101101000100110001111010"
                            + "1000101110001110001010011101110011011010000000001"
                            + "1110101111101011000000001110101010010001110011010"
                            + "0010101101001011000000101011001000100111011110110"
                            + "0110001000111101100101001111011101001100001100010"
                            + "0101001011010110110100101000101100010010001011011"
                            + "1100000000101101111101000100111000011011011110000"
                            + "0001001101011101111011011010001010111100110001101"
                            + "1101110001100001000011001101110000110001001010001"
                            + "1101110010010111110001001001001110011010111000010"
                            + "0000011011010000111100011100110000011000111001011"
                            + "1101101001001010100000011101010100101101001010101"
                            + "1011001100010111101000010101010111111000110011111";

    /* Info block size */
    public static int embedBitLength(int blockLength){
        return (int) Math.floor(Math.log(blockLength+1)/Math.log(2));
    }
    
    /* Get the key */
    public static Vector key(int blockLength){
        return new Vector(k.substring(0, blockLength));
    }
    
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
    public static byte[] embedChanges(byte[] Fi, byte[] info) throws MatrixInputException{
        
        int length = Fi.length;
        
        Vector result = new Vector(length);
        
        int r = embedBitLength(length);
        int pow2r = (int) Math.pow(2, r);
        
        if (info.length!=r) throw new MatrixInputException();
        
        Vector K = key(length);
        Vector F = new Vector(Fi);
        Vector W = new Vector(length);
        W.populateCPT(r); /* Fill the weight matrix*/
        
        /* Step 1: */
        Vector FK = F.add(K);
        
        /* Step 2: */
        int s = FK.pairWiseMultiply(W).sum() % (int) pow2r;
        
        /* Step 3: */
        int d = Codes.bin2Int(info) - s;
        
        /* Step 4: */
        
        /* No changes needed: d=0 */
        if (d==0) return result.tobyte();
        
        if (d!=0){
            
            /* h=0 or h=1 */
            for (int j=0; j<length;j++)
                if ( ( W.get(j) == d && FK.get(j) ==0 ) || 
                     ( W.get(j) == ((pow2r - d)%pow2r) && FK.get(j) ==1 ) ) {
                    result.set(j, (byte) 1);
                    return result.tobyte();
                }
            
            /* h>1 */
            for (int h=2;h<pow2r;h++){
                int w1 = (h*d)%pow2r;
                int w2 = -(h-1)*d%pow2r;
                
                /* If d>0 continue
                 * Else, switch w1 and w2
                 */
                if (d<0) {int x = w1; w1=w2; w2=x;}
                
                /* Search for S(d) and S(-(h-1)d) */
                for (int m=0; m<length;m++){
                    if ( ( (W.get(m) == w1) && (FK.get(m) ==0) ) || w1==0){
                        for (int n=0; n<length;n++){
                            if ( ( (W.get(n) == ((pow2r - w2)%pow2r)) && (FK.get(n) ==1) ) || w2==0 ) {
                                if (w1!=0) result.set(m, (byte) 1);
                                if (w2!=0) result.set(n, (byte) 1);
                                return result.tobyte();
                            }
                        }
                    }
                }
                
            }
        }
        
        return null;
    }
    
    /**
     * Extract method
     * Return the information extracted
     */
    public static byte[] extract(byte[] Fi) throws MatrixInputException{
        int length = Fi.length;
        
        int r = embedBitLength(length);
        int pow2r = (int) Math.pow(2, r);
        
        Vector K = key(length);
        Vector F = new Vector(Fi);
        Vector W = new Vector(length);
        W.populateCPT(r);
        
        /* Step 1: */
        Vector FK = F.add(K);
        
        /* Step 2: */
        int s = FK.pairWiseMultiply(W).sum() % (int) pow2r;
        
        /* Return s as a binary array with length r */
        return Codes.int2Bin(s,r);
        
    }
}
