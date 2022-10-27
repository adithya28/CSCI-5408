import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataDictionary {
    public static Map<String,String> getDataDictionary(String filePath) throws FileNotFoundException {
        File fp = new File(filePath);
        Scanner sc = new Scanner(fp);
        Map<String,String> result = new HashMap<>();

        while(sc.hasNextLine())
        {
            String currentPair=sc.nextLine();
            String [] pairs = currentPair.split(",");
            result.put(pairs[0],pairs[1]);

        }
    return result;


    }
    public static void main(String args[]) throws FileNotFoundException {
        String filePath="TableInfo.txt";
        System.out.println(getDataDictionary(filePath));
    }
}
