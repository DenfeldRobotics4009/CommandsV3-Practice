package first.robot.mechanisms.wheeledIntake;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.AutoLogOutput;

public interface WheeledIntakeIO {

  @AutoLog
  public static class WheeledIntakeIOInputs {
    public double intakeCurrent = 0.0;
    public double intakeSpeed = 0.0;
  }

  @AutoLogOutput
  public default WheeledIntakeIOInputs updateInputs(WheeledIntakeIOInputs inputs) {
    return inputs;
  }

  public default void setIntakeSpeed(double speed) {}
}
