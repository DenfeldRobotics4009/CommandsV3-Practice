package first.robot.mechanisms.Drive;

import first.robot.mechanisms.Drive.DriveIO.DriveIOInputs;
import org.littletonrobotics.junction.AutoLogOutput;
import org.wpilib.command3.Mechanism;
import org.wpilib.math.estimator.DifferentialDrivePoseEstimator;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.linalg.VecBuilder;
import org.wpilib.math.util.Units;
import org.wpilib.telemetry.Telemetry;

public class Drive implements Mechanism {

  private final DriveIO driveIO;
  private final DriveIOInputs driveInputs = new DriveIOInputs();

  private final DifferentialDrivePoseEstimator poseEstimator =
      new DifferentialDrivePoseEstimator(
          driveInputs.yawPosition,
          driveInputs.leftPositionMeters,
          driveInputs.rightPositionMeters,
          Pose2d.ZERO,
          VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5)),
          VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(30)));

  public Drive(DriveIO newDriveIO) {
    driveIO = newDriveIO;

    getRegisteredScheduler().addPeriodic(() -> periodic());
  }

  private void updateOdometry() {
    poseEstimator.update(
        driveInputs.yawPosition, driveInputs.leftPositionMeters, driveInputs.rightPositionMeters);
  }

  public void periodic() {
    driveIO.updateInputs(driveInputs);
    updateOdometry();
    Telemetry.log("Position", getPose());
  }

  public void setPose(Pose2d pose) {
    poseEstimator.resetPosition(
        driveInputs.yawPosition,
        driveInputs.leftPositionMeters,
        driveInputs.rightPositionMeters,
        pose);
  }

  @AutoLogOutput
  public Pose2d getPose() {
    return poseEstimator.getEstimatedPosition();
  }


}
