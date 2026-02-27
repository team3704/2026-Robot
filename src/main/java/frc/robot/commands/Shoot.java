package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.LimelightHelpers;

public class Shoot extends Command {

    private final CommandSwerveDrivetrain m_swerve;
    

    public Shoot(CommandSwerveDrivetrain subsystem) {
        m_swerve = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        
    }
}
