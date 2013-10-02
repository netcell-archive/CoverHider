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
public class Hamming{
    
    private static Vector[] H = {new Vector("001"),
                                 new Vector("010"),
                                 new Vector("011"),
                                 new Vector("100"),
                                 new Vector("101"),
                                 new Vector("110"),
                                 new Vector("111"),
                                 new Vector("000")};
    
    private static Vector[] iH = {new Vector("1000000"),
                                  new Vector("0100000"),
                                  new Vector("0010000"),
                                  new Vector("0001000"),
                                  new Vector("0000100"),
                                  new Vector("0000010"),
                                  new Vector("0000001"),
                                  new Vector("0000000")};
    
    public static byte[] embe(byte[] plain, byte[] info) throws MatrixInputException{
        Vector deltaY = new Vector(3);
        
        for(int i=0;i<7;i++)
            if (plain[i]==1) deltaY = deltaY.add(H[i]);
        deltaY = deltaY.add(new Vector(info));
        
        System.out.println(new Vector(info).toString());
        System.out.println(new Vector(deltaY).toString());
        System.out.println(deltaY.search(H,iH).toString());
        
        return new Vector(plain).add(deltaY.search(H,iH)).tobyte();
    }
    
    public static int plainSize(int infoSize){
        return (int) (Math.pow(2,infoSize)-1);
    }
    
    public static int infoSize(int plainSize){
        return (int) ( Math.log(plainSize+1)/Math.log(2) );
    }
    
    public static byte[] embeU(byte[] plain, byte[] info) throws MatrixInputException{
        
        int infSize = info.length;
        int plainSize = plain.length;
        
        if ((int) Math.pow(2,infSize)-1!=plainSize) throw new MatrixInputException();
        
        Vector deltaY = new Vector(infSize);
        
        for(int i=0;i<plainSize;i++)
            if (plain[i]==1){
                byte[] Hi = Codes.int2Bin(i+1, infSize);
                deltaY = deltaY.add(new Vector(Hi));
            }
        deltaY = deltaY.add(new Vector(info));
        
        int position = Codes.bin2Int(deltaY.tobyte());
        Vector result = new Vector(plainSize);
        if (position!=0) result.set(position-1, (byte) 1);
        
        return new Vector(plain).add(result).tobyte();
    }
    
    public static byte[] embedChanges(byte[] plain, byte[] info) throws MatrixInputException{
        Vector deltaY = new Vector(3);
        for(int i=0;i<7;i++)
            if (plain[i]==1) deltaY = deltaY.add(H[i]);
        deltaY = deltaY.add(new Vector(info));
        return deltaY.search(H,iH).tobyte();
    }
    
    public static byte[] embedChangesU(byte[] plain, byte[] info) throws MatrixInputException{
        
        int infSize = info.length;
        int plainSize = plain.length;
        
        if ((int) Math.pow(2,infSize)-1!=plainSize) throw new MatrixInputException();
        
        Vector deltaY = new Vector(infSize);
        
        for(int i=0;i<plainSize;i++)
            if (plain[i]==1){
                byte[] Hi = Codes.int2Bin(i+1, infSize);
                deltaY = deltaY.add(new Vector(Hi));
            }
        deltaY = deltaY.add(new Vector(info));
        
        int position = Codes.bin2Int(deltaY.tobyte());
        Vector result = new Vector(plainSize);
        if (position!=0) result.set(position-1, (byte) 1);
        
        return result.tobyte();
    }
    
    public static byte[] extract(byte[] embed) throws MatrixInputException{
        Vector result = new Vector(3);
        for(int i=0;i<7;i++)
            if (embed[i]==1) result = result.add(H[i]);
        return result.tobyte();
    }
    
    public static byte[] extractU(byte[] embed) throws MatrixInputException{
        int plainSize = embed.length;
        int infSize = infoSize(plainSize);
        Vector result = new Vector(infSize);
        for(int i=0;i<plainSize;i++)
            if (embed[i]==1){
                byte[] Hi = Codes.int2Bin(i+1, infSize);
                result = result.add(new Vector(Hi));
            }
        return result.tobyte();
    }
    
}
