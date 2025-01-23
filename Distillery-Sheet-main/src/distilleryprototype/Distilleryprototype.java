/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package distilleryprototype;

/**
 *
 * 
 * @author lgosl1
 */
import java.io.*;
import java.util.*;
import javax.swing.*;
public class Distilleryprototype {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Defualt values
        double totalSolids = 22;
        double kgShipped = 17850;
        int numFermentors = 3;
        double tankerTemp = 6;
        double hotWaterTemp = 80;
        double initialWaterAddedPsig = 0.9;
        double desiredFermSolids = 0.14;
        double permeateDensity = 1.1;
        double fermFeedDensity = 1.05;
        double fermentationVolume = 9000;
        double fermentationHeelVolume = 850;
        double fermenterRadius = 1.01;
        double finalAbw = 55.9;
        double conversion1 = 0.4;
        double heatCapacityWater = 4.18;
        double heatCapacityPermeate = 3.8;
        
        // Prompt if wanting default values
        int choice = getValidChoice(scanner);
        
        // if manual input override default
        if (choice ==1) {
            System.out.println("--- Inputs ---");
            totalSolids = getValidDouble(scanner, "Enter Total Solids (%): ", true);
            kgShipped = getValidDouble(scanner, "Enter kg Shipped: ", true);
            numFermentors = getValidInt(scanner, "Enter Number of Fermentors: ", true);
            tankerTemp = getValidDouble(scanner, "Enter Tanker Temperature (\u00b0C): ", true);
            hotWaterTemp = getValidDouble(scanner, "Enter Hot Water Temperature (\u00b0C): ", true);
            initialWaterAddedPsig = getValidDouble(scanner, "Enter Initial Water Added psig: ", true);

            System.out.println("--- Other inputs ---");
            desiredFermSolids = getValidDouble(scanner, "Enter Desired ferm solids: ", true);
            permeateDensity = getValidDouble(scanner, "Enter Permeate density (kg/L): ", true);
            fermFeedDensity = getValidDouble(scanner, "Enter Ferm feed density(kg/L): ", true);
            fermentationVolume = getValidDouble(scanner, "Enter Fermentation Volume (L): ", true);
            fermentationHeelVolume = getValidDouble(scanner, "Enter Fermentation heel volume (L): ", true);
            fermenterRadius = getValidDouble(scanner, "Enter Fermenter radius (m): ", true);
            finalAbw = getValidDouble(scanner, "Enter Final abw (g/L): ", true);
            conversion1 = getValidDouble(scanner, "Enter the First Conversion (g ethanol / g C6): ", true);
            heatCapacityWater = getValidDouble(scanner, "Enter Heat Capacity of water (kJ/kg°C): ", true);
            heatCapacityPermeate = getValidDouble(scanner, "Enter heat capacity of permeate (kJ/kg°C): ", true);
        }
        else {
            System.out.println("Using default values for all inputs.");
        }
        // Equations
        
        // Bottom Equations correct
        double kgTsFerm = kgShipped * (totalSolids / 100) / numFermentors; 
        double conversion2 = 0.95; 
        double desiredConcentration = finalAbw / conversion1 * conversion2; 
        double initialTs = (desiredConcentration / 0.9 / 1000) * 100; 
        
        // Permeate added to water correct
        double permKg = kgShipped / numFermentors; 
         
        // More Final Fermentor Values correct
        double finalKg = kgTsFerm / desiredFermSolids; // correct
        double finalVol = finalKg / fermFeedDensity; // correct
        
        // Water added calculation correct
        double waterIn = finalKg - permKg; // Correct
        
        // Permeate Added to Water correct
        double permVol = permKg / permeateDensity; // Correct
        double permTemp = tankerTemp; // Correct
        double initialPermPsig = (permVol) / 1000 / Math.PI / Math.pow(fermenterRadius, 2) * 3.28 / 2.31 * permeateDensity; // Correct
        
        // Total Water Added
        double waterPsi = (waterIn - fermentationHeelVolume) / 1000 / Math.PI / Math.pow(fermenterRadius, 2) * 3.28 / 2.31; 
        
        // Outputs correct 
        double initialWaterVolumeOutputs = Math.PI * Math.pow(1.01, 2) * (1 / 3.28) * 2.31 * 1000 * initialWaterAddedPsig + 850; // Correct
        double waterToBeAddedPerTank = waterIn; // correct
        double waterToBeAddedPerTankPsig = waterPsi; // correct
        double permeateAddedPerTank = permKg; // Correct
        double pressureAfterPermeateAdded = initialPermPsig + initialWaterAddedPsig; // correct
        double pressureAfterExtraWaterAdded = waterToBeAddedPerTankPsig - initialWaterAddedPsig + pressureAfterPermeateAdded; // correct
        double estimatedFinalTemp = ((permKg * permTemp * heatCapacityPermeate) + (heatCapacityWater * waterIn * hotWaterTemp)) / ((waterIn * heatCapacityWater) + (heatCapacityPermeate * permKg));
        
        // Final Calculation
        double finalPsi = (finalVol - fermentationHeelVolume) / 1000 / Math.PI / Math.pow(fermenterRadius, 2) * 3.28 / 2.31 * fermFeedDensity;
        double finalTemp = estimatedFinalTemp; // correct
       
        // Tanker graphic stuff
        double permeateVolume = permKg / fermFeedDensity;
        double waterVolume = waterIn / fermFeedDensity;
        double totalVolume = permeateVolume + waterVolume;
        showTankerGraphic(permeateVolume, waterVolume, totalVolume, fermentationVolume);
        
        // Write results to a file
        try (FileWriter writer = new FileWriter("Dilution_Calculation_Results.txt", true)) {
            writer.write("=== Dilution Calculation Results ===\n");
            writer.write("Using " + (choice == 1 ? "manual" : "default") + " values\n");
            writer.write("------------------------------------\n");
            
            writer.write("--- Outputs ---\n");
            writer.write(String.format("Initial Water Volume: %.2f L\n", initialWaterVolumeOutputs));
            writer.write(String.format("Water to be Added Per Tank: %.2f L\n", waterToBeAddedPerTank));
            writer.write(String.format("Water to be Added Per Tank: %.2f Psig\n", waterToBeAddedPerTankPsig));
            writer.write(String.format("Permeate to be Added Per Tank: %.2f Psig\n", permeateAddedPerTank));
            writer.write(String.format("Pressure After Permeate Added: %.2f Psig\n", pressureAfterPermeateAdded));
            writer.write(String.format("Pressure After Extra Water Added: %.2f Psig\n", pressureAfterExtraWaterAdded));
            writer.write(String.format("Estimated Final Temperature: %.2f \u00b0C\n", estimatedFinalTemp));
            writer.write("------------------------------------\n");
            
            writer.write("--- Total Water Added ---\n");
            writer.write(String.format("Water In: %.2f L\n", waterIn));
            writer.write(String.format("Water PSI: %.2f Psig\n", waterPsi));
            writer.write(String.format("Temp In: %.2f \u00b0C\n", hotWaterTemp));
            writer.write("------------------------------------\n");

            writer.write("--- Permeate Added to Water ---\n");
            writer.write(String.format("Perm kg: %.2f kg\n", permKg));
            writer.write(String.format("Perm Vol: %.2f L\n", permVol));
            writer.write(String.format("Initial Psig: %.2f Psig\n", initialPermPsig));
            writer.write(String.format("Temp: %.2f \u00b0C\n", permTemp));
            writer.write("------------------------------------\n");

            writer.write("--- Final Fermentor Values ---\n");
            writer.write(String.format("Final kg: %.2f kg\n", finalKg));
            writer.write(String.format("Final vol: %.2f L\n", finalVol));
            writer.write(String.format("Final PSI: %.2f Psig\n", finalPsi));
            writer.write(String.format("Final Temp: %.2f \u00b0C\n", finalTemp));
            writer.write("------------------------------------\n");
            
            writer.write("--- Other Outputs ---\n");
            writer.write(String.format("kg TS/ferm: %.2f kg/ferm\n", kgTsFerm));
            writer.write(String.format("Conversion: %.2f g ethanol / g C6\n", conversion2));
            writer.write(String.format("Desired Concentraction: %.2f g/L lactose\n", desiredConcentration));
            writer.write(String.format("Initial TS: %.2f%%\n", initialTs));
            writer.write("------------------------------------\n");

            System.out.println("Results have been written to Dilution_Calculation_Results.txt");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    private static void showTankerGraphic(double permeateVolume, double waterVolume, double totalVolume, double fermentationVolume) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tanker Contents");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400,600);
            frame.add(new TankerGraphic(permeateVolume, waterVolume, totalVolume, fermentationVolume));
            frame.setVisible(true);
        });
    }
    private static int getValidChoice(Scanner scanner) {
        while(true) {
            try {
                System.out.print("Choose input mode:\n1. Manual input\n2.Default values\nYour choice: ");
                int choice = scanner.nextInt();
                if (choice == 1 || choice == 2) {
                    return choice;
                }
                System.out.println("Please enter 1 or 2");
            }
            catch(InputMismatchException e) {
                System.out.println("Please enter 1 or 2");
                scanner.next();
            }
        }
    }
    private static double getValidDouble(Scanner scanner, String prompt, boolean mustBePositive) {
        while(true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                if (!mustBePositive || value > 0) {
                    return value;
                }
                System.out.println("Value must be positive.");
            }  
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
        }
    }
    private static int getValidInt(Scanner scanner, String prompt, boolean mustBePositive) {
        while(true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                if (!mustBePositive || value > 0) {
                    return value;
                }
                System.out.println("Value must be positive.");
            }  
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
        }
    }
}


