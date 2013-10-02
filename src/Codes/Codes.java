/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Codes;

import matrix.Vector;

/**
 *
 * @author tebaomang
 */
public class Codes {
    
    /* Return the $offset-th part length $range of info */
    public static byte[] stripByte(int offset, int range, byte[] info){
        byte[] result = new byte[range];
        System.arraycopy(info, offset*range, result, 0, range);
        return result;
    }
    
    /* Extend a binary string able to strip in to equal $infoBlockLeng-block */
    public static byte[] extendByte(byte[] info, int infoBlockLength, int holderBlockLength){
        
        /* Handle: Info is small */
        int lack = infoBlockLength-info.length;
        
        /* Handle: Number of Info-block is small */
        if (info.length>infoBlockLength) lack = infoBlockLength-info.length%infoBlockLength;
        
        /* REMOVED: Handle cut off bits - no longer needed since handled before start embeding */
        // if (info.length/infoBlockLength>holderLength/holderBlockLength) lack = (holderLength/holderBlockLength)*infoBlockLength-info.length;
        
        /* Extend info */
        byte[] result = new byte[info.length+lack];
        
        /* Copy info into output */
        if(info.length<=result.length)
            System.arraycopy(info, 0, result, 0, info.length);
        
        /* REMOVED: Handle cut off bits - no longer needed since handled before start embeding */
        //else System.arraycopy(info, 0, result, 0, result.length);
        
        /* Fill the tail with 0 */
        for(int i=info.length; i<result.length;i++) result[i]=0;
        
        return result;
    }
    
    public static int extendByteSize(int originalSize, int infoBlockLength, int holderBlockLength){
        /* Handle: Info is small */
        int lack = infoBlockLength-originalSize;
        
        /* Handle: Number of Info-block is small */
        if (originalSize>infoBlockLength) lack = infoBlockLength-originalSize%infoBlockLength;
        
        return originalSize+lack;
    }
    
    /* CONVERTER: binary array to integer vaule */
    public static int bin2Int(byte[] b){
        int result = 0;
        for (int i=0;i<b.length;i++){
            if (b[i]==1)
                result = result + (int) Math.pow(2, b.length-1-i);
        }
        return result;
    }
    
    /* CONVERTER: integer vaule to binary array with fix length */
    public static byte[] int2Bin (int number, int length) {
        
        String baseDigits = "01";
        
        /* Handle number 0 */
        String result = number == 0 ? "0" : "";
        
        while(number != 0) {
            int mod = number % 2;
            result = baseDigits.substring(mod, mod + 1) + result;
            number = number / 2;
        }
        
        /* Fill head with 0 if too short
         * Should have a handler for the case where the length inputted is 
         * too small -> throws an exception but in this particular program,
         * it's not needed.
         */
        while(result.length()<length) result = "0"+result;
        
        /* Use the CONVERTER of Vector class */
        return new Vector(result).tobyte();
    }
    
    /* TRASH BIN */
    
    /* Display a bytes array - for debugging only*/
    public static void displayByte(byte[] b){
        for(int i=0;i<b.length;i++) System.out.print(b[i]);
    }
}
