package frc.robot.commands;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.desIntakeActions;
import frc.robot.subsystems.Intake.intakeStates;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.Timer;

public class IntakeJobDoing extends Command {

    private final Intake m_intake;

    public IntakeJobDoing(Intake bigI) {
    m_intake = bigI;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(bigI);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
   m_intake.beginIntaking(desIntakeActions.NONE);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   m_intake.beginIntaking(desIntakeActions.INTAKE);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intake.beginIntaking(desIntakeActions.NONE);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
