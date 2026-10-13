package first.robot.mechanisms.wheeledIntake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import org.wpilib.hardware.bus.CANPort;

public class WheeledIntakeIOKitBot implements WheeledIntakeIO {
  private final SparkMax intakeMotor = new SparkMax(CANPort.CAN_S1, 1, MotorType.kBrushless);

  public WheeledIntakeIOKitBot() {
    SparkMaxConfig intakeMotorConfig = new SparkMaxConfig();
    intakeMotorConfig.inverted(false);
    intakeMotorConfig.encoder.apply(new EncoderConfig());

    intakeMotor.configure(
        intakeMotorConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void setIntakeSpeed(double speed) {
    intakeMotor.setThrottle(speed);
  }

  @Override
  public WheeledIntakeIOInputs updateInputs(WheeledIntakeIOInputs inputs) {
    inputs.intakeCurrent = intakeMotor.getOutputCurrent().get();
    inputs.intakeSpeed = intakeMotor.getAppliedOutput().get();
    return inputs;
  }
}
