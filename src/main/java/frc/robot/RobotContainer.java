// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.*;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveRequest.FieldCentric;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.hardware.*;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

import frc.robot.LimelightHelpers;

import frc.robot.commands.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final CommandXboxController logitech = new CommandXboxController(0);

  private final Intake intake = new Intake();

  // The robot's subsystems and commands are defined here...
   //private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  // private final SwerveDrivetrainConstants diahhrea = new SwerveDrivetrainConstants();
  // private TunerConstants poop = new TunerConstants();

            double kP = 0.1;
            double kI = 0.1;
            double kD = 0.1;
            PIDController distancePID = new PIDController(kP, kI, kD);

   private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.RobotCentric drive = new SwerveRequest.RobotCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    
    private final Shooter m_shooter = new Shooter();
    private final Climber m_climber = new Climber();

    private final Joystick supportController = new Joystick(0);

    private final Trigger supportUpTrigger = new Trigger(() -> supportController.getRawAxis(1) > .5);
    private final Trigger supportDownTrigger = new Trigger(() -> supportController.getRawAxis(1) < .5);

    //intake
    private final JoystickButton ourpleButton = new JoystickButton(supportController, 1);

    //shoot
    private final JoystickButton redButton = new JoystickButton(supportController, 2);

    //puke
    private final JoystickButton oinkButton = new JoystickButton(supportController, 3);

    //adjust
    private final JoystickButton limeButton = new JoystickButton(supportController, 4);
    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController m_joystick = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  private void configureBindings() 
  {
    m_joystick.leftBumper().onTrue(Commands.runOnce(SignalLogger::start));
m_joystick.rightBumper().onTrue(Commands.runOnce(SignalLogger::stop));
   
/*
 * Joystick Y = quasistatic forward
 * Joystick A = quasistatic reverse
 * Joystick B = dynamic forward
 * Joystick X = dyanmic reverse
 */
m_joystick.y().whileTrue(m_shooter.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
m_joystick.a().whileTrue(m_shooter.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
m_joystick.b().whileTrue(m_shooter.sysIdDynamic(SysIdRoutine.Direction.kForward));
m_joystick.x().whileTrue(m_shooter.sysIdDynamic(SysIdRoutine.Direction.kReverse));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() 
  {
    // An example command will be run in autonomous
    return null;
  }
}
