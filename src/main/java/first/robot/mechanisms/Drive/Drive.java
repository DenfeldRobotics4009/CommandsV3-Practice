package first.robot.mechanisms.Drive;

import first.robot.Constants;
import first.robot.Constants.DriveConstants;
import first.robot.Constants.Mode;
import first.robot.mechanisms.Drive.DriveIO.DriveIOInputs;
import first.robot.mechanisms.Drive.GyroIO.GyroIOInputs;
import java.util.function.Supplier;
import org.littletonrobotics.junction.AutoLogOutput;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.math.estimator.DifferentialDrivePoseEstimator;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.kinematics.DifferentialDriveKinematics;
import org.wpilib.math.linalg.VecBuilder;
import org.wpilib.math.numbers.N2;
import org.wpilib.math.system.DCMotor;
import org.wpilib.math.system.LinearSystem;
import org.wpilib.math.system.Models;
import org.wpilib.math.util.Units;
import org.wpilib.simulation.DifferentialDrivetrainSim;
import org.wpilib.system.RobotController;

public class Drive implements Mechanism {
  private final DriveIO io;
  private final GyroIO gyroIO;
  private final DriveIOInputs driveInputs = new DriveIOInputs();
  private final GyroIOInputs gyroInputs = new GyroIOInputs();

  private final DifferentialDriveKinematics kinematics =
      new DifferentialDriveKinematics(DriveConstants.trackWidth);

  private final DifferentialDrivePoseEstimator poseEstimator =
      new DifferentialDrivePoseEstimator(
          gyroInputs.yawPosition,
          driveInputs.leftPositionMeters,
          driveInputs.rightPositionMeters,
          Pose2d.ZERO,
          VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5)),
          VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(30)));

  private final LinearSystem<N2, N2, N2> drivetrainSystem =
      Models.differentialDriveFromSysId(1.98, 0.2, 1.5, 0.3);
  private final DifferentialDrivetrainSim drivetrainSimulator =
      new DifferentialDrivetrainSim(
          drivetrainSystem,
          DCMotor.getCIM(2),
          8,
          DriveConstants.trackWidth,
          DriveConstants.wheelRadiusMeters,
          null);

  public Drive(DriveIO newDriveIO, GyroIO newGyroIO) {
    io = newDriveIO;
    gyroIO = newGyroIO;

    getRegisteredScheduler().addPeriodic(() -> periodic());
  }

  public void simulationPeriodic() {
    drivetrainSimulator.setInputs(
        driveInputs.leftAppliedVolts * RobotController.getInputVoltage(),
        driveInputs.rightAppliedVolts * RobotController.getInputVoltage());
    drivetrainSimulator.update(0.02);

    io.updateSim(drivetrainSimulator);
    gyroIO.setYaw(drivetrainSimulator.getHeading().getDegrees());
  }

  private void updateOdometry() {
    poseEstimator.update(
        gyroInputs.yawPosition, driveInputs.leftPositionMeters, driveInputs.rightPositionMeters);
  }

  public void periodic() {
    if (Constants.currentMode == Mode.SIM) {
      simulationPeriodic();
    }
    updateOdometry();
    getPose();
  }

  public void setPose(Pose2d pose) {
    poseEstimator.resetPosition(
        gyroInputs.yawPosition,
        driveInputs.leftPositionMeters,
        driveInputs.rightPositionMeters,
        pose);
  }

  @AutoLogOutput
  public Pose2d getPose() {
    return poseEstimator.getEstimatedPosition();
  }

  public Command drive(Supplier<Double> forwardSupplier, Supplier<Double> turnSupplier) {
    return this.run(
            coro -> {
              while (true) {
                System.out.println("Drive");
                io.setVoltage(
                    forwardSupplier.get() - turnSupplier.get(),
                    forwardSupplier.get() + turnSupplier.get());
                coro.yield();
              }
            })
        .withPriority(Command.LOWEST_PRIORITY)
        .named("Drive");
  }
}
