// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
//
// now its MY turn to commit.
//
// Ahh... free at last. O Gabriel, now dawns thy reckoning, and thy gore shall GLISTEN before the temples of Man!
// Creature of steel... my gratitude upon thee for my freedom. But the crimes thy have committed against humanity are NOT forgotten. And thy punishment... is DEATH.
//
// //limelight branch first commit

//where am i ?!??!?!

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.LimelightResults;
import frc.robot.subsystems.Swerve;

/** An example command that uses an example subsystem. */
public class AdjustDistance extends Command {

    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Swerve m_swerve;

    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public AdjustDistance(Swerve subsystem) {
        m_swerve = subsystem;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);

        double targetOffsetAngle_Vertical = LimelightHelpers.getTY("limelight");

        //hell yeah copy and pasted documentation code

        // how many degrees back is your limelight rotated from perfectly vertical?
        double limelightMountAngleDegrees = 25.0; //assumed

        // distance from the center of the Limelight lens to the floor
        double limelightLensHeightInches = 20.0; //assumed

        // distance from the target to the floor
        double goalHeightInches = 72.0;
        //72 inches off ground (thing, not april tag)
        //the april tag is 44.25 inches off the ground
        //...is adjusting needed?

        double angleToGoalDegrees =
            limelightMountAngleDegrees + targetOffsetAngle_Vertical;
        double angleToGoalRadians = angleToGoalDegrees * (Math.PI / 180.0);

        //calculate distance
        double distanceFromLimelightToGoalInches =
            (goalHeightInches - limelightLensHeightInches) /
            Math.tan(angleToGoalRadians);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
