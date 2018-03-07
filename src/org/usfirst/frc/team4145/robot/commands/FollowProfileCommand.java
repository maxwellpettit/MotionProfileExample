package org.usfirst.frc.team4145.robot.commands;

import org.usfirst.frc.team4145.robot.Robot;
import org.usfirst.frc.team4145.robot.motionprofile.MotionProfileReader;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class FollowProfileCommand extends Command {

    private MotionProfileReader leftReader;
    private MotionProfileReader rightReader;

    public FollowProfileCommand(String left, String right) {
        requires(Robot.robotDrive);
        leftReader = new MotionProfileReader(left);
        rightReader = new MotionProfileReader(right);
    }

    // Called just before this Command runs the first time
    protected void initialize() {

        Robot.robotDrive.enableMotionProfile(leftReader, rightReader);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.robotDrive.disableMotionProfile();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
