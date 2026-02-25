package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {

    private final TalonFX leftShooterLeader;
    private final TalonFX rightShooterFollower;

    private TorqueCurrentFOC torqueveldude = new TorqueCurrentFOC(0);

    public double Speed = 6.0;

    public Shooter() {

        leftShooterLeader = new TalonFX(0);
        rightShooterFollower = new TalonFX(1);
        
        rightShooterFollower.setControl(new Follower(0, MotorAlignmentValue.Aligned));
        
        var configs = new Slot0Configs();
        configs.kS = 2.5; // To account for friction, add 2.5 A of static feedforward
        configs.kP = 5; // An error of 1 rotation per second results in 5 A output
        configs.kI = 0; // No output for integrated error
        configs.kD = 0; // No output for error derivative

        leftShooterLeader.getConfigurator().apply(configs);
    }

    public void Start() {
        leftShooterLeader.setControl(torqueveldude.withOutput(Speed));
    }

    public void Stop() {
        leftShooterLeader.stopMotor();
    }
}
