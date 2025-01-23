/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distilleryprototype;

/**
 *
 * @author lgosl1
 */
import java.awt.*;
import javax.swing.*;

public class TankerGraphic extends JPanel{
    private final double permeateVolume; 
    private final double waterVolume;  
    private final double totalVolume;
    private final double tankCapacity;

    public TankerGraphic(double permeateVolume, double waterVolume, double totalVolume, double tankCapacity) {
        this.permeateVolume = permeateVolume;
        this.waterVolume = waterVolume;
        this.totalVolume = totalVolume;
        this.tankCapacity = tankCapacity;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Tank dimensions
        int width = 200;
        int height = 400;
        int x = (getWidth() - width) / 2;
        int y = 50;

        // Calculate heights for permeate and water
        int permeateHeight = (int) ((permeateVolume / tankCapacity) * height);
        int waterHeight = (int) ((waterVolume / tankCapacity) * height);
        int emptyHeight = height - (permeateHeight + waterHeight);
        int permeateY = y + height - permeateHeight;
        int waterY = permeateY - waterHeight;
        int emptyY = y;
        
        // Outline
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
        
        // Draw permeate
        g2d.setColor(new Color(255, 223, 88));
        g2d.fillRect(x, permeateY, width, permeateHeight);
        
        // Draw water
        g2d.setColor(new Color(88, 155, 255));
        g2d.fillRect(x, waterY, width, waterHeight);
        
        // Draw empty
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x, emptyY, width, emptyHeight);
        
        // Labels
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        // Total volume label
        String totalVolumeLabel = "Total Volume: " + (int) totalVolume + " L";
        int totalVolumeWidth = g2d.getFontMetrics().stringWidth(totalVolumeLabel);
        g2d.drawString(totalVolumeLabel, x + (width - totalVolumeWidth) / 2, y - 10);

        // Permeate label
        String permeateLabel = "Permeate: " + (int) permeateVolume + " L";
        int permeateLabelWidth = g2d.getFontMetrics().stringWidth(permeateLabel);
        g2d.drawString(permeateLabel, x + (width - permeateLabelWidth) / 2, permeateY + permeateHeight / 2);

        // Water label
        String waterLabel = "Water: " + (int) waterVolume + " L";
        int waterLabelWidth = g2d.getFontMetrics().stringWidth(waterLabel);
        g2d.drawString(waterLabel, x + (width - waterLabelWidth) / 2, waterY + waterHeight / 2);
        
        if (emptyHeight > 0) {
        String emptyLabel = "Empty: " + (int) (tankCapacity - totalVolume) + " L";
        int emptyLabelWidth = g2d.getFontMetrics().stringWidth(emptyLabel);
        g2d.drawString(emptyLabel, x + (width - emptyLabelWidth) / 2, emptyY + emptyHeight / 2);
        }
    }
}
