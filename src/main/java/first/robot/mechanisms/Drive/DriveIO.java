package first.robot.mechanisms.Drive;

import org.littletonrobotics.junction.AutoLog;
import org.wpilib.simulation.DifferentialDrivetrainSim;

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
  }

  public default void updateInputs(DriveIOInputs inputs) {}

  public default void setVoltage(double leftVolts, double rightVolts) {}

  public default void updateSim(DifferentialDrivetrainSim drivetrainSimulator) {}
}
