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
public class COV295{
    
    /* Parity check matrix*/
    private static Vector[] H = {new Vector("00011"),
                                 new Vector("00010"),
                                 new Vector("00101"),
                                 new Vector("01000"),
                                 new Vector("01100"),
                                 new Vector("10000"),
                                 new Vector("10100"),
                                 new Vector("11000"),
                                 new Vector("11100")};
    
    /* Syndrom H*e dictionary*/
    private static Vector[] Hi = {new Vector("00001"),
                                  new Vector("00110"),
                                  new Vector("01011"),
                                  new Vector("01111"),
                                  new Vector("10011"),
                                  new Vector("10111"),
                                  new Vector("11011"),
                                  new Vector("11111"),
                                  new Vector("00011"),
                                  new Vector("00111"),
                                  new Vector("01010"),
                                  new Vector("01110"),
                                  new Vector("10010"),
                                  new Vector("10110"),
                                  new Vector("11010"),
                                  new Vector("11110"),
                                  new Vector("00010"),
                                  new Vector("01101"),
                                  new Vector("01001"),
                                  new Vector("10101"),
                                  new Vector("10001"),
                                  new Vector("11101"),
                                  new Vector("11001"),
                                  new Vector("00101"),
                                  new Vector("00100"),
                                  new Vector("11000"),
                                  new Vector("11100"),
                                  new Vector("10000"),
                                  new Vector("10100"),
                                  new Vector("01000"),
                                  new Vector("11100"),
                                  new Vector("11000"),
                                  new Vector("10100"),
                                  new Vector("10000"),
                                  new Vector("01100"),
                                  new Vector("00100"),
                                  new Vector("01000"),
                                  new Vector("01100"),
                                  new Vector("10000"),
                                  new Vector("01100"),
                                  new Vector("01000"),
                                  new Vector("10100"),
                                  new Vector("00100"),
                                  new Vector("11000"),
                                  new Vector("11100"),
                                  new Vector("00000")};
    
    /* Noise e pool*/
    private static Vector[] iH = {new Vector("110000000"),
                                  new Vector("101000000"),
                                  new Vector("100100000"),
                                  new Vector("100010000"),
                                  new Vector("100001000"),
                                  new Vector("100000100"),
                                  new Vector("100000010"),
                                  new Vector("100000001"),
                                  new Vector("100000000"),
                                  new Vector("011000000"),
                                  new Vector("010100000"),
                                  new Vector("010010000"),
                                  new Vector("010001000"),
                                  new Vector("010000100"),
                                  new Vector("010000010"),
                                  new Vector("010000001"),
                                  new Vector("010000000"),
                                  new Vector("001100000"),
                                  new Vector("001010000"),
                                  new Vector("001001000"),
                                  new Vector("001000100"),
                                  new Vector("001000010"),
                                  new Vector("001000001"),
                                  new Vector("001000000"),
                                  new Vector("000110000"),
                                  new Vector("000101000"),
                                  new Vector("000100100"),
                                  new Vector("000100010"),
                                  new Vector("000100001"),
                                  new Vector("000100000"),
                                  new Vector("000011000"),
                                  new Vector("000010100"),
                                  new Vector("000010010"),
                                  new Vector("000010001"),
                                  new Vector("000010000"),
                                  new Vector("000001100"),
                                  new Vector("000001010"),
                                  new Vector("000001001"),
                                  new Vector("000001000"),
                                  new Vector("000000110"),
                                  new Vector("000000101"),
                                  new Vector("000000100"),
                                  new Vector("000000011"),
                                  new Vector("000000010"),
                                  new Vector("000000001"),
                                  new Vector("000000000")};
    
    public static  void main(String[] args){
        
        for (Vector a : Hi){
            int count = 0;
            for (Vector b : Hi){
                if (b.isEqual(a)){
                    count++;
                }
            }
            System.out.println(a.toString()+":"+count);
        }
    }    
    /**
     * Embed method
     * Not important, only for debug
     * Written separately to test and minimize the risk
     */
    public static byte[] embed(byte[] plain, byte[] info) throws MatrixInputException{
        
        Vector deltaY = new Vector(5);
        
        for(int i=0;i<9;i++)
            if (plain[i]==1) deltaY = deltaY.add(H[i]);
        System.out.println(deltaY.toString());
        deltaY = deltaY.add(new Vector(info));
        System.out.println(deltaY.toString());
        
        return new Vector(plain).add(deltaY.search(Hi,iH)).tobyte();
    }
    
    /**
     * Embed method
     * Return the changes needed
     */
    public static byte[] embedChanges(byte[] plain, byte[] info) throws MatrixInputException{
        
        Vector deltaY = new Vector(5);
        
        /* Calculate H*x */
        for(int i=0;i<9;i++)
            if (plain[i]==1) deltaY = deltaY.add(H[i]);
        System.out.println(deltaY.toString());
        /* Calculate y'=y-H*x */
        deltaY = deltaY.add(new Vector(info));
        System.out.println(deltaY.toString());
        /* Find H^{-1}(y') */
        return deltaY.search(Hi,iH).tobyte();
    }
    
    /**
     * Extract method
     * Return the information extracted
     */
    public static byte[] extract(byte[] embed) throws MatrixInputException{
        
        Vector result = new Vector(5);
        
        /* Calculate H*x */
        for(int i=0;i<9;i++)
            if (embed[i]==1) result = result.add(H[i]);
        
        return result.tobyte();
    }
    
}
