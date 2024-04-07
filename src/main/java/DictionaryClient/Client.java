package DictionaryClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Gets the <server-address> and <server-port>.
 * Invokes the client's user interface and gets
 * invoked when a button is pressed in the interface,
 * requests server on the basis of the press and gets
 * back the result to display them.
 *
 */
public class Client
{
    static ClientGUI objGUI = new ClientGUI();
    static BufferedWriter writer;
    static BufferedReader reader;
    static String ip;
    static int port;
    static Socket socket;

    static String errorMessage = "";

    public static void main(String []args)
    {
        try
        {
            ip = args[0];
            port = Integer.parseInt(args[1]);

            objGUI.ClientWindow();
            connect();
//            objGUI.getIP(ip);
//            objGUI.getPort(port);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Please enter the 'Server address' followed by the 'Server port'.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void connect(){
        try
        {
            socket = new Socket(ip, port);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));

        } catch (UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
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

            writer.write(data.toString());
            writer.newLine();
            writer.flush();

            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
//            JSONObject addObj = new JSONObject(addResult);
//            displayText = addObj.getString("display");

            //Disconnecting
//            reader.close();
//            writer.close();
//            socket.close();
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (JSONException e)
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
//            JSONObject addObj = new JSONObject(addResult);
//            displayText = addObj.getString("display");

            //Disconnecting
//            reader.close();
//            writer.close();
//            socket.close();
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (JSONException e)
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
//            JSONObject queryObj = new JSONObject(queryResult);
//            displayText = queryObj.getString("display");

            //Disconnecting
//            reader.close();
//            writer.close();
//            socket.close();
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (JSONException e)
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
//            JSONObject queryObj = new JSONObject(removeResult);
//            displayText = queryObj.getString("display");

            //Disconnecting
//            reader.close();
//            writer.close();
//            socket.close();

        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (JSONException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot connect to Server at Address: "+ip+" , Port: "+port, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
//        return displayText;
        return removeResult;
    }
}