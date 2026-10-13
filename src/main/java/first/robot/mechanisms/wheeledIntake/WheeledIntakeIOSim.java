package first.robot.mechanisms.wheeledIntake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.math.system.DCMotor;

public class WheeledIntakeIOSim implements WheeledIntakeIO {
  private final SparkMax intakeMotor = new SparkMax(CANPort.CAN_S1, 1, MotorType.kBrushless);

  private final SparkMaxSim intakeMotorSim = new SparkMaxSim(intakeMotor, DCMotor.getNEO(1));

  public WheeledIntakeIOSim() {
    SparkMaxConfig intakeMotorConfig = new SparkMaxConfig();
    intakeMotorConfig.inverted(false);
    intakeMotorConfig.encoder.apply(new EncoderConfig());

    intakeMotor.configure(
        intakeMotorConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void setIntakeSpeed(double speed) {
    intakeMotorSim.setAppliedOutput(speed);
  }

  @Override
  public WheeledIntakeIOInputs updateInputs(WheeledIntakeIOInputs inputs) {
    inputs.intakeCurrent = intakeMotorSim.getMotorCurrent();
    inputs.intakeSpeed = intakeMotorSim.getAppliedOutput();
    return inputs;
  }
}
