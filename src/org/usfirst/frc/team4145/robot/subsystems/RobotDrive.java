package org.usfirst.frc.team4145.robot.subsystems;

import org.usfirst.frc.team4145.robot.motionprofile.Constants;
import org.usfirst.frc.team4145.robot.motionprofile.MotionProfileFollower;
import org.usfirst.frc.team4145.robot.motionprofile.MotionProfileReader;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class RobotDrive extends Subsystem {

    /** The Talons we want to motion profile. */
    TalonSRX leftMasterTalon = new TalonSRX(0);
    TalonSRX rightMasterTalon = new TalonSRX(1);
    TalonSRX leftSlaveTalon = new TalonSRX(2);
    TalonSRX rightSlaveTalon = new TalonSRX(3);

    MotionProfileFollower leftMotionProfile;
    MotionProfileFollower rightMotionProfile;

    boolean motionProfileEnabled = false;
    boolean motionProfileStarted = false;

    public void initDefaultCommand() {

    }

    public void enableMotionProfile(MotionProfileReader leftReader, MotionProfileReader rightReader) {

        this.leftMotionProfile = new MotionProfileFollower(leftMasterTalon, leftReader);
        this.rightMotionProfile = new MotionProfileFollower(rightMasterTalon, rightReader);

        configTalonPID(leftMasterTalon);
        configTalonPID(rightMasterTalon);

        leftSlaveTalon.follow(leftMasterTalon);
        rightSlaveTalon.follow(rightMasterTalon);
        
        motionProfileEnabled = true;
    }

    private void configTalonPID(TalonSRX talon) {
        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        talon.setSensorPhase(true); /* keep sensor and motor in phase */
        talon.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);

        talon.config_kF(0, 0.076, Constants.kTimeoutMs);
        talon.config_kP(0, 2.000, Constants.kTimeoutMs);
        talon.config_kI(0, 0.0, Constants.kTimeoutMs);
        talon.config_kD(0, 20.0, Constants.kTimeoutMs);

        /* Our profile uses 10ms timing */
        talon.configMotionProfileTrajectoryPeriod(10, Constants.kTimeoutMs);
        /*
         * status 10 provides the trajectory target for motion profile AND
         * motion magic
         */
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);
    }

    public void disableMotionProfile() {
        motionProfileEnabled = false;
        motionProfileStarted = false;
        leftMasterTalon.set(ControlMode.PercentOutput, 0);
        leftSlaveTalon.set(ControlMode.PercentOutput, 0);
        leftMotionProfile.reset();
        rightMasterTalon.set(ControlMode.PercentOutput, 0);
        rightSlaveTalon.set(ControlMode.PercentOutput, 0);
        rightMotionProfile.reset();
    }

    @Override
    public void periodic() {
        /*
         * call this periodically, and catch the output. Only apply it if user
         * wants to run MP.
         */
        if (leftMotionProfile != null) {
            leftMotionProfile.control();
        }
        if (rightMotionProfile != null) {
            rightMotionProfile.control();
        }

        if (motionProfileEnabled && leftMotionProfile != null && rightMotionProfile != null) {
            SetValueMotionProfile leftSetOutput = leftMotionProfile.getSetValue();
            leftMasterTalon.set(ControlMode.MotionProfile, leftSetOutput.value);

            SetValueMotionProfile rightSetOutput = leftMotionProfile.getSetValue();
            rightMasterTalon.set(ControlMode.MotionProfile, rightSetOutput.value);

            /*
             * Start the motion profile only once
             */
            if (!motionProfileStarted) {
                leftMotionProfile.startMotionProfile();
                rightMotionProfile.startMotionProfile();
                motionProfileStarted = true;
            }
        }
    }
}
