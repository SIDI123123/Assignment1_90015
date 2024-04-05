package DictionaryServer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Map;
public class DictionaryDB {
    static HashMap<String, String> dictionary = new HashMap<String, String>();
    final static String filePath = "src/main/java/DictionaryServer/dic.txt";

    public DictionaryDB() {
    }

    public static void updateDictionary(HashMap<String, String> dictionary) throws IOException {
        File file = new File(filePath);
        BufferedWriter bf = null;;
        try{
            bf = new BufferedWriter(new FileWriter(file));

            for(Map.Entry<String, String> entry : dictionary.entrySet()){
                bf.write( entry.getKey() + " " + entry.getValue() +"\n");
            }bf.flush();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                bf.close();
            }catch(Exception e){

            }
        }
    }
    public static HashMap<String, String> loadDictionary() throws IOException {
        File file = new File(filePath);
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            String word = scan.next().trim();
            String definition = scan.nextLine().trim();
            dictionary.put(word, definition);
        }scan.close();
        return dictionary;
    }

    public HashMap<String, String> getDictionary() throws IOException {
        HashMap<String, String> dictionary = loadDictionary();
        return dictionary;
    }


}