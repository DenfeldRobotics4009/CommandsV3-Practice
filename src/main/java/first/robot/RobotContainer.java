package first.robot;

import first.robot.mechanisms.drive.Drive;
import first.robot.mechanisms.drive.DriveIOKitBot;
import first.robot.mechanisms.drive.DriveIOSim;
import first.robot.mechanisms.printer.Printer;
import first.robot.mechanisms.wheeledIntake.WheeledIntake;
import first.robot.mechanisms.wheeledIntake.WheeledIntakeIOSim;
import org.wpilib.command3.Command;
import org.wpilib.command3.button.CommandSwitchProController;

public class RobotContainer {

  private Printer printer;

  private Drive drive;

  private WheeledIntake wheeledIntake;

  private CommandSwitchProController controller = new CommandSwitchProController(0);

  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:
        drive = new Drive(new DriveIOKitBot());
        break;

      case SIM:
        printer = new Printer("HIIIIIIIIIIIII!!!!!!!!!!!!!!!!");
        drive = new Drive(new DriveIOSim());
        wheeledIntake = new WheeledIntake(new WheeledIntakeIOSim());
        break;

      default:
        break;
    }

    configureButtonBindings();
  }

  private void configureButtonBindings() {
    controller.a().onTrue(printer.printSavedMessage());
    controller.L().onTrue(wheeledIntake.Outake());
    controller.L().onFalse(wheeledIntake.Stop());
    controller.R().onTrue(wheeledIntake.Intake());
    controller.R().onFalse(wheeledIntake.Stop());
    drive.setDefaultCommand(
        drive.drive(() -> -controller.getLeftY(), () -> -controller.getRightX()));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
