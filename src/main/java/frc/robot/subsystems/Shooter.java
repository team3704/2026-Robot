package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.swerve.utility.PhoenixPIDController;

public class Shooter extends SubsystemBase {

    private final TalonFX leftShooter;
    private final TalonFX rightShooter;

    private TorqueCurrentFOC torqueveldude = new TorqueCurrentFOC(0);
    private PIDController Lpiddy = new PIDController(0, 0, 0);
    private PIDController Rpiddy = new PIDController(0, 0, 0);

    public double Speed = 6.0;

    public Shooter() {

        leftShooter = new TalonFX(0);
        rightShooter = new TalonFX(1);

        // var configs = new Slot0Configs();
        // configs.kS = 2.5; // To account for friction, add 2.5 A of static feedforward
        // configs.kP = 5; // An error of 1 rotation per second results in 5 A output
        // configs.kI = 0; // No output for integrated error
        // configs.kD = 0; // No output for error derivative

        // leftShooterLeader.getConfigurator().apply(configs);
    }

    public void Start() {
        // leftShooterLeader.setControl(torqueveldude.withOutput(Speed));
        leftShooter.set(Lpiddy.calculate(Speed));
        rightShooter.set(Rpiddy.calculate(Speed));

    }

    public void Stop() {
        leftShooter.stopMotor();
        rightShooter.stopMotor();
    }
}
