package first.robot.mechanisms.drive;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.AutoLogOutput;
import org.wpilib.math.geometry.Rotation2d;

public interface DriveIO {
  @AutoLog
  public static class DriveIOInputs {
    public double leftPositionMeters = 0.0;
    public double leftVelocityMetersPerSec = 0.0;
    public double leftAppliedVolts = 0.0;
    public double[] leftCurrentAmps = new double[] {};

    public double rightPositionMeters = 0.0;
    public double rightVelocityMetersPerSec = 0.0;
    public double rightAppliedVolts = 0.0;
    public double[] rightCurrentAmps = new double[] {};

    public Rotation2d yawPosition = Rotation2d.ZERO;
    public double yawVelocityRadPerSec = 0.0;
  }

  @AutoLogOutput
  public default DriveIOInputs updateInputs(DriveIOInputs inputs) {
    return inputs;
  }

  public default void setThrottle(double leftThrottle, double rightThrottle) {}
}
