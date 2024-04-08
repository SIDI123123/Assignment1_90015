/**
 * Zicheng Jin
 * 1511951
 */
package DictionaryClient;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;


/**
 * Creates a graphical user interface on the
 * client side, invokes the client.java when
 * a button is pressed and displays results.
 *
 */
public class ClientGUI
{
    Client objClient = new Client();
    JFrame clientInterface;
    JTextField Word_textField;

    public static void ClientWindow(){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientGUI window = new ClientGUI();
                    window.clientInterface.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * Create the application.
     */
    public ClientGUI()
    {
        initialize();

    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        clientInterface = new JFrame();
        clientInterface.setTitle("Multi-threaded Dictionary Server");
        clientInterface.setBounds(100, 100, 550, 350);
//        clientInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientInterface.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                objClient.disconnect();
                System.exit(0);
            }
        });

        Word_textField = new JTextField();
        Word_textField.setBounds(80, 6, 445, 26);
        Word_textField.setColumns(10);

        JLabel lblWord = new JLabel("Word");
        lblWord.setBounds(15, 11, 32, 16);

        final JTextArea Meaning_textArea = new JTextArea();
        JScrollPane sp = new JScrollPane(Meaning_textArea);
        sp.setBounds(80,77,445,130);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        Meaning_textArea.setWrapStyleWord(true);
        Meaning_textArea.setLineWrap(true);
        Meaning_textArea.setBounds(80, 77, 445, 130);
//        clientInterface.getContentPane().add(sp);

        JLabel lblNewLabel = new JLabel("Meaning");
        lblNewLabel.setBounds(15, 80, 53, 16);

        final JTextArea Result_textArea = new JTextArea();
        JScrollPane sp1 = new JScrollPane(Result_textArea);
        sp1.setBounds(80, 219, 445, 80);
        sp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        Result_textArea.setLineWrap(true);
        Result_textArea.setWrapStyleWord(true);
        Result_textArea.setBounds(80, 219, 445, 80);
//        clientInterface.getContentPane().add(sp1);

        JLabel lblNewLabel_1 = new JLabel("Result");
        lblNewLabel_1.setBounds(15, 230, 39, 16);

        //Three buttons: Query, Insert and Remove
        JButton btnQuery = new JButton("Query");
        btnQuery.setBounds(80, 40, 85, 29);
        JButton btnAdd = new JButton("Add");
        btnAdd.setBounds(200, 40, 85, 29);
        JButton btnRemove = new JButton("Remove");
        btnRemove.setBounds(320, 40, 85, 29);
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(440,40,85,29);


        clientInterface.getContentPane().setLayout(null);
        clientInterface.getContentPane().add(lblNewLabel);
        clientInterface.getContentPane().add(lblWord);
        clientInterface.getContentPane().add(Word_textField);
//        clientInterface.getContentPane().add(Meaning_textArea);
//        clientInterface.getContentPane().add(Result_textArea);
        clientInterface.getContentPane().add(sp);
        clientInterface.getContentPane().add(sp1);


        clientInterface.getContentPane().add(btnQuery);
        clientInterface.getContentPane().add(btnAdd);
        clientInterface.getContentPane().add(btnRemove);
        clientInterface.getContentPane().add(btnUpdate);

        clientInterface.getContentPane().add(lblNewLabel_1);




        //Query button listener
        btnQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {

                //add event for Query button
                String word = Word_textField.getText();
                if(word.isEmpty())
                {
                    Result_textArea.setText("Please type a word.");
                    return;
                }
                String meaning = Meaning_textArea.getText();

                String displayText = objClient.query(word,meaning);
                String[] split = displayText.split("\\|", 2);
                System.out.println(displayText);
                Meaning_textArea.setText(split[0].replaceAll(";", "\n"));
                Word_textField.setText("");
                Result_textArea.setText(split[1]);
            }

        });


        //Add button listener
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //add event for add button
                String word = Word_textField.getText();
                String meaning = Meaning_textArea.getText();
                if(word.isEmpty() && meaning.isEmpty())
                {
                    Result_textArea.setText("Please type the Word & Meaning.");
                    return;
                }
                else if(word.isEmpty())
                {
                    Result_textArea.setText("Please type a word.");
                    return;
                }
                else if(meaning.isEmpty())
                {
                    Result_textArea.setText("Please type the meaning.");
                    return;
                }
                else if(word.matches("[a-zA-Z]+") != true)
                {
                    Result_textArea.setText("Please type a word with Uppercase or Lowercase letters.");
                    Word_textField.setText("");
                    Meaning_textArea.setText("");
                    return;
                }

                String displayText = objClient.add(word,meaning);
                String[] split = displayText.split("\\|", 2);
                Result_textArea.setText(split[1].replaceAll(";", "\n"));
                Word_textField.setText("");
                Meaning_textArea.setText(split[0]);
            }
        });


        //Remove button listener
        btnRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //add event for Remove button
                String word = Word_textField.getText();
                if(word.isEmpty())
                {
                    Result_textArea.setText("Please type a word.");
                    return;
                }
                String meaning = Meaning_textArea.getText();
                String displayText = objClient.remove(word,meaning);
                String[] split = displayText.split("\\|", 2);
                Word_textField.setText(split[0]);
                Meaning_textArea.setText("");
                Result_textArea.setText(split[0]);
            }
        });

        //update
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //add event for update button
                String word = Word_textField.getText();
                String meaning = Meaning_textArea.getText();
                if(word.isEmpty() && meaning.isEmpty())
                {
                    Result_textArea.setText("Please type the Word & Meaning.");
                    return;
                }
                else if(word.isEmpty())
                {
                    Result_textArea.setText("Please type a word.");
                    return;
                }
                else if(meaning.isEmpty())
                {
                    Result_textArea.setText("Please type the meaning.");
                    return;
                }


                String displayText = objClient.update(word,meaning);
                String[] split = displayText.split("\\|", 2);
                Result_textArea.setText(split[1].replaceAll(";", "\n"));
                Word_textField.setText("");
                Meaning_textArea.setText(split[0]);
            }
        });

    }
}
