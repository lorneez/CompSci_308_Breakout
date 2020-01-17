package breakout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestReader {
    public static void main(String[] args) throws FileNotFoundException {





        try {
            String i;
            BufferedReader r=new BufferedReader(new FileReader("src/breakout/level1.txt"));
            String first = r.readLine();
            System.out.println(first);
            String second = r.readLine();
            System.out.println(second);
            while(!((i=r.readLine()).equals("STOP"))) {
                System.out.println(i);
            }
            r.close();
        } catch(IOException ie) {
            ie.printStackTrace();
            System.out.println("Fail");
        }



    }
}
