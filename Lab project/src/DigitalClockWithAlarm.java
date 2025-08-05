import javax.swing.*;
import java.awt.*;
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

    public DigitalClockWithAlarm() {
        frame = new JFrame("Digital Clock with Alarm");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(4, 1));

        // Time display (12-hour format with seconds)
        timeLabel = new JLabel("12:00:00 AM", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        frame.add(timeLabel);

        // Date display
        dateLabel = new JLabel("Date goes here", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        frame.add(dateLabel);

        // Alarm input panel (HH:mm only)
        JPanel alarmPanel = new JPanel();
        alarmPanel.add(new JLabel("Set Alarm (HH:MM):"));
        alarmField = new JTextField(5);
        alarmPanel.add(alarmField);
        frame.add(alarmPanel);

        // Alarm button
        setAlarmButton = new JButton("Set Alarm");
        frame.add(setAlarmButton);

        // Alarm button action
        setAlarmButton.addActionListener(e -> {
            String input = alarmField.getText().trim();
            if (!input.matches("\\d{2}:\\d{2}")) {
                JOptionPane.showMessageDialog(frame, "Invalid format. Use HH:mm", "Error", JOptionPane.ERROR_MESSAGE);
                alarmTime = null;
                return;
            }
            try {
                new SimpleDateFormat("hh:mm").parse(input); // Validate format
                alarmTime = input;
                alarmTriggered = false;
                JOptionPane.showMessageDialog(frame, "Alarm set for: " + alarmTime);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid time format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Clock update timer
        Timer timer = new Timer(1000, e -> {
            Date now = new Date();
            String currentTime = new SimpleDateFormat("hh:mm:ss a").format(now); // 12-hour with seconds
            String currentDate = new SimpleDateFormat("EEEE, dd MMMM yyyy").format(now);

            timeLabel.setText(currentTime);
            dateLabel.setText(currentDate);

            // Check alarm match (ignoring seconds)
            String currentTimeNoSec = new SimpleDateFormat("hh:mm").format(now);
            if (alarmTime != null && currentTimeNoSec.equals(alarmTime) && !alarmTriggered) {
                alarmTriggered = true;
                playAlarmSound();
                JOptionPane.showMessageDialog(null, "⏰ Alarm! It's " + currentTime);
            }
        });
        timer.setInitialDelay(0);
        timer.start();

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DigitalClockWithAlarm::new);
    }
}
