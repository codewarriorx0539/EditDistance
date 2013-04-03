
package editdistance;

import java.util.concurrent.ArrayBlockingQueue;

public class EditProcessor implements Runnable 
{
    // Normally floats but I scaled by x 10 and using ints... less space and faster
    static int INSERT_COST = 10;        // Move Right 1.0
    static int DELETION_COST = 12;      // Move Down 1.2
    static int SUBSTITUTION_COST = 15;  // Move Diagonal 1.5
    
    static int MAX_COMPARE_LEN = 25;

    int table[][];
    
    ArrayBlockingQueue<String> q;
    
    String masterString;
    int masterStringLen;
    
    String compareString;
    int compareStringLen;
    
    public EditProcessor(ArrayBlockingQueue<String> q, String masterString)
    {
        this.q = q;
        this.masterString = masterString;
        masterStringLen = this.masterString.length();
        
        table = new int[masterStringLen + 1 ][MAX_COMPARE_LEN + 1];
        
        for(int i =0; i < masterStringLen + 1; i++)
        {
            table[i][0] = INSERT_COST * i;
        }
        
        for(int j =0; j < MAX_COMPARE_LEN + 1; j++)
        {
            table[0][j] = DELETION_COST * j;
        }
    }
    
    private void printGrid()
    {
        for(int i = 0; i < masterStringLen + 1; i++)
        {
            for(int j = 0; j < compareStringLen + 1; j++)
            {
                System.out.printf("%4d\t", table[i][j]);
            }
            System.out.println();
        }    
    }
    
    @Override
    public void run()
    {
        float dist;
        
        while(true)
        {
           compareString = q.poll();
       
           if(compareString != null) 
           {
               
            compareStringLen = compareString.length();
            
            for(int i = 1; i < masterStringLen + 1; i++)
            {
                for(int j = 1; j < compareStringLen + 1; j++)
                {
                    if(masterString.charAt(i - 1) == compareString.charAt(j - 1))
                    {
                        table[i][j] = Math.min(table[i -1 ][j -1], Math.min( table[i ][j -1] + DELETION_COST , table[i -1 ][j] + INSERT_COST));
                    }
                    else
                    {
                        table[i][j] = Math.min(table[i -1 ][j -1] + SUBSTITUTION_COST, Math.min( table[i ][j -1] + DELETION_COST  , table[i -1 ][j] + INSERT_COST));        
                    }
                }
            }
            
            dist = (float) table[masterStringLen][compareStringLen];
            dist = dist/10.0f;
            
            System.out.println(" The Compare String was: " + compareString + "  Distance is: " + dist);
            
           }
        }
    }
    
    public static void main(String[] args) 
    {
        int NUM_THREADS = 2;
        int QUEUE_SIZE = 1000;
        
        int TEST_SIZE = 100;
        
        int COMPARE_STRING_MIN = 5;
        int COMPARE_STRING_MAX = 15;
        int num;
        
        String masterString = "Hello World";
        masterString = masterString.toLowerCase();
        
        ArrayBlockingQueue<String> q = new ArrayBlockingQueue(QUEUE_SIZE);
        
        for(int i = 0 ; i < NUM_THREADS; i++)
        {
            new Thread(new EditProcessor(q , masterString)).start();
        }
       
        for(int i = 0; i < TEST_SIZE; i++)
        {
            num = COMPARE_STRING_MIN + (int)(Math.random() * ((COMPARE_STRING_MAX - COMPARE_STRING_MIN) + 1));
            q.offer(new RandomString(num).getString().toLowerCase());
        }
        
        q.offer("Hello World");
        q.offer("hhello world");
        q.offer("ello world");
        q.offer("hello world");
        
    }
    
}
