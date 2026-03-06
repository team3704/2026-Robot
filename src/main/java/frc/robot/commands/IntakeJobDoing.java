package frc.robot.commands;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.Timer;

public class IntakeJobDoing extends Command 
{
  public static class IntakeCommand extends Command
    {
      private final Intake m_intake;

      public IntakeCommand(Intake bigI) 
      {
        m_intake = bigI;
        addRequirements(bigI);
      }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
      m_intake.startIntaking();
    }
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) 
    {
      m_intake.stopIntaking();
    }
    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
      return false;
    }
  }

  public static class EjectCommand extends Command 
  {
    private final Intake intake;

    public EjectCommand(Intake intake)
    {
      this.intake = intake;
      addRequirements(intake);
    }

    @Override
    public void initialize()
    {
      intake.startEjecting();
    }

    @Override
    public void end(boolean interrupted)
    {
      intake.stopIntaking();
    }
  }
}
