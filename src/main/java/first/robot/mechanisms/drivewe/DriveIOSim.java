package first.robot.mechanisms.drivewe;

import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import first.robot.Constants.DriveConstants;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.numbers.N2;
import org.wpilib.math.system.DCMotor;
import org.wpilib.math.system.LinearSystem;
import org.wpilib.math.system.Models;
import org.wpilib.simulation.DifferentialDrivetrainSim;
import org.wpilib.system.RobotController;

public class DriveIOSim implements DriveIO {
  // copied from Real
  private final SparkMax leftLeader =
      new SparkMax(
          DriveConstants.driveCanPort, DriveConstants.leftLeaderCanId, MotorType.kBrushless);

  private final SparkMax rightLeader =
      new SparkMax(
          DriveConstants.driveCanPort, DriveConstants.rightLeaderCanId, MotorType.kBrushless);

  // simulator
  private final SparkMaxSim leftMaxSim = new SparkMaxSim(leftLeader, DCMotor.getNEO(2));
  private final SparkMaxSim rightMaxSim = new SparkMaxSim(rightLeader, DCMotor.getNEO(2));

  private final LinearSystem<N2, N2, N2> drivetrainSystem =
      Models.differentialDriveFromSysId(1.98, 0.2, 4, 0.3);
  private final DifferentialDrivetrainSim drivetrainSimulator =
      new DifferentialDrivetrainSim(
          drivetrainSystem,
          DCMotor.getCIM(2),
          8,
          DriveConstants.trackWidth,
          DriveConstants.wheelRadiusMeters,
          null);

  public DriveIOSim() {}

  @Override
  public void setThrottle(double leftThrottle, double rightThrottle) {
    leftMaxSim.setAppliedOutput(leftThrottle);
    rightMaxSim.setAppliedOutput(rightThrottle);
    return;
  }

  @Override
  public DriveIOInputs updateInputs(DriveIOInputs inputs) {

    drivetrainSimulator.setInputs(
        inputs.leftAppliedVolts * RobotController.getInputVoltage(),
        inputs.rightAppliedVolts * RobotController.getInputVoltage());
    drivetrainSimulator.update(0.02);

    inputs.leftAppliedVolts = leftMaxSim.getAppliedOutput();
    inputs.rightAppliedVolts = rightMaxSim.getAppliedOutput();
    inputs.leftPositionMeters = drivetrainSimulator.getLeftPosition();
    inputs.rightPositionMeters = drivetrainSimulator.getRightPosition();
    inputs.leftVelocityMetersPerSec = drivetrainSimulator.getLeftVelocity();
    inputs.rightVelocityMetersPerSec = drivetrainSimulator.getRightVelocity();

    inputs.yawPosition = Rotation2d.fromDegrees(drivetrainSimulator.getHeading().getDegrees());
    inputs.yawVelocityRadPerSec = 0;

    inputs.leftCurrentAmps = null;
    inputs.rightCurrentAmps = null;

    return inputs;
  }
}
