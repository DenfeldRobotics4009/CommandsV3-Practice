package first.robot.mechanisms.Drive;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import first.robot.Constants.DriveConstants;
import org.wpilib.hardware.rotation.Encoder;
import org.wpilib.simulation.DifferentialDrivetrainSim;
import org.wpilib.simulation.EncoderSim;

public class DriveIOKitBot implements DriveIO {

  private final SparkMax leftLeader =
      new SparkMax(
          DriveConstants.driveCanPort, DriveConstants.leftLeaderCanId, MotorType.kBrushless);
  private final SparkMax leftFollower =
      new SparkMax(
          DriveConstants.driveCanPort, DriveConstants.leftFollowerCanId, MotorType.kBrushless);
  private final SparkMax rightLeader =
      new SparkMax(
          DriveConstants.driveCanPort, DriveConstants.rightLeaderCanId, MotorType.kBrushless);
  private final SparkMax rightFollower =
      new SparkMax(
          DriveConstants.driveCanPort, DriveConstants.rightFollowerCanId, MotorType.kBrushless);

  private final Encoder leftEncoder =
      new Encoder(DriveConstants.leftEncoderPort1, DriveConstants.leftEncoderPort2);
  private final Encoder rightEncoder =
      new Encoder(DriveConstants.rightEncoderPort1, DriveConstants.rightEncoderPort2);

  // simulator
  private final EncoderSim leftEncoderSim = new EncoderSim(leftEncoder);
  private final EncoderSim rightEncoderSim = new EncoderSim(rightEncoder);

  public DriveIOKitBot() {
    SparkMaxConfig leftLeaderConfig = new SparkMaxConfig();
    leftLeaderConfig.inverted(false);
    SparkMaxConfig leftFollowConfig = new SparkMaxConfig();
    leftFollowConfig.follow(leftLeader);

    leftLeader.configure(
        leftLeaderConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    leftFollower.configure(
        leftFollowConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    SparkMaxConfig rightLeaderConfig = new SparkMaxConfig();
    rightLeaderConfig.inverted(true);
    SparkMaxConfig rightFollowConfig = new SparkMaxConfig();
    rightFollowConfig.follow(rightLeader);

    rightLeader.configure(
        rightLeaderConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    rightFollower.configure(
        rightFollowConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

    // Set the distance per pulse for the drive encoders. We can simply use the
    // distance traveled for one rotation of the wheel divided by the encoder
    // resolution.
    leftEncoder.setDistancePerPulse(
        2 * Math.PI * DriveConstants.wheelRadiusMeters / DriveConstants.encoderResolution);
    rightEncoder.setDistancePerPulse(
        2 * Math.PI * DriveConstants.wheelRadiusMeters / DriveConstants.encoderResolution);

    leftEncoder.reset();
    rightEncoder.reset();
  }

  @Override
  public void setVoltage(double leftVolts, double rightVolts) {
    // TODO Auto-generated method stub
    leftLeader.setVoltage(leftVolts);
    rightLeader.setVoltage(rightVolts);
  }

  @Override
  public void updateInputs(DriveIOInputs inputs) {
    inputs.leftAppliedVolts = leftLeader.getBusVoltage().get();
    inputs.rightAppliedVolts = rightLeader.getBusVoltage().get();

    inputs.leftVelocityMetersPerSec = leftEncoder.getRate();
    inputs.rightVelocityMetersPerSec = rightEncoder.getRate();

    inputs.leftPositionMeters = leftEncoder.getDistance();
    inputs.rightPositionMeters = rightEncoder.getDistance();

    inputs.leftCurrentAmps = null;
    inputs.rightCurrentAmps = null;
  }

  @Override
  public void updateSim(DifferentialDrivetrainSim drivetrainSimulator) {
    leftEncoderSim.setDistance(drivetrainSimulator.getLeftPosition());
    leftEncoderSim.setRate(drivetrainSimulator.getLeftVelocity());
    rightEncoderSim.setDistance(drivetrainSimulator.getRightPosition());
    rightEncoderSim.setRate(drivetrainSimulator.getRightVelocity());
  }
}
