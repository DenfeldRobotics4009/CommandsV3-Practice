package first.robot.mechanisms.wheeledIntake;

import first.robot.mechanisms.wheeledIntake.WheeledIntakeIO.WheeledIntakeIOInputs;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.telemetry.Telemetry;

public class WheeledIntake implements Mechanism {

  private final WheeledIntakeIO wheeledIntakeIO;
  private final WheeledIntakeIOInputs intakeInputs = new WheeledIntakeIOInputs();

  public WheeledIntake(WheeledIntakeIO io) {
    wheeledIntakeIO = io;
    getRegisteredScheduler().addPeriodic(() -> periodic());
  }

  public void periodic() {
    wheeledIntakeIO.updateInputs(intakeInputs);

    Telemetry.log("Intake Current", intakeInputs.intakeCurrent);
    Telemetry.log("Intake Speed", intakeInputs.intakeSpeed);
  }

  public Command Intake() {
    return this.run(
            coro -> {
              while (true) {
                wheeledIntakeIO.setIntakeSpeed(.5);
                coro.yield();
              }
            })
        .whenCanceled(
            () -> {
              wheeledIntakeIO.setIntakeSpeed(0);
            })
        .named("Run Intake");
  }

  public Command Outake() {
    return this.run(
            coro -> {
              while (true) {
                wheeledIntakeIO.setIntakeSpeed(-.5);
                coro.yield();
              }
            })
        .whenCanceled(
            () -> {
              wheeledIntakeIO.setIntakeSpeed(0);
            })
        .named("Run Outake");
  }

  public Command Stop() {
    return this.run(
            coro -> {
              wheeledIntakeIO.setIntakeSpeed(0);
            })
        .named("Run Outake");
  }
}
