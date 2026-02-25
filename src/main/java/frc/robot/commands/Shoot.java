package frc.robot.commands;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;

public class Shoot extends Command {

    private Shooter m_shoot;
    private TorqueCurrentFOC torqueveldude = new TorqueCurrentFOC(0);

    public Shoot(Shooter subsystem) {
        m_shoot = subsystem;
        addRequirements(subsystem);

        var configs = new Slot0Configs();
        configs.kS = 2.5; // To account for friction, add 2.5 A of static feedforward
        configs.kP = 5; // An error of 1 rotation per second results in 5 A output
        configs.kI = 0; // No output for integrated error
        configs.kD = 0; // No output for error derivative
        // Peak output of 40 A
        configs.withPeakForwardTorqueCurrent(Amps.of(40))
            .withPeakReverseTorqueCurrent(Amps.of(-40));
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
