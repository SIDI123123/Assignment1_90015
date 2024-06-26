/**
 * Zicheng Jin
 * 1511951
 */
package DictionaryClient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;

public class Client
{
    private static BufferedWriter writer;
    private static BufferedReader reader;
    private static String ip;
    private static int port;
    static Socket socket;


    static String errorMessage = "";

    public static void main(String []args)
    {

        if (args.length != 2)
        {
            JOptionPane.showMessageDialog(null, "Please enter the 'Server address' followed by the 'Server port'.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        ip = args[0];
        port = Integer.parseInt(args[1]);

        ClientGUI.ClientWindow();
        connect();

    }
    public static void connect(){
        try
        {
            socket = new Socket(ip, port);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
        } catch (IOException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    public void disconnect()
    {
        try {
            JSONObject data = new JSONObject();
            String type = "exit";
            data.put("queryType", type);
            data.put("userWord","");
            data.put("meaning", "");
            if(socket!=null && !socket.isClosed())
            {
                writer.write(data.toString());
                writer.newLine();
                writer.flush();

                writer.close();
                reader.close();
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String add(String word, String meaning)
    {
        String addResult ="";
        try{

            //to send data to the server
            JSONObject data = new JSONObject();
            String type = "add";
            data.put("queryType", type);
            data.put("userWord",word);
            data.put("meaning", meaning);

            writer.write(data.toString());
            writer.newLine();
            writer.flush();

            //to get results from the server
            addResult = reader.readLine();

        }
        catch (IOException | JSONException | NullPointerException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }

//        return displayText;
        return addResult;
    }

    public String update(String word, String meaning)
    {
        String updateResult ="";
        try{

            //to send data to the server
            JSONObject data = new JSONObject();
            String type = "update";
            data.put("queryType", type);
            data.put("userWord",word);
            data.put("meaning", meaning);

            writer.write(data.toString());
            writer.newLine();
            writer.flush();

            //to get results from the server
            updateResult = reader.readLine();

        }
        catch (IOException | JSONException | NullPointerException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }

//        return displayText;
        return updateResult;
    }


    public String query(String word, String meaning)
    {
        String queryResult ="";

        try {

            //to send data to the server
            JSONObject data = new JSONObject();
            String type = "query";
            data.put("queryType", type);
            data.put("userWord",word);
            data.put("meaning", meaning);

            writer.write(data.toString());
            writer.newLine();
            writer.flush();

            //to get results from the server
            queryResult = reader.readLine();

        }
        catch (IOException | JSONException | NullPointerException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
//        return displayText;
        return queryResult;
    }
    public String remove(String word, String meaning)
    {
        String removeResult ="";
        try {

            //to send data
            JSONObject data = new JSONObject();
            String type = "remove";
            data.put("queryType", type);
            data.put("userWord",word);
            data.put("meaning", meaning);

            writer.write(data.toString());
            writer.newLine();
            writer.flush();

            //to get results from the server
            removeResult = reader.readLine();


        }
        catch (IOException | JSONException | NullPointerException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
//        return displayText;
        return removeResult;
    }
}