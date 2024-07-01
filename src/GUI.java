import java.util.ArrayList;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.time.Instant;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Arrays;

public class GUI {
    public static JFrame window;
    private static JPanel content;

    public static String username;
    public static String password;

    public static JPanel panel;

    public static Networker networker = new Networker();

    public static ArrayList<String> usernames = new ArrayList<String>();
    public static ArrayList<String> messages = new ArrayList<String>();
    public static ArrayList<Long> times = new ArrayList<Long>();

    public static Long lastTime = 0L;

    public void createWindow() {
    	window = new JFrame("net chat");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
		window.setSize(400, 500);
        window.setResizable(false);
		window.setVisible(true);

		panel();
    }
    public static void updateWindow() {
        window.pack();
        window.setSize(400, 500);
    }
    private static void panel() {
        content = new JPanel();
        content.setBackground(new Color(14,17,17));
        content.setBounds(0, 0, 400, 500);
        content.setLayout(null);
        window.add(content);
        updateWindow();
    }
    public static void startPage() {

    	JLabel serverLabel = new JLabel("Enter a server address", SwingConstants.CENTER);
        serverLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        serverLabel.setForeground(Color.WHITE);
        serverLabel.setBounds(100, 100, 200, 20);

        // text input
        JTextField serverInput = new JTextField(100);
        serverInput.setForeground(Color.WHITE);
        serverInput.setBackground(new Color(14,17,17));
        serverInput.setFont(new Font("Arial", Font.PLAIN, 14));
        serverInput.setBounds(100, 150, 200, 20);

        // submit server address
        JButton connect = new JButton("Connect");
        connect.setFont(new Font("Arial", Font.PLAIN, 14));
        connect.setBackground(new Color(14,17,17));
        connect.setForeground(Color.WHITE);
        connect.setBounds(150, 200, 100, 20);

        // capture if the button is pressed
        connect.addActionListener(new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
                window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    int serverResponse = networker.alive(serverInput.getText());
                    if (serverResponse == 200) {
                        System.out.println("connected to server");
                        networker.server = serverInput.getText();
                        window.setCursor(Cursor.getDefaultCursor());
                        serverLogon();
                    } else {
                        window.setCursor(Cursor.getDefaultCursor());
                        JOptionPane.showMessageDialog(window, "Server didn't respond properly, response: " + serverResponse, "Error",  JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(window, "Error connecting to server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });        

        content.add(serverLabel);
        content.add(serverInput);
        content.add(connect);
        updateWindow();
    }
    public static void serverLogon() {
        System.out.println(networker.server);
        content.removeAll();
        updateWindow();

        JLabel usernameLabel = new JLabel("username", SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(100, 100, 200, 20);

        JTextField usernameInput = new JTextField(100);
        usernameInput.setForeground(Color.WHITE);
        usernameInput.setBackground(new Color(14,17,17));
        usernameInput.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameInput.setBounds(100, 150, 200, 20);

        JLabel passwordLabel = new JLabel("password", SwingConstants.CENTER);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(100, 200, 200, 20);

        JTextField passwordInput = new JTextField(100);
        passwordInput.setForeground(Color.WHITE);
        passwordInput.setBackground(new Color(14,17,17));
        passwordInput.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordInput.setBounds(100, 250, 200, 20);

        JButton logon = new JButton("logon");
        logon.setFont(new Font("Arial", Font.PLAIN, 14));
        logon.setBackground(new Color(14,17,17));
        logon.setForeground(Color.WHITE);
        logon.setBounds(150, 300, 100, 20);

        logon.addActionListener(new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameInput.getText();
                password = passwordInput.getText();
                System.out.println(username);
                if ((username.equals("server")) || (username.equals("admin") || (username.equals("")) || (username.equals("you")))) {
                    usernameLabel.setForeground(Color.RED);
                    usernameLabel.setText("pick another username");
                    usernameInput.setForeground(Color.RED);
                    usernameInput.setBorder(BorderFactory.createLineBorder(Color.RED));
                } else {
                    System.out.println("username valid");
                }
                try {
                    window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    String output = networker.send(networker.server, password, "server", username+" has joined the chat", "false");
                    if (output.equals("msg sent")) {
                        chatRoom();
                    } else {
                        window.setCursor(Cursor.getDefaultCursor());
                        passwordLabel.setForeground(Color.RED);
                        passwordLabel.setText("invalid password");
                        passwordInput.setForeground(Color.RED);
                        passwordInput.setBorder(BorderFactory.createLineBorder(Color.RED));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(window, "Error connecting to server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        content.add(usernameLabel);
        content.add(usernameInput);
        content.add(passwordLabel);
        content.add(passwordInput);
        content.add(logon);
        updateWindow();
    }


    public static void chatRoom() {
        System.out.println("joined the chat room");

        content.removeAll();
        updateWindow();

        window.setCursor(Cursor.getDefaultCursor());

        // bottom sender input and send button
        JTextField messageBox = new JTextField(500);
        messageBox.setForeground(Color.WHITE);
        messageBox.setBackground(new Color(14,17,17));
        messageBox.setFont(new Font("Arial", Font.PLAIN, 24));
        messageBox.setBounds(0, 435, 300, 30);

        JButton send = new JButton("send");
        send.setFont(new Font("Arial", Font.PLAIN, 24));
        send.setBackground(new Color(14,17,17));
        send.setForeground(Color.WHITE);
        send.setFocusable(false);
        send.setBounds(300, 435, 100, 30);

        send.addActionListener(new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
                window.setTitle("sending...");
                window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    networker.send(networker.server, password, username, messageBox.getText(), "false");
                    messageBox.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(window, "Error connecting to server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                window.setCursor(Cursor.getDefaultCursor());
                window.setTitle("net chat");

            }
        });

        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        JScrollPane msgsBox = new JScrollPane(panel);
        msgsBox.setBounds(2, 2, 398, 428);
        msgsBox.setBackground(new Color(14,17,17));
        panel.setBackground(new Color(14,17,17));

        content.add(messageBox);
        content.add(send);
        content.add(msgsBox);
        updateWindow();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                long unixTime = Instant.now().getEpochSecond();
                boolean run = true;
                while (run) {
                    try {
                        reciver(unixTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    displayer();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread1.start();

    }
    public static boolean isValidJSON(String jsonString) {
        try {
            JSONParser parser = new JSONParser();
            parser.parse(jsonString);
            return true; // The JSON string is valid
        } catch (ParseException e) {
            return false; // The JSON string is invalid
        }
    }
    public static void reciver(long startTime) throws InterruptedException {
        try {
            String json = networker.send(networker.server, password, "server", username+" has joined the chat", "true"); 
            //System.out.println(json);

            try {
                JSONParser parser = new JSONParser();
                JSONArray jsonArray = (JSONArray) parser.parse(json);
                JSONArray usernamesArray = (JSONArray) jsonArray.get(0);
                for (int i = 0; i < usernamesArray.size(); i++) {
                    JSONArray subSubArray = (JSONArray) usernamesArray.get(i);
                    for (int j = 0; j < subSubArray.size(); j++) {
                        String username = (String) subSubArray.get(j);
                        usernames.add(username);
                    }
                }

                JSONArray messagesArray = (JSONArray) jsonArray.get(1);
                for (int i = 0; i < messagesArray.size(); i++) {
                    JSONArray subSubArray = (JSONArray) messagesArray.get(i);
                    for (int j = 0; j < subSubArray.size(); j++) {
                        String message = (String) subSubArray.get(j);
                        messages.add(message);
                    }
                }

                JSONArray timesArray = (JSONArray) jsonArray.get(2);
                for (int i = 0; i < timesArray.size(); i++) {
                    JSONArray subSubArray = (JSONArray) timesArray.get(i);
                    for (int j = 0; j < subSubArray.size(); j++) {
                        Long time = (Long) subSubArray.get(j);
                        times.add(time);
                    }
                }
                
            } catch(ParseException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, "Error connecting to server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    public static void displayer() {
        for (int i = 0; i < usernames.size(); i++) {
            if (times.get(i) > lastTime) {
                addNewLabel(usernames.get(i) + ": " + messages.get(i));
                lastTime = times.get(i);
            }
        }
    }
    public static void addNewLabel(String content) {
        JLabel newLabel = new JLabel(content);
        newLabel.setForeground(Color.WHITE); // Set label text color to white
        newLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Set Arial font with size 14
        panel.add(newLabel);
        panel.revalidate();
    }
}
