package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX; // Import for Falcon/Kraken motors
import com.ctre.phoenix6.controls.DutyCycleOut; // Import for percentage output control
import edu.wpi.first.wpilibj2.command.Command; // Import for Command creation
import edu.wpi.first.wpilibj2.command.SubsystemBase; // Base class for subsystems
import edu.wpi.first.wpilibj2.command.button.CommandXboxController; // Import for Xbox controller

public class ShooterSubsystem extends SubsystemBase {
    
    // Define motor CAN IDs
    private static final int LEFT_MOTOR_ID = 17; // CAN ID for left motor
    private static final int RIGHT_MOTOR_ID = 18; // CAN ID for right motor
    
    // Define motor speed values (0.0 to 1.0)
    private static final double INTAKE_SPEED = 0.8; // 80% speed for intake
    private static final double SHOOTING_SPEED = 0.8; // 80% speed for shooting
    private static final double TRIGGER_THRESHOLD = 0.1; // Minimum trigger value to activate

    // Create motor objects (public for direct access from commands)
    public final TalonFX leftMotor = new TalonFX(LEFT_MOTOR_ID); // Initialize left motor
    public final TalonFX rightMotor = new TalonFX(RIGHT_MOTOR_ID); // Initialize right motor
    
    // Create duty cycle (percentage) control object (public for direct access from commands)
    public final DutyCycleOut speedControl = new DutyCycleOut(0); // Initialize with 0% output
    
    /**
     * Constructor - Just initializes the motors
     */
    public ShooterSubsystem() {
        // No additional configuration needed for simplicity
    }
    
    /**
     * Creates a command that runs the shooter based on controller triggers
     */
    public Command createTriggerControlCommand(CommandXboxController controller) {
        // Return a command that continuously checks trigger values
        return run(() -> {
            // Read trigger values
            double leftTrigger = controller.getLeftTriggerAxis(); // For intake
            double rightTrigger = controller.getRightTriggerAxis(); // For shooting
            
            // Right trigger pressed - SHOOTING MODE
            if (rightTrigger > TRIGGER_THRESHOLD) {
                // Set left motor forward
                leftMotor.setControl(speedControl.withOutput(SHOOTING_SPEED)); 
                // Set right motor backward
                rightMotor.setControl(speedControl.withOutput(-SHOOTING_SPEED));
            }
            // Left trigger pressed - INTAKE MODE 
            else if (leftTrigger > TRIGGER_THRESHOLD) {
                // Set left motor backward
                leftMotor.setControl(speedControl.withOutput(-INTAKE_SPEED));
                // Set right motor forward
                rightMotor.setControl(speedControl.withOutput(INTAKE_SPEED));
            }
            // No triggers pressed - STOP MOTORS
            else {
                // Stop left motor
                leftMotor.setControl(speedControl.withOutput(0));
                // Stop right motor
                rightMotor.setControl(speedControl.withOutput(0));
            }
        });
    }
}