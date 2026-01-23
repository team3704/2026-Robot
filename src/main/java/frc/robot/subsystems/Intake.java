package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import frc.robot.Constants.intakeConstants;


public class Intake extends SubsystemBase{

    public enum desIntakeActions {
        NONE,INTAKE,COUGH,LOCK
    }

    public enum intakeStates{
        IDLE,INTAKING,COUGHING,FULL
    }

    public double motorDirector;
    private intakeStates intakeStatus = intakeStates.IDLE;
    private desIntakeActions desiredState = desIntakeActions.NONE;
    public final PWMSparkMax intakeDude1 = new PWMSparkMax(0);
    public final PWMSparkMax intakeDude2 = new PWMSparkMax(1);

    private Intake() {
        //intakeDude1.disable();
        //intakeDude2.disable();
    }

    public void setState(desIntakeActions wantedState){
        switch(wantedState){
                case NONE:
                    intakeStatus = intakeStates.IDLE;
                    runStateMachine();
                    break;
                case INTAKE:
                    intakeStatus = intakeStates.INTAKING;
                    runStateMachine();
                    break;
                case COUGH:
                    intakeStatus = intakeStates.COUGHING;
                    runStateMachine();
                    break;
                case LOCK:
                    intakeStatus = intakeStates.FULL;
                    runStateMachine();
                    break;
        }
    }
    /** Also known as the execute state method. */
    private void runStateMachine(){
        switch (intakeStatus) {
            case IDLE:
                motorDirector = 0.0;
                motority();
                break;
            case INTAKING:
                motorDirector = intakeConstants.intakeSpeed;
                motority();

                break;
            case COUGHING:
                motorDirector = intakeConstants.coughSpeed;
                motority();
                break;
            case FULL:
                motorDirector = 0.0;
                motority();
                break;
        }
    }

    public void motority(){
        intakeDude1.set(motorDirector);
        intakeDude2.set(motorDirector);
    }

    //Set methods to begin intaking
    public void beginIntaking()
    {
        desiredState = desIntakeActions.INTAKE;
        setState(desiredState);
    }
}

/*
                67       6767676767676767
              67                      67
            67                     67
           67                    67
          67 676767            67
         67        67         67
         67        67         67
         67        67         67
           67676767           67
*/
