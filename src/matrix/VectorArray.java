/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

import exception.MatrixInputException;

/**
 *
 * @author tebaomang
 */
public class VectorArray{
    
    private Vector[] vectorArray;
    
    public VectorArray (int numberOfRow, int numberOfCollumn){
        this.vectorArray = new Vector[numberOfRow];
        for (int i=0; i<numberOfRow;i++){
            this.vectorArray[i] = new Vector(numberOfCollumn);
        }
    }
    
    public VectorArray(int[] v, int elength){
        this.vectorArray = new Vector[v.length];
        for(int i=0;i<v.length;i++){
            this.vectorArray[i] = new Vector(v[i], elength);
        }
    }
    
    public VectorArray(String[] v){
        this.vectorArray = new Vector[v.length];
        int rowLength = v[0].length();
        for (int i=0; i<v.length;i++){
            if (v[i].length()!=rowLength);
            else {
                this.vectorArray[i] = new Vector(v[i]);
            }
        }
    }
    
    public VectorArray(String vs){
        Vector v = new Vector(vs);
        this.vectorArray = new Vector[v.length()];
        for(int i=0;i<v.length();i++){
            this.vectorArray[i]=new Vector(v.get(i));
        }
    }
    
    public VectorArray(Vector v){
        this.vectorArray = new Vector[v.length()];
        for(int i=0;i<v.length();i++){
            this.vectorArray[i]=new Vector(v.get(i));
        }
    }
    
    public VectorArray(Vector[] v){
        for (int i=0; i<v.length;i++){
            this.vectorArray[i] = new Vector(v[i]);
        }
    }
    
    public VectorArray minus(VectorArray that) throws MatrixInputException{
        if ((this.numberOfCollumns()!=that.numberOfCollumns())||this.numberOfRows()!=that.numberOfRows()) throw new MatrixInputException("Not matched matrices dimensions");
        VectorArray result = new VectorArray(this.numberOfRows(),this.numberOfCollumns());
        for (int i=0; i<result.numberOfRows();i++)
            result.set(i, this.getRow(i).add(that.getRow(i)));
        return result;
        
    }
    
    public VectorArray multiply(VectorArray that) throws MatrixInputException{
        if (this.numberOfCollumns()!=that.numberOfRows()) throw new MatrixInputException("Not matched matrices dimensions");
        VectorArray result = new VectorArray(this.numberOfRows(),that.numberOfCollumns());
        int cell;
        for (int resultRow=0; resultRow<result.numberOfRows();resultRow++)
            for (int resultCollumn=0; resultCollumn<result.numberOfCollumns(); resultCollumn++){
                cell = 0;
                for (int i=0; i<this.numberOfCollumns(); i++) 
                    cell=(cell+this.get(resultRow, i)*that.get(i, resultCollumn));
                result.set(resultRow, resultCollumn, cell%2);
        }
        return result;
    }
    
    public Vector pairWiseMultiply(Vector that) throws MatrixInputException{
        Vector result = new Vector ( vectorArray[0].length() );
        for(int i=0;i<that.length();i++){
            if ( that.get(i)==1 ) {
                result = result.add(vectorArray[i]);
            }
        }
        return result;
    }
    
    public Vector getVector(int i){
        return vectorArray[i];
    }
    
    public VectorArray T(){
        VectorArray result = new VectorArray(this.numberOfCollumns(),this.numberOfRows());
        for (int resultRow=0; resultRow<result.numberOfRows();resultRow++)
            for (int resultCollumn=0; resultCollumn<result.numberOfCollumns(); resultCollumn++)
                result.set(resultRow, resultCollumn, this.get(resultCollumn,resultRow));
        return result;
    }
    
    public void set(int row, int collumn, byte b){
        this.vectorArray[row].set(collumn,b);
    }
    
    public void set(int row, int collumn, int b){
        this.vectorArray[row].set(collumn,(byte)b);
    }
    
    public void set(int row, Vector v){
        this.vectorArray[row]=new Vector(v);
    }
    
    public Vector getRow(int row){
        return new Vector(this.vectorArray[row]);
    }
    
    public byte get(int row, int collumn){
        return this.vectorArray[row].get(collumn);
    }
    
    public int numberOfCollumns(){
        return vectorArray[0].length();
    }
    
    public int numberOfRows(){
        return vectorArray.length;
    }
    
    public Vector toVector() throws MatrixInputException{
        if (this.numberOfRows()==1) return this.getRow(0);
        else if (this.numberOfCollumns()==1) return this.T().getRow(0);
        else throw new MatrixInputException();
    }
    
    public String toString(){
        String result = "";
        result = result+this.vectorArray[0].toString();
        for(int i=1;i<this.numberOfRows();i++){
            result = result+"\n"+this.vectorArray[i].toString();
        }
        return result;
    }
}
