import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DigitalClockWithAlarm {

    private JFrame frame;            
    private JLabel timeLabel;

    public DigitalClockWithAlarm() {
        frame = new JFrame("Digital Clock with Alarm");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(4, 1));

        timeLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        frame.add(timeLabel);

       
        Timer timer = new Timer(1000, e -> {
            Date now = new Date();
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(now);
 

            timeLabel.setText(currentTime);
        });
        timer.setInitialDelay(0);
        timer.start();

        frame.setVisible(true);
    }
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(DigitalClockWithAlarm::new);
    }
    }
