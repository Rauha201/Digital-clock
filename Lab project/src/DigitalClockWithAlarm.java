import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


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
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(4, 1));

        // Time display
        timeLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        frame.add(timeLabel);

        // Date display
        dateLabel = new JLabel("Date goes here", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        frame.add(dateLabel);

        // Alarm input panel
        JPanel alarmPanel = new JPanel();
        alarmPanel.add(new JLabel("Set Alarm (HH:MM:SS):"));
        alarmField = new JTextField(8);
        alarmPanel.add(alarmField);
        frame.add(alarmPanel);

        // Alarm button
        setAlarmButton = new JButton("Set Alarm");
        frame.add(setAlarmButton);

        // Alarm button action
        setAlarmButton.addActionListener(e -> {
            String input = alarmField.getText().trim();
            if (!input.matches("\\d{2}:\\d{2}:\\d{2}")) {
                JOptionPane.showMessageDialog(frame, "Invalid format. Use HH:mm:ss", "Error", JOptionPane.ERROR_MESSAGE);
                alarmTime = null;
                return;
            }
            try {
                new SimpleDateFormat("HH:mm:ss").parse(input);
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
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(now);
            String currentDate = new SimpleDateFormat("EEEE, dd MMMM yyyy").format(now);

            timeLabel.setText(currentTime);
            dateLabel.setText(currentDate);

            if (alarmTime != null && currentTime.equals(alarmTime) && !alarmTriggered) {
                alarmTriggered = true;
               
                JOptionPane.showMessageDialog(null, "⏰ Alarm! It's " + currentTime);
            }
        });
        timer.setInitialDelay(0);
        timer.start();

        // Setup system tray
       
        frame.setVisible(true); // Set to false if you want tray-only startup
    }

        public static void main(String[] args) {
        SwingUtilities.invokeLater(DigitalClockWithAlarm::new);
    }
}
