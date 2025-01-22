/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package distilleryprototype;

/**
 *
 * @author lgosl1
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
public class Distilleryprototype {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input Section
        System.out.println("--- General Information ---");
        System.out.print("Enter Total Solids (%): ");
        double totalSolids = scanner.nextDouble();

        System.out.print("Enter kg Shipped: ");
        double kgShipped = scanner.nextDouble();

        System.out.print("Enter Number of Fermentors: ");
        int numFermentors = scanner.nextInt();

        System.out.println("--- Temperature Details ---");
        System.out.print("Enter Tanker Temperature (\u00b0C): ");
        double tankerTemp = scanner.nextDouble();

        System.out.print("Enter Hot Water Temperature (\u00b0C): ");
        double hotWaterTemp = scanner.nextDouble();

        System.out.println("--- Initial Settings ---");
        System.out.print("Enter Initial Water Added psig: ");
        double initialWaterAddedPsig = scanner.nextDouble();

        System.out.println("--- Other inputs ---");
        System.out.print("Enter Desired ferm solids: ");
        double desiredFermSolids = scanner.nextDouble();
        
        System.out.print("Enter Permeate density (kg/L): ");
        double permeateDensity = scanner.nextDouble();
        
        System.out.print("Enter Ferm feed density(kg/L): ");
        double fermFeedDensity = scanner.nextDouble();
        
        System.out.print("Enter Fermentation Volume (L): ");
        double fermentationVolume = scanner.nextDouble();
        
        System.out.print("Enter Fermentation heel volume (L): ");
        double fermentationHeelVolume = scanner.nextDouble();
        
        System.out.print("Enter Fermenter radius (m): ");
        double fermenterRadius = scanner.nextDouble();
        
        System.out.print("Enter Final abw (g/L): ");
        double finalAbw = scanner.nextDouble();
        
        System.out.print("Enter the First Conversion (g ethanol / g C6): ");
        double conversion1 = scanner.nextDouble();
        
        System.out.print("Enter Heat Capacity of water (kJ/kg°C): ");
        double heatCapacityWater = scanner.nextDouble();
        
        System.out.print("Enter heat capacity of permeate (kJ/kg°C): ");
        double heatCapacityPermeate = scanner.nextDouble();
        
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
        double estimatedFinalTemp = ((permKg * permTemp * heatCapacityPermeate) + (heatCapacityWater * waterIn * hotWaterTemp)) / ((waterIn * heatCapacityWater) + (heatCapacityPermeate * permKg)); // correct
        
        // Final Calculation
        double finalPsi = (finalVol - fermentationHeelVolume) / 1000 / Math.PI / Math.pow(fermenterRadius, 2) * 3.28 / 2.31 * fermFeedDensity;
        double finalTemp = estimatedFinalTemp; // correct
       
        // Write results to a file
        try (FileWriter writer = new FileWriter("Dilution_Calculation_Results.txt", true)) {
            writer.write("=== Dilution Calculation Results ===\n");
            writer.write("------------------------------------\n");
            
            writer.write("--- Outputs ---\n");
            writer.write(String.format("Initial Water Volume: %.2f L\n", initialWaterVolumeOutputs));
            writer.write(String.format("Water to be Added Per Tank: %.2f L\n", waterToBeAddedPerTank));
            writer.write(String.format("Water to be Added Per Tank: %.2f psig\n", waterToBeAddedPerTankPsig));
            writer.write(String.format("Permeate to be Added Per Tank: %.2f psig\n", permeateAddedPerTank));
            writer.write(String.format("Pressure After Permeate Added: %.2f psig\n", pressureAfterPermeateAdded));
            writer.write(String.format("Pressure After Extra Water Added: %.2f psig\n", pressureAfterExtraWaterAdded));
            writer.write(String.format("Estimated Final Temperature: %.2f \u00b0C\n", estimatedFinalTemp));
            writer.write("------------------------------------\n");
            
            writer.write("--- Total Water Added ---\n");
            writer.write(String.format("Water In: %.2f L\n", waterIn));
            writer.write(String.format("Water PSI: %.2f psig\n", waterPsi));
            writer.write(String.format("Temp In: %.2f \u00b0C\n", hotWaterTemp));
            writer.write("------------------------------------\n");

            writer.write("--- Permeate Added to Water ---\n");
            writer.write(String.format("Perm kg: %.2f kg\n", permKg));
            writer.write(String.format("Perm Vol: %.2f L\n", permVol));
            writer.write(String.format("Initial PSIG: %.2f psig\n", initialPermPsig));
            writer.write(String.format("Temp: %.2f \u00b0C\n", permTemp));
            writer.write("------------------------------------\n");

            writer.write("--- Final Fermentor Values ---\n");
            writer.write(String.format("Final kg: %.2f kg\n", finalKg));
            writer.write(String.format("Final vol: %.2f L\n", finalVol));
            writer.write(String.format("Final PSI: %.2f psig\n", finalPsi));
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
}
