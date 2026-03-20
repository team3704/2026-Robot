// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import frc.robot.Constants.LimelightConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.*;
import frc.robot.commands.*;

import static edu.wpi.first.units.Units.*;

import java.util.concurrent.CompletableFuture;

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
  private enum side {
    LEFT,
    MIDDLE,
    RIGHT,
  }

  public double limelightDistance;

  private final boolean DoMove = true;
  private final boolean DoAuto = true;

  public Command Move(double x, double y) {
    return drivetrain.applyRequest(() -> drive.withVelocityX(x).withVelocityY(y));
  }

  public double x = 0;
  public double y = 0;

  private void AutoMove(int y_mul) {

    //1.57 seconds to travel 5 feet (60 inches)
    //we travel at 38.21 inches per second.
    switch(this.Side) {
      default: {
        TimedMove(-0.67, 0, 4370);
        break;
      }
      case MIDDLE: {
        TimedMove(-1.0, 1.0, 1500);
        TimedMove(-1.0, 1.0, 1500);
        break;
      }

    }
  }

  private class AutoCommand extends Command {
    public AutoCommand(side Side) {
    if(!DoAuto) return;
    CompletableFuture<String> future = CompletableFuture.supplyAsync( () -> {
      switch(Side) {
      case LEFT: {
        AutoMove(1);
        break;
      }
      case MIDDLE: {
        while(Math.abs(limelightDistance - LimelightConstants.TargetDistance) > 5) {
          new AdjustDistance(drivetrain).schedule();
        }

        m_shooter.Start();
        //DOES THIS NEED TO BE CONTINOULY SET??? IDK............

        try {
            Thread.sleep(10000);
            m_shooter.Stop();
        } catch(InterruptedException e) {
          m_shooter.Stop();
          Thread.currentThread().interrupt();
        }


        break;
            }
      case RIGHT: {
        AutoMove(-1);
        break;
      }
    }
      return "";
    });
  }
  }

  private boolean TimedMove(double x, double y, int time) {
    this.x = x;
    this.y = y;

    try {
      Thread.sleep(time);
    } catch(InterruptedException e) {
      this.x = 0;
      this.y = 0;
      Thread.currentThread().interrupt();
      return false;
    }

    return true;
  }

  private side Side = side.LEFT;

  // The robot's subsystems and commands are defined here...
   //private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  // private final SwerveDrivetrainConstants diahhrea = new SwerveDrivetrainConstants();
  // private TunerConstants poop = new TunerConstants();

            double kP = 0.1;
            double kI = 0.1;
            double kD = 0.1;
            PIDController distancePID = new PIDController(kP, kI, kD);

            private double OldMax = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
   private double MaxSpeed = 1.0;// * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.35).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    
    private final Shooter m_shooter = new Shooter();
    private final Climber m_climber = new Climber();

    private final Joystick supportController = new Joystick(1);

    private final Trigger supportUpTrigger = new Trigger(() -> supportController.getRawAxis(1) > .5);
    private final Trigger supportDownTrigger = new Trigger(() -> supportController.getRawAxis(1) < -.5);

    private final IntakeCommands intakeCommands = new IntakeCommands();

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
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(m_joystick.getLeftY()) // Drive forward with negative Y (forward)
                    .withVelocityY(m_joystick.getLeftX()) // Drive left with negative X (left)
                    .withRotationalRate(-m_joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );
    

    

    redButton.whileTrue(new Shoot(m_shooter));
    ourpleButton.whileTrue(new IntakeCommands.IntakeCommand(intake));
    oinkButton.whileTrue(new IntakeCommands.UpDeploy(intake));
    oinkButton.whileFalse(new IntakeCommands.DownDeploy(intake));

    oinkButton.whileTrue(new IntakeCommands.EjectCommand(intake));
    limeButton.whileTrue(new AdjustDistance(drivetrain));

    supportUpTrigger.whileTrue(new ClimberCommands.ClimbUp(m_climber));
    supportDownTrigger.whileTrue(new ClimberCommands.ClimbDown(m_climber));
/*
 * Joystick Y = quasistatic forward
 * Joystick A = quasistatic reverse
 * Joystick B = dynamic forward
 * Joystick X = dyanmic reverse
 */
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() 
  {
      return new AutoCommand(this.Side);
    }

}
