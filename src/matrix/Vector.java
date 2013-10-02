/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

import Codes.Codes;
import exception.MatrixInputException;

/**
 *
 * @author tebaomang
 */
public class Vector {
    
    private byte[] vector;
    private static Vector WRONG = new Vector("-1");
    public static final int NONE = 0;
    
    public Vector(int length){
        this.vector = new byte[length];
        for (int i = 0; i<length;i++){
            this.vector[i]=(byte)0;
        }
    }
    
    public Vector(int i, int length){
        this.vector = Codes.int2Bin ( i, length);
    }
    
    public Vector(String v){
        this.vector = new byte[v.length()];
        for (int i = 0; i<v.length();i++){
            this.vector[i]=(byte)(v.charAt(i)-48);
        }
    }
    
    public Vector(Vector v){
        this.vector = new byte[v.length()];
        System.arraycopy(v.vector, 0, this.vector, 0, v.length());
    }
    
    public Vector(byte[] v){
        this.vector = new byte[v.length];
        System.arraycopy(v, 0, this.vector, 0, v.length);
    }
    
    public Vector(byte v){
        this.vector = new byte[1];
        this.vector[0]=v;
    }
    
    public Vector flip(int position){
        Vector result = new Vector(this);
        result.vector[position]=(byte)(1-result.vector[position]);
        return result;
    }
    
    public byte get(int position){
        return this.vector[position];
    }
    
    public void set(int position, byte b){
        this.vector[position]=b;
    }
    
    public int length(){
        return this.vector.length;
    }
    
    @Override
    public String toString(){
        String result = "";
        for(int i=0;i<this.length();i++){
            result += this.get(i);
        }
        return result;
    }
    
    public boolean isEqual(Vector that){
        for(int i=0;i<vector.length;i++) {
            if(this.vector[i]!=that.vector[i]) {
                return false;
            }
        }
        return true;
    }
    
    public Vector search(Vector[] index, Vector[] map){
        for(int i=0;i<index.length;i++) {
            if(this.isEqual(index[i])) {
                return map[i];
            }
        }
        return WRONG;
    }
    
    public Vector add(Vector that) throws MatrixInputException{
        if (this.length()!=that.length()) {
            throw new MatrixInputException();
        }
        Vector result = new Vector(length());
        for (int i=0; i<length();i++) {
            result.set(i, (byte)((this.get(i)+that.get(i)+2)%2));
        }
        return result;
    }
    
    public Vector pairWiseMultiply(Vector that) throws MatrixInputException{
        if (this.length()!=that.length()) {
            throw new MatrixInputException();
        }
        Vector result = new Vector(length());
        for (int i=0;i<length();i++) {
            result.set(i, (byte)(this.get(i)*that.get(i)));
        }
        return result;
    }
    
    public int sum(){
        int result = 0;
        for (int i=0;i<length();i++) {
            result += get(i);
        }
        return result;
    }
    
    public byte[] tobyte(){
        byte[] result = new byte[vector.length];
        System.arraycopy(vector, 0, result, 0, vector.length);
        return result;
    }
    
    public void populateCPT(int r){
        
        int range = (int) (Math.pow(2, r)-1);
        for(int i=0;i<range;i++) {
            set(i,(byte)(i+1));
        }
        byte k = 1;
        for(int i=range;i<length();i++){
            if (k>range) {
                k=1;
            }
            set(i,k);
            k++;
        }
    }
    
    public void populateCPTE(int r){
        int range = (int) (Math.pow(2, r));
        for(int i=0;i<range;i++) {
            set(i,(byte)(i+1));
        }
        byte k = 1;
        for(int i=range;i<length();i++){
            if (k>range) {
                k=1;
            }
            set(i,k);
            k++;
        }
    }
    
}
