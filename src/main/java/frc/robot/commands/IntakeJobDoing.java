package frc.robot.commands;

import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

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
    m_intake.startIntaking
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
