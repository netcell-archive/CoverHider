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
public class CPTE {
    
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
        return (int) Math.floor(Math.log(blockLength)/Math.log(2))+1;
    }
    
    /* Get a key */
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
    
    public static int mod(int a, int M){
        if(a>=0&&a<M) return a;
        if(a>=M) return a%M;
        if(a<0&&a>=-M) return M+a;
        if(a<-M)
            while(a<0)
                a=a+M;
        return a;
    }
    
    public static int S(int r, int a, Vector W, Vector FK){
        int length = FK.length();
        int pow2r = (int) Math.pow(2, r);
        int pow2r1 = (int) Math.pow(2, r+1);
        a = mod(a,pow2r1);
        if (a>=1 && a<pow2r)
            for (int i=0;i<length;i++)
                if (FK.get(i)==0 && W.get(i)==a) return i;
        if (a>pow2r && a<=pow2r1-1)
            for (int i=0;i<length;i++)
                if (FK.get(i)==1 && W.get(i)==pow2r1-a) return i;
        if (a==pow2r)
            for (int i=0;i<length;i++)
                if (W.get(i)==a) return i;
        return -1;
    }
    
    /**
     * Embed method
     * Return the changes needed
     */
    public static byte[] embedChanges(byte[] Fi, byte[] info) throws MatrixInputException{
        
        int length = Fi.length;
        
        Vector result = new Vector(length);
        
        int r = embedBitLength(length)-1;
        int pow2r = (int) Math.pow(2, r);
        int pow2r1 = (int) Math.pow(2, r+1);
        
        if (info.length!=r+1) throw new MatrixInputException();
        
        Vector K = key(length);
        Vector F = new Vector(Fi);
        Vector W = new Vector(length);
        W.populateCPTE(r); /* Fill the weight matrix*/
        
        /* Step 1: */
        Vector FK = F.add(K);
        
        /* Step 2: */
        int s = mod(FK.pairWiseMultiply(W).sum(), pow2r1);
        
        /* Step 3: */
        int d = mod(Codes.bin2Int(info) - s,pow2r1);
        
        /* Step 4: */
        
        /* No changes needed: d=0 */
        if (d==0) return result.tobyte();
        
        if (d!=0){
            
            /* h=0 or h=1 */
            int flip = S(r,d,W,FK);
            if (flip!=-1){
                result.set(flip, (byte) 1);
                return result.tobyte();
            }
            /* h>1 */
            for (int h=2;h<pow2r1;h++){
                int flip1 = S(r,h*d,W,FK);
                int flip2 = S(r,d-h*d,W,FK);
                if (flip1!=-1&&flip2!=-1){
                    result.set(flip1, (byte) 1);
                    result.set(flip2, (byte) 1);
                    return result.tobyte();
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
        
        int r = embedBitLength(length)-1;
        int pow2r1 = (int) Math.pow(2, r+1);
        
        Vector K = key(length);
        Vector F = new Vector(Fi);
        Vector W = new Vector(length);
        W.populateCPTE(r);
        
        /* Step 1: */
        Vector FK = F.add(K);
        
        /* Step 2: */
        int s = FK.pairWiseMultiply(W).sum() % (int) pow2r1;
        
        /* Return s as a binary array with length r */
        return Codes.int2Bin(s,r+1);
        
    }
}
