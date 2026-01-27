package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import frc.robot.Constants.intakeConstants;

public class Intake extends SubsystemBase{

    public enum desIntakeActions {
        NONE,INTAKE,COUGH,LOCK
    }

    public enum intakeStates{
        IDLE,INTAKING,COUGHING,FULL
    }

    public double speedCapout;
    public double autoTimer;
    public double tImer = Timer.getFPGATimestamp();
    public double TimeElapse = tImer - autoTimer;
    private intakeStates intakeStatus = intakeStates.IDLE;
    private desIntakeActions desiredState = desIntakeActions.NONE;
    public final SparkMax intakeDude1 = new SparkMax(0);
    public final SparkMax intakeDude2 = new SparkMax(1);
    
 

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
    public void runStateMachine(){
        switch (intakeStatus) {
            case IDLE:
                speedCapout = 0.0;
                break;
            case INTAKING:
                speedCapout = intakeConstants.intakeSpeed;
                break;
            case COUGHING:
                speedCapout = intakeConstants.coughSpeed;
                break;
            case FULL:
                speedCapout = 0.0;
                break;
        }
    }

    public void motority(double speed){
        intakeDude1.set(speed);
        intakeDude2.set(-speed);
    }

    //Set methods to begin intaking
    public void beginIntaking(desIntakeActions intakeNeedMove)
    {
        setState(intakeNeedMove);
    }

    public double ramp()
    {
        double x = TimeElapse;
        double rampRate = ((14*(Math.log(x+0.72))+2)*0.1);
        return rampRate;
    }

    public double clampedRate(double rampRate)
    {
        double output;
        return output = Math.max(0.0, Math.min(intakeConstants.Speed, rampRate));
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
