package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.Timer;

public class Intake extends SubsystemBase
{
    private final TalonFX leftIntakeLeader;
   // private final TalonFX rightIntakeFollower;
    private final TalonFXConfiguration intakeConfigs = new TalonFXConfiguration();

    private IntakeState currentState = IntakeState.IDLE;

    private final DutyCycleOut dutyCycleRequest = new DutyCycleOut(0); 

    private final double intakeStatorCurrentLimitValue = 60.0;
    private final double intakeSupplyCurrentLimitValue = 40.0;

    private final double jamCurrentThreshold = 45.0;
    private final int jamDetectionCountCycles = 50; //stator current is "noisy" when motor starts, current spikes so the code might think there is a jam when motor begins
    private final double jamVoltageThreshold = 2.0;
    
    private int jamCount;

    private final double intakeSpeed = 1;
    private final double ejectSpeed = -0.8;
    private final double holdingSpeed = 0.10;

    private final Timer recoveryTimer = new Timer();
    private final double recoveryTimerSeconds = 0.75; 


    public enum IntakeState{
        IDLE,
        INTAKING,
        EJECTING,
        JAMMED,
        HOLDING,
        RECOVERING
    }
    /*
     * The intake system will be open loop. Open loop means there is no 
     * sensor measuring the ouput of said system. You wouldnt need 
     * a sensor measuring the output of the intake subsystem, but you
     * would for a shooter(PID)
     */
    public Intake()
    {
      leftIntakeLeader = new TalonFX(39);
      //rightIntakeFollower = new TalonFX(16);

      intakeConfigs.MotorOutput.NeutralMode = NeutralModeValue.Coast;
      intakeConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    
      //Current limits to protect motor and battery
      intakeConfigs.CurrentLimits.StatorCurrentLimit = intakeStatorCurrentLimitValue; // In amps, limits the current circulating in the motor, sets a punch to getr game piece but not enough to melt internal insulation
      intakeConfigs.CurrentLimits.StatorCurrentLimitEnable = true; // restrict the actual current drawn by the motor, reducing heat and prevent stalling
      intakeConfigs.CurrentLimits.SupplyCurrentLimit = intakeSupplyCurrentLimitValue; //protects battery/breaker
      intakeConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;

      //Ramping mechanism
      intakeConfigs.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0.5; //0.5 secs to reach 0% to 100% output

      //Apply the leftConfigs into the leftIntakeLeader motor
      leftIntakeLeader.getConfigurator().apply(intakeConfigs);

      //You might need to change the direction of the follower motor, if so
      //leftConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Negative;
     // rightIntakeFollower.getConfigurator().apply(intakeConfigs);
      //rightIntakeFollower.setControl(new Follower(leftIntakeLeader.getDeviceID(), MotorAlignmentValue.Aligned)); //motorAlignmentValue aligned means that the follower matches the leaders directions

    }
    @Override
    public void periodic()
    {
        detectJam();
        executeState();
    }
    /**
     * Jam detected when:
     * Motor is commanded to INTAKE (INtAKE STAETE)
     * Current draw is high
     * or voltage is low despite high command
     * and this condition continues for multiple cycles
     */
    public void detectJam()
    {
        if(currentState == IntakeState.INTAKING)
        {
            double leaderStatorCurrent = leftIntakeLeader.getStatorCurrent().getValueAsDouble();
            double leaderVoltage = leftIntakeLeader.getMotorVoltage().getValueAsDouble();
            double leaderOutput = leftIntakeLeader.get();
            System.out.println(leaderStatorCurrent);

            if(leaderStatorCurrent > jamCurrentThreshold)
            {
                jamCount++;
                if(jamCount >= jamDetectionCountCycles)
                {
                    System.out.println("Potential jam detected \n Self healing...");
                    recoveryTimer.reset();
                    recoveryTimer.start();
                    setState(IntakeState.RECOVERING);

                }
        
            }
            else
            {
                jamCount = 0;
            }
            
      }

    }
    private void executeState()
    {
        switch (currentState) 
        {
        
            case INTAKING:
                leftIntakeLeader.setControl(dutyCycleRequest.withOutput(intakeSpeed));
            break;
            
            case EJECTING:
                leftIntakeLeader.setControl(dutyCycleRequest.withOutput(ejectSpeed));
            break;

            case HOLDING:
                //Small holding power to keep ball in place
                leftIntakeLeader.setControl(dutyCycleRequest.withOutput(holdingSpeed));
            break;

            case JAMMED:
                leftIntakeLeader.setControl(dutyCycleRequest.withOutput(0));
            break;

            case RECOVERING:
                leftIntakeLeader.setControl(dutyCycleRequest.withOutput(ejectSpeed));

                if(recoveryTimer.hasElapsed(recoveryTimerSeconds))
                {
                    recoveryTimer.stop();
                    jamCount = 0;
                    setState(IntakeState.INTAKING);
                    System.out.println("Successfully Recovered");
                }
            break;

            case IDLE:
            default:
                leftIntakeLeader.setControl(dutyCycleRequest.withOutput(0));
            break;
        }
    }
    private void setState(IntakeState newState)
    {
        if(currentState != newState)
        {
            System.err.println("Intake state: "+ currentState + " -> " + newState);
            recoveryTimer.stop();
            recoveryTimer.reset();

            currentState = newState;
        }
    }

    public void startIntaking()
    {
        if(currentState != IntakeState.JAMMED)
        {
            jamCount = 0;
            setState(IntakeState.INTAKING);
        }
        else
        {
            System.out.println("Cant intake yet. System JAMMED");
        }
    }

    public void stopIntaking() //MIGHT HAVE TO DO SOME FIXING HERE
    {
        if(currentState == IntakeState.INTAKING)
        {
            setState(IntakeState.IDLE);
        }
    }

    public void startEjecting()
    {
        setState(IntakeState.EJECTING);
    }

    public void setHolding()
    {
        setState(IntakeState.HOLDING);
    }

    public void clearJam()
    {
        if(currentState == IntakeState.JAMMED)
        {
            jamCount = 0;
            setState(IntakeState.IDLE);
        }
    }
    
    public IntakeState getState()
    {
        return currentState;
    }
    
    public boolean isJammed()
    {
        return currentState == IntakeState.JAMMED;
    }

    public double getLeaderCurrent()
    {
        return leftIntakeLeader.getStatorCurrent().getValueAsDouble();
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