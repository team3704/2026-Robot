package frc.robot.commands;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.generated.TunerConstants;
import frc.robot.Constants.LimelightConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class AdjustDistance extends Command {

    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    private final CommandSwerveDrivetrain m_swerve;
    private final SwerveRequest.RobotCentric drive = new SwerveRequest.RobotCentric()
        .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    ;

    public AdjustDistance(CommandSwerveDrivetrain subsystem) {
        m_swerve = subsystem;
        addRequirements(m_swerve);
    }

    @Override
    public void initialize() {
        if(!LimelightHelpers.getTV(LimelightConstants.Name)) return;

        double VerticalOffsetAngle = LimelightHelpers.getTY(LimelightConstants.Name);

        double AngleToGoalDegrees = LimelightConstants.LimelightAngle + VerticalOffsetAngle;
        double AngleToGoalRadians = AngleToGoalDegrees * (3.14159 / 180.0);

        //calculate distance
        //Might need to add actual goal height later, instead of just the tag.
        double Distance = (LimelightConstants.TagHeight - LimelightConstants.LimelightHeight) / Math.tan(AngleToGoalRadians);

        double error = LimelightConstants.TargetDistance - Distance;
        double xError = LimelightHelpers.getTX(LimelightConstants.Name);

        //KpDistance is a proportional constant, no idea what it does lol but documentation says to use
        m_swerve.setControl(
            drive.withVelocityX(error * LimelightConstants.KpDistance)
            .withRotationalRate(xError)
        );
    }
}
