package first.robot;

import first.robot.mechanisms.Drive.Drive;
import first.robot.mechanisms.Drive.DriveIO;
import first.robot.mechanisms.Drive.DriveIOKitBot;
import first.robot.mechanisms.Drive.DriveIOSim;
import org.wpilib.command3.Command;
import org.wpilib.command3.button.CommandSwitch2ProController;

public class RobotContainer {

  private final Drive drive;

  private final CommandSwitch2ProController controller = new CommandSwitch2ProController(0);

  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        drive = new Drive(new DriveIOKitBot());
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        drive = new Drive(new DriveIOSim());
        break;

      default:
        // Replayed robot, disable IO implementations
        drive = new Drive(new DriveIO() {});
        break;
    }

    configureButtonBindings();
  }

  private void configureButtonBindings() {
    // Default command, normal arcade drive
    drive.setDefaultCommand(
        drive.drive(() -> -controller.getLeftY(), () -> -controller.getRightX()));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
