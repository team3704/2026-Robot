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
import pabeles.concurrency.IntOperatorTask.Max;
import frc.robot.LimelightHelpers;

import frc.robot.commands.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  //private final CommandXboxController logitech = new CommandXboxController(0);

  private final Intake intake = new Intake();
  private enum side {
    LEFT,
    MIDDLE,
    RIGHT,
  }

  public double limelightDistance;

  private final boolean DoAuto = false;
  private boolean shooterOn = false;
  private boolean climbUp = false;
  private boolean climbDown = false;

  public Command Move(double x, double y)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      {
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
        drivetrain.setControl(drive.withRotationalRate(.5));
        try {
          Thread.sleep(500);
        } catch(InterruptedException e) {
          drivetrain.setControl(drive.withRotationalRate(0.0));
        }
        drivetrain.setControl(drive.withRotationalRate(0.0));
        

      }

    }
  }

  private class AutoCommand extends Command {
    public AutoCommand(side Side) {
    if(!DoAuto) return;
    CompletableFuture<String> future = CompletableFuture.supplyAsync( () -> {
        TimedMove(0.67, 0, 700);
        while(Math.abs(limelightDistance - LimelightConstants.TargetDistance) > 5) {
          new AdjustDistance(drivetrain).schedule();
        }

        climbUp = true;

        try {
            Thread.sleep(3000);
            climbUp = false;
        } catch(InterruptedException e) {
          climbUp = false;
          Thread.currentThread().interrupt();
        }

        TimedMove(.1, 0.0, 300);

        climbDown = true;

        try {
            Thread.sleep(1500);
             climbDown = false;
        } catch(InterruptedException e) {
           climbDown = false;
          Thread.currentThread().interrupt();
        }


      return "";
    });
  }
  }

  public void doshoot() {
    if(shooterOn) {
      m_shooter.Start();
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

  
  public void DoMotor() {
    if(climbUp) {
      new ClimberCommands.ClimbUp(m_climber).schedule();
    }
    if(climbDown) {
      new ClimberCommands.ClimbDown(m_climber).schedule();
    }
  }

  private side Side = side.MIDDLE;

  // The robot's subsystems and commands are defined here...
   //private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  // private final SwerveDrivetrainConstants diahhrea = new SwerveDrivetrainConstants();
  // private TunerConstants poop = new TunerConstants();

            double kP = 0.1;
            double kI = 0.1;
            double kD = 0.1;
            PIDController distancePID = new PIDController(kP, kI, kD);

            private double OldMax = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
   private double MaxSpeed = 1.0; //* TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
   private double otherMaxSppedTrollRageBait = 4.5; 
   private double MaxAngularRate = RotationsPerSecond.of(0.35).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    
    private final Shooter m_shooter = new Shooter();
    private final Climber m_climber = new Climber();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController m_joystick = new CommandXboxController(0);
     private final CommandXboxController m_support = new CommandXboxController(1);

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
    
    m_support.b().whileTrue(new Shoot(m_shooter));
m_support.a().whileTrue(new AdjustDistance(drivetrain));
    m_support.y().whileTrue(new IntakeCommands.IntakeCommand(intake));

    m_support.x().whileTrue(new IntakeCommands.UpDeploy(intake));
    m_support.x().whileFalse(new IntakeCommands.DownDeploy(intake));

    m_support.rightTrigger().whileTrue(new IntakeCommands.EjectCommand(intake));
    m_support.povUp().whileTrue(new ClimberCommands.ClimbUp(m_climber));
    m_support.povDown().whileTrue(new ClimberCommands.ClimbDown(m_climber));
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
  public Command driveWithTime(double x, double y, double seconds)
  {
    return drivetrain.applyRequest(() ->
      drive.withVelocityX(x).withVelocityY(y)
      ).withTimeout(seconds);
  }
  
  public Command getAutonomousCommand() 
  {
      return driveWithTime(0.4, 0, 5.0)
      .andThen(driveWithTime(0.4, 0,2.0));
  }

}
