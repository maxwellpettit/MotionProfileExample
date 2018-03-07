package org.usfirst.frc.team4145.robot.motionprofile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MotionProfileReader {

    private double[][] points;

    public MotionProfileReader(String filename) {

        File file = new File(filename);
        Scanner inputStream;

        List<double[]> lines = new ArrayList<>();
        try {
            inputStream = new Scanner(file);

            while (inputStream.hasNext()) {
                String line = inputStream.nextLine();
                String[] values = line.split(",");
                double[] numbers = new double[values.length];
                for (int v = 0; v < values.length; v++) {
                    numbers[v] = Double.parseDouble(values[v]);
                }
                lines.add(numbers);
            }

            points = lines.toArray(new double[0][0]);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public double[][] getPoints() {
        return points;
    }

}
