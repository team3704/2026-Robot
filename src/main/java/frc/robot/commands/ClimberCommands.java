package frc.robot.commands;

import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.Command;

public class ClimberCommands {

    public static class ClimbUp extends Command {
        private Climber m_climb;
        public ClimbUp(Climber subsystem) {
            this.m_climb = subsystem;
            addRequirements(subsystem);
        }
        
        @Override
        public void execute() {
                        System.out.println("ok up");
            this.m_climb.Up();
        }

        @Override
        public void end(boolean interrupted) {
            System.out.println("ok done up");
            this.m_climb.Stop();
        }

        @Override
        public boolean isFinished()
        {
            System.out.println("ermmm up");
            return false;
        }

    }

    public static class ClimbDown extends Command {
        private Climber m_climb;
        public ClimbDown(Climber subsystem) {
            this.m_climb = subsystem;

            addRequirements(subsystem);
        }
        
        @Override
        public void execute() {
                        System.out.println("ok down");
            this.m_climb.Down();
        }

        @Override
        public void end(boolean interrupted) {
            System.out.println("ok end down");
            this.m_climb.Stop();
        }

        @Override
        public boolean isFinished()
        {
            System.out.println("ermmm down");
            return false;
        }

    }
}
