package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import frc.robot.subsystems.Intake;

public class IntakeCommands 
{
    public static class IntakeCommand extends Command
    {
        private final Intake intake;

        public IntakeCommand(Intake intake)
        {
            this.intake = intake;
            addRequirements(intake);
        }

        @Override
        public void initialize()
        {
            intake.startIntaking();
        }
        @Override
        public void end(boolean interrupted)
        {
            intake.stopIntaking();
        }
        @Override
        public boolean isFinished()
        {
            return false;
        }

    }
    /**
     * EJECT BALLS
     */
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


    }


}
