package org.firstinspires.ftc.teamcode.team.Merlin1617.Merlin3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.team.Merlin1617.Merlin3.Merlin3AutoMethods;

/*
 * My cases are:
 *
 * Wait
 * Go forward away from wall
 * Turn
 * Shoot Ball
 * Load Ball
 * Shoot Ball
 * Drive Forward
 *
 */
@Autonomous(name = "Blue LCAPO", group = "Merlin3")
public class Blue2LaunchCap extends Merlin3AutoMethods {


    @Override
    public void init() {
        super.init();
        super.initCamera();

        telemetry.addData("Say", "Hello Driver");    //
        telemetry.addData("Blue2", "LCapo, 15 sec");
        robot.navx_device.zeroYaw();



    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }
    private String CurrentCase = "Wait";
    private String CompletionClause = "";
    double StartTime = 0;
    @Override
    public void loop() {
        telemetry.addData("CurrentCase", CurrentCase);

        switch (CurrentCase){
            case "Wait":
                double CurrentTime = 0;
                if (StartTime == 0){
                    StartTime = System.currentTimeMillis();
                }
                else{
                    CurrentTime = (System.currentTimeMillis() - StartTime)/1000;
                }
                if(CurrentTime > 15) {
                    CurrentCase = "GoFrowardAwayFromWall";
                }
                break;
            case "GoFrowardAwayFromWall":
                CompletionClause = super.driveBasedOnEncoders(5, "Forward");
                if(CompletionClause.equals("Done")){
                    CurrentCase = "TurnToShoot";
                    CompletionClause = "NOTDONE";
                    super.resetAll();
                }
                break;
            case "TurnToShoot":
                CompletionClause = super.turnToGyroHeading(60);//The robot makes sure it is on angle for the shot.
                if(CompletionClause.equals("Done")){
                    CurrentCase = "GoForwardBeforeShoot";
                    CompletionClause = "NOTDONE";
                    super.resetAll();
                }
                break;
            case "GoForwardBeforeShoot"://First Case that drives the robot forward to a position that can launch the balls and identify the beacon
                CompletionClause = super.driveBasedOnEncoders(22, "Forward");
                if(CompletionClause.equals("Done")){
                    CurrentCase = "MakeSureItIsOnAngle";
                    CompletionClause = "NOTDONE";
                    super.resetAll();
                }
                break;
            case "MakeSureItIsOnAngle":
                CompletionClause = super.turnToGyroHeading(45);//The robot makes sure it is on angle for the shot.
                if(CompletionClause.equals("Done")){
                    CurrentCase = "WaitBeforeShoot";
                    CompletionClause = "NOTDONE";
                    super.resetAll();
                    StartTime = 0;
                }
                break;
            case "WaitBeforeShoot":
                if (StartTime == 0){
                    StartTime = System.currentTimeMillis();
                }
                else{
                    CurrentTime = (System.currentTimeMillis() - StartTime)/1000;
                    if(CurrentTime > .5) {
                        CurrentCase = "ShootFirstBall";
                        StartTime = 0;
                    }
                }

                break;
            case "ShootFirstBall"://Shoot the first ball to try to make it in the hoop
                CompletionClause = super.launchBall();
                if(CompletionClause.equals("Done")){
                    CurrentCase = "LoadSecondBall";
                    CompletionClause = "NOTDONE";
                    super.resetAll();
                }
                break;
            case "LoadSecondBall"://Load the second ball in the flipper
                CompletionClause = super.loadBall();
                if(CompletionClause.equals("Done")){
                    CurrentCase = "SecondLoadBall";
                    CompletionClause = "NOTDONE";
                    super.resetAll();
                }
                break;
            case "SecondLoadBall":
                CompletionClause = super.loadBall();
                if(CompletionClause.equals("Done")){
                    CurrentCase = "ShootSecondBall";
                    CompletionClause = "NOTDONE";
                    super.resetAll();
                }
                break;
            case "ShootSecondBall"://Shoot the second ball into the hoop
                CompletionClause = super.launchBall();
                if(CompletionClause.equals("Done")){
                    CurrentCase = "TurnToHitCapBall";
                    CompletionClause = "NOTDONE";
                    super.resetAll();
                }
                break;
            case "TurnToHitCapBall":
                CompletionClause = super.turnToGyroHeading(45);//The robot makes sure it is on angle for the shot.
                if(CompletionClause.equals("Done")){
                    CurrentCase = "HitTheCapBall";
                    CompletionClause = "NOTDONE";
                    super.resetAll();
                }
                break;

            case "HitTheCapBall":
                CompletionClause = super.driveBasedOnEncoders(38, "Forward");
                if(CompletionClause.equals("Done")){
                    CurrentCase = "Done";
                    CompletionClause = "NOTDONE";
                    super.resetAll();
                }
                break;
            case"Done":
                super.done();
                break;
            default:
                super.broken();
                break;
        }
    }

    @Override
    public void stop() {
        super.stopCamera();
    }




}