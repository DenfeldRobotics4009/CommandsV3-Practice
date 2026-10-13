package first.robot.mechanisms.printer;

import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;

public class Printer implements Mechanism {

  String message;

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
}
