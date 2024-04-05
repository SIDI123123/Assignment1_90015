package DictionaryServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
public class Server {
    protected static HashMap<String, String> dictionary;
    protected static int Port;
    protected static ServerSocket server;
    protected static Socket client;
    protected static String result;
    protected static DictionaryDB db;

    // initialize the server by creating socket and download dictionary file
    public static void main(String[] args) {
        int i = 0;
        try {
            System.out.println(args.length);
            if (args.length != 1) {
                System.out.println("Insert port number");
                System.exit(1);
            } else {
                Port = Integer.parseInt(args[0]);
                db = new DictionaryDB();
                dictionary = db.loadDictionary();
                server = new ServerSocket(Port);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Port error, insert valid number");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            while (true) {
                System.out.println("Server port " + Port + " waiting for a client" );
                client = server.accept();
                i++;
                Thread thread = new Thread(()-> serve(client));
                thread.start();
                System.out.println("Client no." + i + "Connected");
            }
        }catch (Exception e) {
            e.getStackTrace();
        }

    }

    // receive clients' request and send a response to clients
    public static void serve(Socket client) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
            try {
                String request = input.readLine();
                while(request != null && request.length() >=2) {
                    String command = request.substring(0,1);
                    String word = request.substring(1);
                    request = "";
                    switch (command) {
                        case "A":
                            int def_i = word.indexOf("F");
                            String def = word.substring(def_i + 1);
                            word = word.substring(0, def_i);
                            result = add(word, def);
                            break;
                        case "D":
                            result = delete(word);
                            break;
                        case "Q":
                            result = query(word);
                            break;
                    }output.write(result + "\n");
                    output.flush();
                    System.out.println("Result (" + result + ") sent >> " + client.getRemoteSocketAddress() + "\n");
                }
            }catch (SocketException e) {
                System.out.println("Processing Error >> " + client.getRemoteSocketAddress());
            }
            System.out.println("Client disconnected >> " + client.getRemoteSocketAddress());
            output.close();
            input.close();
            client.close();
        } catch (IOException e) {
            System.out.println("Error >> " + e.getMessage() + "\n");
        } finally {
            if(client.isConnected()){
                try {
                    db.updateDictionary(dictionary);
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // add a word and a definition to a dictionary file
    public static synchronized String add(String word, String def) {
        try{
            if(dictionary.containsKey(word)){
                result = word + " already exist, you cannot add";
            }else {
                dictionary.put(word, def);
                result = word + " is successfully added with meaning "+ def ;
            }
        }catch(NullPointerException e1) {
            try {
                dictionary.put(word, def); //modify it
                result = word + " is successfully added with meaning "+ def;
            } catch(Exception e2) {
                e2.getStackTrace();
            }
        } catch (Exception e3) {
            result = "Fail to add";
            e3.printStackTrace();
        }
        return result;
    }
    // delete a word from a dictionary file
    public static synchronized String delete(String word) {
        try{
            if(dictionary.containsKey(word)) {
                dictionary.remove(word);
                result = word + "is successfully deleted";
            }
        }catch(NullPointerException e1){
            result = "The word "+word +" does not exists in dictionary, you cannot delete";
        }catch(Exception e2){
            result = "Fail to add";
            e2.printStackTrace();
        }
        return result;
    }
    // query a word
    public static synchronized String query(String word) {
        try{
            if(dictionary.containsKey(word)){
                result = word + " is successfully searched.  Definition>>" +dictionary.get(word);
            }else result = "The word "+word +" does not exist in dictionary, sorry";
        }catch(NullPointerException e1){
            result = "The word "+word +" does not exist in dictionary, you cannot query";
        }catch(Exception e2){
            result = "Fail to add";
            e2.printStackTrace();
        }return result;
    }
}
