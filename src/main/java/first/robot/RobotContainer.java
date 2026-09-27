package first.robot;

import first.robot.mechanisms.drive.Drive;
import first.robot.mechanisms.drive.DriveIO;
import first.robot.mechanisms.drive.DriveIOKitBot;
import first.robot.mechanisms.drive.DriveIOSim;
import first.robot.mechanisms.printer.Printer;
import org.wpilib.command3.Command;
import org.wpilib.command3.button.CommandSwitch2ProController;

public class RobotContainer {

  private final Drive drive;
  private final Printer printer;

  private final CommandSwitch2ProController controller = new CommandSwitch2ProController(0);

  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        drive = new Drive(new DriveIOKitBot());
        printer = new Printer("On Robot!");
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        drive = new Drive(new DriveIOSim());
        printer = new Printer("In Simulation!");
        break;

      default:
        // Replayed robot, disable IO implementations
        drive = new Drive(new DriveIO() {});
        printer = new Printer("Replay");
        break;
    }

    configureButtonBindings();
  }

  private void configureButtonBindings() {
    // Default command, normal arcade drive
    drive.setDefaultCommand(
        drive.drive(() -> -controller.getLeftY(), () -> -controller.getRightX()));
    controller.a().onTrue(printer.printSaved());
    controller.b().onTrue(printer.print(() -> "B Pressed!"));
    controller.x().onTrue(printer.printToTelemetry(() -> "X Pressed!"));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
