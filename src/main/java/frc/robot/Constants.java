// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
  public static class intakeConstants {
    public static final double intakeSpeed = 0.8;
    public static final double coughSpeed = -0.4;
  }

  public static class DriveTrainConstants {
    public static final double MaxSpeed = 15.0;
    public static final double MaxAngularSpeed = 15.0;
  }

  public static class LimelightConstants {
    public static final double TagHeight = 44.25;
    public static final double TargetHeight = 72.0;
    public static final double LimelightAngle = -0.99 ;
    public static final double TargetDistance = 121.0;
    public static final String Name = "limelight-larry";

    //Limelight lens height from floor
    public static final double LimelightHeight = 19.6;

    public static final double KpDistance = 0.1;
  }

  public static class SubsystemConstants {
    public static final double ClimberSpeed = .5;
    public static final double ShooterSpeed = 0.7;
    public static final double DriveSpeed = 6.7;
    
  }
}
