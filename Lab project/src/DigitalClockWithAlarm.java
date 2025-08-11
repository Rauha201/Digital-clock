import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sound.sampled.*;

public class DigitalClockWithAlarm {

    private JFrame frame;
    private JLabel timeLabel;
    private JLabel dateLabel;
    private JTextField alarmField;
    private JButton setAlarmButton;
    private String alarmTime = null;
    private boolean alarmTriggered = false;
    private TrayIcon trayIcon;
    private JComboBox<String> timeFormatBox;
    private JComboBox<String> amPmBox;

    public DigitalClockWithAlarm() {
        frame = new JFrame("Digital Clock with Alarm");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(5, 1));

        
        timeLabel = new JLabel("12:00:00 AM", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        frame.add(timeLabel);

        
        dateLabel = new JLabel("Date goes here", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        frame.add(dateLabel);

        
        JPanel formatPanel = new JPanel();
        formatPanel.add(new JLabel("Select Time Format:"));
        timeFormatBox = new JComboBox<>(new String[]{"12-hour", "24-hour"});
        formatPanel.add(timeFormatBox);
        frame.add(formatPanel);

        
        JPanel alarmPanel = new JPanel();
        alarmPanel.add(new JLabel("Set Alarm (HH:MM):"));
        alarmField = new JTextField(5);
        alarmPanel.add(alarmField);
        amPmBox = new JComboBox<>(new String[]{"AM", "PM"});
        alarmPanel.add(amPmBox);
        frame.add(alarmPanel);

        
        setAlarmButton = new JButton("Set Alarm");
        setAlarmButton.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(setAlarmButton);

        
        setAlarmButton.addActionListener(e -> {
            String input = alarmField.getText().trim();
            boolean is12Hour = timeFormatBox.getSelectedItem().equals("12-hour");

            if (!input.matches("\\d{2}:\\d{2}")) {
                JOptionPane.showMessageDialog(frame, "Invalid format. Use HH:mm", "Error", JOptionPane.ERROR_MESSAGE);
                alarmTime = null;
                return;
            }
            try {
                if (is12Hour) {
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    Date date = sdf.parse(input + " " + amPmBox.getSelectedItem());
                    alarmTime = sdf.format(date);
                } else {
                    alarmTime = input;
                }
                alarmTriggered = false;
                JOptionPane.showMessageDialog(frame, "Alarm set for: " + alarmTime);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid time format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        
        Timer timer = new Timer(1000, e -> {
            Date now = new Date();
            boolean is12Hour = timeFormatBox.getSelectedItem().equals("12-hour");
            String currentTime;
            if (is12Hour) {
                currentTime = new SimpleDateFormat("hh:mm:ss a").format(now);
            } else {
                currentTime = new SimpleDateFormat("HH:mm:ss").format(now);
            }
            String currentDate = new SimpleDateFormat("EEEE, dd MMMM yyyy").format(now);

            timeLabel.setText(currentTime);
            dateLabel.setText(currentDate);

            
            if (alarmTime != null) {
                String currentCheck = is12Hour
                        ? new SimpleDateFormat("hh:mm a").format(now)
                        : new SimpleDateFormat("HH:mm").format(now);
                if (currentCheck.equalsIgnoreCase(alarmTime) && !alarmTriggered) {
                    alarmTriggered = true;
                    playAlarmSound();
                    JOptionPane.showMessageDialog(null, "⏰ Alarm! It's " + currentTime);
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();

        
        setupSystemTray();

        
        timeFormatBox.addActionListener(e -> {
            boolean is12Hour = timeFormatBox.getSelectedItem().equals("12-hour");
            amPmBox.setVisible(is12Hour);
            frame.revalidate();
            frame.repaint();
        });

        amPmBox.setVisible(true); 

        frame.setVisible(true);
    }

    private void playAlarmSound() {
        try {
            File soundFile = new File("sound/alarm_sound.wav");
            if (!soundFile.exists()) {
                System.err.println("Sound file not found: " + soundFile.getAbsolutePath());
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip alarmClip = AudioSystem.getClip();
            alarmClip.open(audioIn);
            alarmClip.setFramePosition(0);
            alarmClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to play alarm sound.", "Sound Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupSystemTray() {
        if (!SystemTray.isSupported()) {
            System.err.println("SystemTray not supported");
            return;
        }
        SystemTray tray = SystemTray.getSystemTray();
        Image icon = Toolkit.getDefaultToolkit().getImage("sound/clock.jpg");

        PopupMenu popup = new PopupMenu();
        MenuItem openItem = new MenuItem("Open Clock");
        openItem.addActionListener(e -> frame.setVisible(true));
        popup.add(openItem);

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });
        popup.add(exitItem);

        trayIcon = new TrayIcon(icon, "Digital Clock", popup);
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
        }

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DigitalClockWithAlarm::new);
    }
}

