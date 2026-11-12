package first.robot.mechanisms.printer;

import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.math.controller.PIDController;
import org.wpilib.math.system.DCMotor;
import org.wpilib.telemetry.Telemetry;

public class Printer implements Mechanism {

  String message;
  private final SparkMax m_motor = new SparkMax(CANPort.CAN_D0, 0, MotorType.kBrushless);
  private final SparkMaxSim motorSim = new SparkMaxSim(m_motor, DCMotor.getNEO(1));
  // Creates a PIDController with gains kP, kI, and kD
  PIDController pid = new PIDController(1, 0, 0);

  public Printer(String newMessage) {
    message = newMessage;
  }

  public Command printSavedMessage() {
    return this.run(
            coro -> {
              System.out.println(message);
            })
        .named("Print Saved Message");
  }

  public Command runningamoterCommand() {
    return this.run(
            coro -> {
              while (true) {
                double moterspeed = pid.calculate(motorSim.getAppliedOutput(), 0.1);
                Telemetry.log("Motor Power", moterspeed);
                motorSim.setAppliedOutput(moterspeed);
                coro.yield();
              }
            })
        .named("Running a moter command ");
  }

  public Command stoppingthemoterCommand() {
    return this.run(
            coro -> {
              motorSim.setAppliedOutput(0);
              Telemetry.log("Motor Power", 0);
            })
        .named("Stopping a moter command ");
  }
}
