package DictionaryServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import DictionaryClient.Client;
import org.json.JSONException;
import org.json.JSONObject;
public class Server {
    protected static HashMap<String, ArrayList<String>> dictionary;
    protected static int Port;
    protected static ServerSocket server;
    protected static Socket client;
    protected static String result;
    protected static DictionaryDB db;
    protected static final int maxWorkers = 2;

    // initialize the server by creating socket and download dictionary file
    public static void main(String[] args) {

        int i = 0;
        try {
            if (args.length != 2) {
                System.out.println("Insert port number and the path to the dictionary data.");
                System.exit(1);
            } else {
                Port = Integer.parseInt(args[0]);
                DictionaryDB.setPath(args[1]);
                dictionary = DictionaryDB.loadDictionary();
                server = new ServerSocket(Port);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Port error, insert valid number");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            Threadpool pool = new Threadpool(maxWorkers);
            while (true) {
                System.out.println("Server port " + Port + " waiting for a client" );

                client = server.accept();
                i++;
                System.out.println("Client no." + i + " Connected from "+ client.getRemoteSocketAddress());
//                Thread thread = new Thread(()-> serve(client, finalI));
//                thread.start();
                pool.execute(i, client);

            }
        }catch (Exception e) {
            e.getStackTrace();
        }

    }

    // receive clients' request and send a response to clients
    public static void serve(Socket client, int i) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
//            try {
            String request;
            JSONObject jsonobject;
            while((request = input.readLine()) != null) {
                jsonobject = new JSONObject(request);
//                System.out.println(jsonobject);
                String command = jsonobject.getString("queryType");
                String word = jsonobject.getString("userWord");
                String meaning = null;
//                    request = "";
                if (Objects.equals(command, "exit"))
                {
                    exit(input, output, client, i);
                    break;
                }
                switch (command) {
                    case "add":
                        meaning = jsonobject.getString("meaning");
                        result = add(word, meaning);
                        break;
                    case "remove":
                        result = delete(word);
                        break;
                    case "query":
                        result = query(word);
                        break;
                    case "update":
                        meaning = jsonobject.getString("meaning");
                        result = update(word, meaning);
                }output.write(result + "\n");
                output.flush();
                System.out.println("Result: \"" + result + "\" sent to " + client.getRemoteSocketAddress());
            }
        } catch (SocketException e) {
                System.out.println("Processing Error >> " + client.getRemoteSocketAddress());
        } catch (IOException e) {
            System.out.println("Error >> " + e.getMessage() + "\n");
        } finally {
            try {
                DictionaryDB.updateDictionary(dictionary);
                if(client.isConnected()){
                    client.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static synchronized void exit(BufferedReader input, BufferedWriter output, Socket client, int i)
    {
        System.out.println("Client no." + i + " disconnected from " + client.getRemoteSocketAddress());

        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // add a word and a definition to a dictionary file
    public static synchronized String add(String word, String def) {
        try{
            if(dictionary.containsKey(word)){
                result = "|"+word + " already exist, fail to add";
            }else {
                ArrayList<String> defs = new ArrayList<String>(Arrays.asList(def.split("\n")));
                dictionary.put(word, defs);
                result = "|"+word + " is successfully added with meanings: ;" ;
                for (int i = 0; i < defs.size(); i++)
                {
                    result = result + (i + 1) +". "+ defs.get(i) + ";";
                }
            }
        }catch (Exception e) {
            result = "|Fail to add";
            e.printStackTrace();
        }
        return result;
    }

    public static synchronized String update(String word, String def) {
        try{
            if(!dictionary.containsKey(word)){
                result = "|"+word + " doesn't exist, fail to update";
            }else {
                String[] defs = def.split("\n");
                result = "|"+word + " is successfully updated with meanings: ;" ;
                int number = 1;
                for (int i = 0; i < defs.length; i++)
                {
                    if (!dictionary.get(word).contains(defs[i]))
                    {
                        dictionary.get(word).add(defs[i]);
                        result = result + (number) +". "+ defs[i] + ";";
                        number += 1;
                    }

                }
            }
        }catch (Exception e) {
            result = "|Fail to add";
            e.printStackTrace();
        }
        return result;
    }

    // delete a word from a dictionary file
    public static synchronized String delete(String word) {
        try{
            if(dictionary.containsKey(word)) {
                dictionary.remove(word);
                result = "|"+word + " is successfully deleted";
            }
            else result = "|The word " + word + " does not exist in dictionary, fail to delete";
        }catch(Exception e){
            result = "|Fail to delete";
            e.printStackTrace();
        }
        return result;
    }
    // query a word
    public static synchronized String query(String word) {
        try{
            if(dictionary.containsKey(word)){
                result = "Definition: ;";
                for(int i = 0; i < dictionary.get(word).size(); i++)
                {
                    result = result + (i + 1) +". "+ dictionary.get(word).get(i) + ";";
                }
                result = result+"|"+word + " is successfully searched.";
            }else result = "None|The word "+word +" does not exist in dictionary, fail to query";
        }catch(Exception e){
            result = "|Fail to query";
            e.printStackTrace();
        }return result;
    }
}
