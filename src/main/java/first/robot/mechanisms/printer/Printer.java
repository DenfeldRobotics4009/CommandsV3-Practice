package first.robot.mechanisms.printer;

import java.util.function.Supplier;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.telemetry.Telemetry;

public class Printer implements Mechanism {

  private String printerMessage;

  public Printer(String newMessage) {
    printerMessage = newMessage;
  }

  public Command printSaved() {
    return this.run(
            coro -> {
              System.out.println(printerMessage);
            })
        .named("Saved Message");
  }

  public Command print(Supplier<String> message) {
    return this.run(
            coro -> {
              System.out.println(message.get());
            })
        .named("Print Message");
  }

  public Command printToTelemetry(Supplier<String> message) {
    return this.run(
            coro -> {
              Telemetry.log("Print", message.get());
            })
        .named("Log Message");
  }
}
