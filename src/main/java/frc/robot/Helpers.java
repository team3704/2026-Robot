package frc.robot;

public class Helpers {
    public static double CalculateDistance(double Height, double AngleRadians) {
        return Height / Math.tan(AngleRadians);
    } 
}
