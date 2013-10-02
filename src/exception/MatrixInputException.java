/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author tebaomang
 */
public class MatrixInputException extends Exception{
   public MatrixInputException() {}
   public MatrixInputException(String gripe)
   {
      super(gripe);
   }
}
