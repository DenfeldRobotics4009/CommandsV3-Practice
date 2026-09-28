package first.robot.mechanisms.drivetest;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import first.robot.Constants.DriveConstants;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.util.Units;
import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.AngularVelocity;

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

  private RelativeEncoder leftEncoder;
  private RelativeEncoder rightEncoder;
  private final Pigeon2 pigeon = new Pigeon2(DriveConstants.pigeonCanId, DriveConstants.driveCAN);
  private final StatusSignal<Angle> yaw = pigeon.getYaw();
  private final StatusSignal<AngularVelocity> yawVelocity = pigeon.getAngularVelocityZWorld();

  public DriveIOKitBot() {
    SparkMaxConfig leftLeaderConfig = new SparkMaxConfig();
    leftLeaderConfig.inverted(false);
    leftLeaderConfig.encoder.apply(new EncoderConfig());

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
    leftLeaderConfig.encoder.apply(new EncoderConfig().inverted(true));
    SparkMaxConfig rightFollowConfig = new SparkMaxConfig();
    rightFollowConfig.follow(rightLeader);

    rightLeader.configure(
        rightLeaderConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    rightFollower.configure(
        rightFollowConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

    leftEncoder = leftLeader.getEncoder();
    rightEncoder = rightLeader.getEncoder();

    pigeon.getConfigurator().apply(new Pigeon2Configuration());
    pigeon.getConfigurator().setYaw(0.0);
    BaseStatusSignal.setUpdateFrequencyForAll(50.0, yaw, yawVelocity);
    pigeon.optimizeBusUtilization();
  }

  @Override
  public DriveIOInputs updateInputs(DriveIOInputs inputs) {
    inputs.leftAppliedVolts = leftLeader.getAppliedOutput().get();
    inputs.rightAppliedVolts = rightLeader.getAppliedOutput().get();

    inputs.leftVelocityMetersPerSec = getLeftEncoderVelocityMetersPerSecond();
    inputs.rightVelocityMetersPerSec = getRightEncoderVelocityMetersPerSecond();

    inputs.leftPositionMeters = getLeftEncoderPositionMeters();
    inputs.rightPositionMeters = getRightEncoderPositionMeters();

    inputs.leftCurrentAmps = null;
    inputs.rightCurrentAmps = null;

    inputs.yawPosition = Rotation2d.fromDegrees(yaw.getValueAsDouble());
    inputs.yawVelocityRadPerSec = Units.degreesToRadians(yawVelocity.getValueAsDouble());
    return inputs;
  }

  @Override
  public void setThrottle(double leftThrottle, double rightThrottle) {
    leftLeader.setThrottle(leftThrottle);
    rightLeader.setThrottle(rightThrottle);
  }

  private double getLeftEncoderPositionMeters() {
    return leftEncoder.getPosition().get() * 2 * Math.PI * DriveConstants.wheelRadiusMeters;
  }

  private double getRightEncoderPositionMeters() {
    return rightEncoder.getPosition().get() * 2 * Math.PI * DriveConstants.wheelRadiusMeters;
  }

  private double getLeftEncoderVelocityMetersPerSecond() {
    return (leftEncoder.getVelocity().get() / 60) * 2 * Math.PI * DriveConstants.wheelRadiusMeters;
  }

  private double getRightEncoderVelocityMetersPerSecond() {
    return (rightEncoder.getVelocity().get() / 60) * 2 * Math.PI * DriveConstants.wheelRadiusMeters;
  }
}
