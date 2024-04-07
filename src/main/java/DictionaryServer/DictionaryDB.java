package DictionaryServer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class DictionaryDB {
    static HashMap<String, ArrayList<String>> dictionary = new HashMap<String, ArrayList<String>>();
    final static String filePath = "src/main/java/DictionaryServer/dic.txt";


    public static void updateDictionary(HashMap<String, ArrayList<String>> dictionary) throws IOException {
        File file = new File(filePath);
        BufferedWriter writer = null;;
        try{
            writer = new BufferedWriter(new FileWriter(file));

            for(Map.Entry<String, ArrayList<String>> entry : dictionary.entrySet()){
                writer.write( entry.getKey() + " ");
                for(int i = 0 ; i < entry.getValue().size() ; i++)
                {
                    writer.write(entry.getValue().get(i));
                    if(i != entry.getValue().size()-1)
                    {
                        writer.write(";");
                    }
                    else {
                        writer.write("\n");
                    }
                }
            }writer.flush();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            assert writer != null;
            writer.close();
        }
    }
    public static HashMap<String, ArrayList<String>> loadDictionary() throws IOException {
        File file = new File(filePath);
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            String word = scan.next().trim();
            String definition = scan.nextLine().trim();
            ArrayList<String> defs = new ArrayList<String>(Arrays.asList(definition.split(";")));
            dictionary.put(word, defs);
        }scan.close();
        return dictionary;
    }



}