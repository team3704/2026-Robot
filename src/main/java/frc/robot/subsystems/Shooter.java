package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import com.ctre.phoenix6.swerve.utility.PhoenixPIDController;

public class Shooter extends SubsystemBase {

    private final TalonFX leftShooter;
    private final TalonFX rightShooter;

    private final VoltageOut m_voltReq = new VoltageOut(0.0);

    private PIDController Lpiddy = new PIDController(0, 0, 0);
    private PIDController Rpiddy = new PIDController(0, 0, 0);
 
    private final SysIdRoutine m_sysIdRoutine;

    private final TalonFXConfiguration shooterConfigs = new TalonFXConfiguration();
   // private final TalonFXConfiguration rightShooterConfigs = new TalonFXConfiguration();


    public double Speed = 6.0;

    public Shooter() {

        shooterConfigs.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        shooterConfigs.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0.1;

        leftShooter = new TalonFX(41);
        rightShooter = new TalonFX(1);
        
        leftShooter.getConfigurator().apply(shooterConfigs);
        rightShooter.getConfigurator().apply(shooterConfigs);

        rightShooter.setControl(new Follower(leftShooter.getDeviceID(), MotorAlignmentValue.Aligned)); //set to opposemasterdirection if theyre against eachother

/*
        m_sysIdRoutine =
   new SysIdRoutine(
      new SysIdRoutine.Config(
         null,        // Use default ramp rate (1 V/s)
         Volts.of(8), // Reduce dynamic step voltage to 4 to prevent brownout
         null,        // Use default timeout (10 s)
                      // Log state with Phoenix SignalLogger class
         (state) -> SignalLogger.writeString("state: Shooter", state.toString())
      ),
      new SysIdRoutine.Mechanism(
         (volts) -> leftShooter.setControl(m_voltReq.withOutput(volts.in(Volts))),
         null,
         this
      )
   );
*/
        // var configs = new Slot0Configs();
        // configs.kS = 2.5; // To account for friction, add 2.5 A of static feedforward
        // configs.kP = 5; // An error of 1 rotation per second results in 5 A output
        // configs.kI = 0; // No output for integrated error
        // configs.kD = 0; // No output for error derivative

        // leftShooterLeader.getConfigurator().apply(configs);
    }

    public void Start() {
       leftShooter.set(0.6); // no need to control the right motor since it follows the left
    }

    public void Stop() {
        leftShooter.stopMotor();
    }

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
    return m_sysIdRoutine.dynamic(direction);
    }
}
