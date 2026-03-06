package frc.robot.commands;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;

public class Shoot extends Command {

    private Shooter m_shoot;

    public Shoot(Shooter subsystem) {
        m_shoot = subsystem;
        addRequirements(m_shoot);
    }

    @Override
    public void initialize() {
        m_shoot.Start();
    }

    public void end() {
        m_shoot.Stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
