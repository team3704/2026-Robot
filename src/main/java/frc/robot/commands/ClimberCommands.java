package frc.robot.commands;

import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.Command;

public class ClimberCommands {

        private static Climber m_climb;
    public static class ClimbUp extends Command {
        private Climber m_climb;
        public ClimbUp(Climber subsystem) {
            this.m_climb = subsystem;
            addRequirements(this.m_climb);
        }
        
        @Override
        public void initialize() {
            this.m_climb.Up();
        }

        public void end() {
            this.m_climb.Stop();
        }

        @Override
        public boolean isFinished()
        {
            return false;
        }

    }

    public static class ClimbDown extends Command {
        private Climber m_climb;
        public ClimbDown(Climber subsystem) {
            this.m_climb = subsystem;
            addRequirements(this.m_climb);
        }
        
        @Override
        public void initialize() {
            this.m_climb.Down();
        }

        @Override
        public void end(boolean interrupted) {
            this.m_climb.Stop();
        }

        @Override
        public boolean isFinished()
        {
            return false;
        }

    }
}
