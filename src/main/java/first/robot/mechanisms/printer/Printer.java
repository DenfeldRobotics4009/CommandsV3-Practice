package first.robot.mechanisms.printer;

import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;

public class Printer implements Mechanism{
  
  String message;
  public Printer(String newMessage){
    message = newMessage;
  }

  public Command printSavedMessage(){
    return this.run(
      coro -> {
        //initialize
        System.out.println(message);
        while(1== 1){ //is finished
          //peodic
          coro.yield();
        }
        //end
      }
    ).named("Print Saved Message");
  }
}
