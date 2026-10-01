package first.robot;

import org.wpilib.command3.Command;
import org.wpilib.command3.button.CommandXboxController;

import first.robot.mechanisms.drive.Drive;
import first.robot.mechanisms.drive.DriveIOSim;
import first.robot.mechanisms.printer.Printer;

public class RobotContainer {

  Printer myPrinter;
  Drive myDrive;
  CommandXboxController controller = new CommandXboxController(0);

  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:
        break;

      case SIM:
        myPrinter = new Printer("Custome Message");
        myDrive = new Drive(new DriveIOSim());
        break;

      default:
        break;
    }

    configureButtonBindings();
  }

  private void configureButtonBindings() {
    controller.a().onTrue(myPrinter.printSavedMessage());
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
