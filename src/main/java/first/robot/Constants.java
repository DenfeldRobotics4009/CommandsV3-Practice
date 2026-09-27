// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package first.robot;

import com.ctre.phoenix6.CANBus;
import org.wpilib.framework.RobotBase;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.math.system.DCMotor;
import org.wpilib.math.util.Units;

/**
 * This class defines the runtime mode used by AdvantageKit. The mode is always "real" when running
 * on a roboRIO. Change the value of "simMode" to switch between "sim" (physics sim) and "replay"
 * (log replay from a file).
 */
public final class Constants {
  public static final Mode simMode = Mode.SIM;
  public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,

    /** Replaying from a log file. */
    REPLAY
  }

  public class DriveConstants {
    public static CANPort driveCanPort = CANPort.CAN_S0;
    public static CANBus driveCAN = new CANBus(driveCanPort);
    public static final double maxSpeedMetersPerSec = 4.0;
    public static final double trackWidth = Units.inchesToMeters(26.0);
    public static final int encoderResolution = 4096;

    // Device CAN IDs
    public static final int pigeonCanId = 9;
    public static final int leftLeaderCanId = 1;
    public static final int leftFollowerCanId = 2;
    public static final int rightLeaderCanId = 3;
    public static final int rightFollowerCanId = 4;

    // device inputs
    public static final int leftEncoderPort1 = 1;
    public static final int leftEncoderPort2 = 2;
    public static final int rightEncoderPort1 = 3;
    public static final int rightEncoderPort2 = 4;

    // Motor configuration
    public static final int currentLimit = 60;
    public static final double wheelRadiusMeters = Units.inchesToMeters(3.0);
    public static final double motorReduction = 10.71;
    public static final boolean leftInverted = false;
    public static final boolean rightInverted = true;
    public static final DCMotor gearbox = DCMotor.getCIM(2);

    // Velocity PID configuration
    public static final double realKp = 0.0;
    public static final double realKd = 0.0;
    public static final double realKs = 0.0;
    public static final double realKv = 0.1;

    public static final double simKp = 0.05;
    public static final double simKd = 0.0;
    public static final double simKs = 0.0;
    public static final double simKv = 0.227;
  }
}
