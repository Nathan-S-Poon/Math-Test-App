/*
TODO self reference
 */
package curtin.edu.mathtestapp;


public class InputException extends Exception
{
    public InputException(String message)
    {
        super(message);
    }
    public InputException(String message, Throwable cause)
    {
        super(message, cause);
    }


}