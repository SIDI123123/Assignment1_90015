package DictionaryServer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class DictionaryDB {
    static HashMap<String, ArrayList<String>> dictionary = new HashMap<String, ArrayList<String>>();
    final static String filePath = "src/main/java/DictionaryServer/dic.txt";

    public DictionaryDB() {
    }

    public static void updateDictionary(HashMap<String, ArrayList<String>> dictionary) throws IOException {
        File file = new File(filePath);
        BufferedWriter bf = null;;
        try{
            bf = new BufferedWriter(new FileWriter(file));

            for(Map.Entry<String, ArrayList<String>> entry : dictionary.entrySet()){
                bf.write( entry.getKey() + " ");
                for(int i = 0 ; i < entry.getValue().size() ; i++)
                {
                    bf.write(entry.getValue().get(i));
                    if(i != entry.getValue().size()-1)
                    {
                        bf.write(";");
                    }
                    else {
                        bf.write("\n");
                    }
                }
            }bf.flush();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            assert bf != null;
            bf.close();
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

//    public HashMap<String, String> getDictionary() throws IOException {
//        HashMap<String, String> dictionary = loadDictionary();
//        return dictionary;
//    }


}