
package editdistance;

import java.util.Random;


public class RandomString 
{
    static final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();
    String str;
    
    public RandomString(int size)
    {
       StringBuilder sb = new StringBuilder( size );
       for( int i = 0; i < size; i++ ) 
       {
          sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
       }
       str = sb.toString();
    }
    
    public String getString()
    {
        return str;
    }
   
}
