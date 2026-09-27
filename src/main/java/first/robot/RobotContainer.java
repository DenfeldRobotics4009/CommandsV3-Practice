package first.robot;

import org.wpilib.command3.Command;

public class RobotContainer {
  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:
        break;

      case SIM:
        break;

      default:
        break;
    }

    configureButtonBindings();
  }

  private void configureButtonBindings() {
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
