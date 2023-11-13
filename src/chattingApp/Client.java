package chattingApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.*;
import java.util.*;


public class Client implements ActionListener {
    private Menu menu;
    JTextField text;
    private static JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
     DataOutputStream oust;
    private boolean isConnected = false;

    

    private ImageIcon createImageIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(path));
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(image);
    }

    private JLabel createLabel(ImageIcon icon, int x, int y, int width, int height) {
        JLabel label = new JLabel(icon);
        label.setBounds(x, y, width, height);
        return label;
    }

    Client() {
        menu = new Menu();
        f.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        JLabel back = createLabel(createImageIcon("icons/3.png", 25, 25), 5, 20, 25, 25);
        p1.add(back);

        // Configure the "profile" icon
        JLabel profile = createLabel(createImageIcon("icons/female2.jpg", 50, 50), 40, 10, 50, 50);
        p1.add(profile);

        // Configure the "video" icon
        JLabel video = createLabel(createImageIcon("icons/video.png", 30, 30), 300, 20, 30, 30);
        p1.add(video);

        // Configure the "phone" icon
        JLabel phone = createLabel(createImageIcon("icons/phone.png", 35, 30), 360, 20, 35, 30);
        p1.add(phone);

        // Configure the "morevert" icon and its right-click event
        ImageIcon morevertIcon = createImageIcon("icons/3icon.png", 10, 25);
        JLabel morevert = createLabel(morevertIcon, 420, 20, 10, 25);
        morevert.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    menu.show(morevert, e.getX(), e.getY());
                }
            }
        });
        p1.add(morevert);

        JLabel name = new JLabel("Mosa");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel(isConnected ? "Active Now" : "Offline");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        p1.add(status);

        a1 = new JPanel();
        a1.setLayout(null);
        a1.setBounds(5, 75, 425, 570);
        f.add(a1);

        text = new JTextField();
        text.setBounds(5, 475, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        a1.add(text);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(320, 475, 100, 40);
        sendButton.setBackground(new Color(7, 94, 84));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        sendButton.addActionListener(this);
        a1.add(sendButton);

        f.setSize(450, 700);
        f.setLocation(800, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);
    }

    // ActionListener method for handling the "Send" button click
    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text.getText();
            JPanel p2 = formatLabel(out, "Client");

            a1.setLayout(new BorderLayout());
            JPanel right = new JPanel(new BorderLayout());

            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            a1.add(vertical, BorderLayout.PAGE_START);

            oust.writeUTF(out);
            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for formatting chat messages with a sender label
    public static JPanel formatLabel(String out, String sender) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        
        // Set the background color based on the sender
        if (sender.equals("Client")) {
            output.setBackground(new Color(37, 211, 102));
        } else {
            output.setBackground(Color.WHITE);
        }
        
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        panel.add(output);
        //make sure that sent messages have a time stamp on it 
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }

    // Main method for starting the client
    public static void main(String args[]) {
       Client client =  new Client();
      Client.Sound sound=client.new Sound(); //new

        // Connect to the server and set up I/O streams
        try (Socket s = new Socket("127.0.0.1", 8080)) { //make sure this port is available for use
            DataInputStream din = new DataInputStream(s.getInputStream());
            client.oust = new DataOutputStream(s.getOutputStream());
            client.isConnected = true;
            while (true) {
                a1.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg, "Server");

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                a1.add(vertical, BorderLayout.PAGE_START);

                f.validate();
               sound.tone(1000, 1000, 1.0);
          
            }
        } catch (Exception e) {
            e.printStackTrace();
            client.isConnected = false;
        }
        
    }
    class Sound
{
    public static float SAMPLE_RATE = 8000f;
    public static void tone(int hz, int msecs) 
    throws LineUnavailableException 
    {
        tone(hz, msecs, 1.0);
    }

    public static void tone(int hz, int msecs, double vol)
    throws LineUnavailableException 
    {
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat(SAMPLE_RATE,8,1,true,false);     
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i=0; i < msecs*8; i++) {
              double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
              buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
              sdl.write(buf,0,1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }
}
}