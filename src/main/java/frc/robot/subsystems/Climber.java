package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

    private final TalonFX climbMotor;
    public final double Speed = .5;
    private final TalonFXConfiguration climbConfig = new TalonFXConfiguration().withCurrentLimits(new CurrentLimitsConfigs().withStatorCurrentLimit(15.0));

    public Climber() {
        
        climbMotor = new TalonFX(39);
        climbMotor.getConfigurator().apply(climbConfig);
        climbMotor.setNeutralMode(NeutralModeValue.Brake);
    }

    public void Up() {
        climbMotor.set(Speed);
    }

    public void Down() {
        climbMotor.set(-Speed);
    }

    public void Stop() {
        climbMotor.stopMotor();
    }
}
