package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand {
    // Store subsystem reference
    private final ShooterSubsystem shooter;
    
    // Store controller reference
    public CommandXboxController operatorXbox;
    
    // Constants for motor speeds
    private static final double INTAKE_SPEED = 0.8; // 80% speed for intake
    private static final double SHOOTING_SPEED = 0.8; // 80% speed for shooting
        
    /**
     * Constructor to initialize the command with required subsystems and controller
     */
    public ShooterCommand(ShooterSubsystem shooter, CommandXboxController operatorXbox) {
        this.shooter = shooter;
        this.operatorXbox = operatorXbox;
    }
    
    /**
     * Configure shooter commands and bind them to controller inputs
     */
    public void configureButtonBindings() {
        // Set the default command for the shooter to be controlled by triggers
        shooter.setDefaultCommand(shooter.createTriggerControlCommand(operatorXbox));
        
        // Additional button bindings examples (uncomment or modify as needed):
        // operatorXbox.a().onTrue(createShootCommand());     // A button starts shooting
        // operatorXbox.b().onTrue(createStopCommand());      // B button stops all motors
        // operatorXbox.x().onTrue(createIntakeCommand());    // X button starts intake
        // operatorXbox.y().onTrue(createTimedShootCommand(2.0)); // Y button shoots for 2 seconds
    }
    
    /**
     * Create a command that activates intake mode
     */
    public Command createIntakeCommand() {
        // Simple command that sets motors for intake
        return shooter.run(() -> {
            shooter.leftMotor.setControl(shooter.speedControl.withOutput(-INTAKE_SPEED));
            shooter.rightMotor.setControl(shooter.speedControl.withOutput(INTAKE_SPEED));
        });
    }
    
    /**
     * Create a command that activates shooting mode
     */
    public Command createShootCommand() {
        // Simple command that sets motors for shooting
        return shooter.run(() -> {
            shooter.leftMotor.setControl(shooter.speedControl.withOutput(SHOOTING_SPEED));
            shooter.rightMotor.setControl(shooter.speedControl.withOutput(-SHOOTING_SPEED));
        });
    }
    
    /**
     * Create a command that stops the shooter
     */
    public Command createStopCommand() {
        // Simple command that stops all motors
        return shooter.runOnce(() -> {
            shooter.leftMotor.setControl(shooter.speedControl.withOutput(0));
            shooter.rightMotor.setControl(shooter.speedControl.withOutput(0));
        });
    }
    
    /**
     * Create a command to shoot for a specified duration in seconds
     */
    public Command createTimedShootCommand(double durationSeconds) {
        // Run the shoot command for specified duration, then stop
        return createShootCommand()
            .withTimeout(durationSeconds)
            .andThen(createStopCommand());
    }
    
    /**
     * Create a command to intake for a specified duration in seconds
     */
    public Command createTimedIntakeCommand(double durationSeconds) {
        // Run the intake command for specified duration, then stop
        return createIntakeCommand()
            .withTimeout(durationSeconds)
            .andThen(createStopCommand());
    }
}