package org.firstinspires.ftc.teamcode.team.Merlin1718.WestCoast;





import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Disabled
@Autonomous(name = "Case Test", group = "Cardinal")
//@Disabled //Uncomment this if it is not wanted on the phone
public class IndivigualCaseTest extends OpMode {


    public WestCoastHardware robot = new WestCoastHardware();//The hardware map needs to be the hardware map of the robot we are using

    public void init(){//This only runs once
        robot.init(hardwareMap);
        robot.motoG.initCamera();
        robot.glyphCollector.topGrasper.close();
        robot.glyphCollector.bottomGrasper.open();
        ;//Initializing everything needed
    }
    @Override
    public void init_loop(){//Tis runs many time during the init phase
    }
    @Override
    public void start(){}//This runs when the start button is pressed
    // distance of variables


    double versionNumber = 104;


    private String color = "blue";
    private double spinRightToKnockOffRightJewel = 7;
    private double spinLeftToKnockOffLeftJewel  = -7;
    private double driveOffBalancePad = 21;
    private double driveDistanceToRightColumn = 15;//14
    private double driveDistanceToCenterColumn = 7;// 10
    private double driveDistanceToLeftColumn = 0;//4
    private double driveForwardToCryptobox = 8;
    private double driveAwayFromCryptobox = 5;//7



    private String currentCase = "DropSorter";
    private String redJewel;
    private String column = "null";
    @Override
    public void loop(){//This runs while opmode is active
        telemetry.addData("Version Number: ", versionNumber);
        telemetry.addData("CurrentCase", currentCase);
        telemetry.addData("jewel", redJewel);
        telemetry.addData("colum", column);
        telemetry.addData("power", robot.westCoast.leftMotor.getPower());
        telemetry.addData("startTime", startTime);
        telemetry.addData("duration", (System.currentTimeMillis()-startTime)/1000);
        switch (currentCase){
            case "DropSorter": //startLowering sorter to sort jewel
                boolean doneYet;
                doneYet = robot.jewelSorter.lower();
                if(doneYet){
                    currentCase = "IDCryptographPicAndJewelColor";
                }
                break;
            case "IDCryptographPicAndJewelColor": //determine cryptograph and what color the jewl closest to the crytobo is and spin left or right based off color
                redJewel = robot.jewelSorter.auto();
                column = robot.motoG.key();
                if(!column.equals("null") && !redJewel.equals("null")){
                    if(redJewel.equals(color)){
                        currentCase = "SpinRightToKnockOffRightJewel";
                    }
                    else {
                        currentCase = "SpinLeftToKnockOffLeftJewel";
                    }
                }
                break;
            case "SpinRightToKnockOffRightJewel": //spin right to sort jewl, then raise sorter
                doneYet = robot.westCoast.turnToGyroHeading(true, spinRightToKnockOffRightJewel, robot.navx.getCurrentOrientation());
                if(doneYet){
                    currentCase = "RaiseSorter";
                }
                break;
            case "SpinLeftToKnockOffLeftJewel": //spin left to sort jewl, then raise sorter
                doneYet = robot.westCoast.turnToGyroHeading(true, spinLeftToKnockOffLeftJewel, robot.navx.getCurrentOrientation());
                if(doneYet){
                    currentCase = "RaiseSorter";
                }
                break;
            //raise sorter
            case "RaiseSorter":
                doneYet = robot.jewelSorter.raise();
                if(doneYet){
                    currentCase = "SpinBackToStartingPosition";
                }
                break;
            case "SpinBackToStartingPosition": //resenter robot
                doneYet = robot.westCoast.turnToGyroHeading(true, 0, robot.navx.getCurrentOrientation());
                if(doneYet){
                    currentCase = "DriveOffBalancePad";
                }
                break;

            case "DriveOffBalancePad": //move to right collumn
                doneYet = robot.westCoast.driveBasedOnEncodersAndGyro(driveOffBalancePad, -1, true, 0, robot.navx.getCurrentOrientation());//distance, direction, targetHeading, currentHeading
                telemetry.addData("start: ", robot.westCoast.startEncoder);
                telemetry.addData("current: ", robot.westCoast.getLeftCurrentMotorPosition());
                if(doneYet){
                    currentCase = "SpinTo-90";

                }
                break;

            case "SpinTo-90": //turn -90% to face cryptobox
                doneYet = robot.westCoast.turnToGyroHeading(true, -90, robot.navx.getCurrentOrientation());
                telemetry.addData("Current Orientation", robot.navx.getCurrentOrientation());
                if(doneYet){
                    currentCase = choseColumnCase(column);
                }
                break;

            case "ToRightColumn": //move to right collumn
                doneYet = robot.westCoast.driveBasedOnEncodersAndGyro(driveDistanceToRightColumn, 1, true, -90, robot.navx.getCurrentOrientation());//distance, direction, targetHeading, currentHeading
                //telemetry.addData("start: ", robot.westCoast.startEncoder);
                //telemetry.addData("current: ", robot.westCoast.getLeftCurrentMotorPosition());
                if(doneYet){
                    currentCase = "SpinTo-170";

                }
                break;

            case "ToCenterColumn": //move to center column
                doneYet = robot.westCoast.driveBasedOnEncodersAndGyro(driveDistanceToCenterColumn, 1, true, -90, robot.navx.getCurrentOrientation());
                if(doneYet){
                    currentCase = "SpinTo-170";
                }
                break;

            case "ToLeftColumn": //move to left column
                doneYet = robot.westCoast.driveBasedOnEncodersAndGyro(driveDistanceToLeftColumn, 1, true, -90, robot.navx.getCurrentOrientation());
                if(doneYet){
                    currentCase = "SpinTo-170";
                }
                break;

            case "SpinTo-170": //turn -179% to face cryptobox
                doneYet = robot.westCoast.turnToGyroHeading(false, 0, robot.revIMU.getCurrentOrientation());
                if(doneYet){
                    currentCase = "ReleaseGripper";
                }
                break;
            case "ReleaseGripper": //release glyph
                robot.glyphCollector.topGrasper.open();
                doneYet = true;
                if(doneYet){
                    currentCase = "DriveForward";
                }
                break;
            case "DriveForward": //dive forward twards cryptobox
                doneYet = robot.westCoast.driveBasedOnEncodersAndGyro(driveForwardToCryptobox, 1, false, 0, robot.revIMU.getCurrentOrientation()) || time();
                //doneYet = robot.westCoast.driveBasedOnEncoders(driveForwardToCryptobox, 1) || time();
                if(doneYet){
                    currentCase = "DriveBack";
                }
                break;
            //back up
            case "DriveBack":
                doneYet = robot.westCoast.driveBasedOnEncoders(driveAwayFromCryptobox, -1, true);
                if(doneYet){
                    currentCase = "DriveForward2";
                }
                break;
            case "DriveForward2":
                //doneYet = robot.westCoast.driveBasedOnEncodersAndGyro(driveAwayFromCryptobox+2, 1, -175, robot.navx.getCurrentOrientation()) || time();
                doneYet = robot.westCoast.driveBasedOnEncoders(driveForwardToCryptobox+2, 1, true) || time();
                if(doneYet){
                    currentCase = "DriveBack2";
                }
                break;
            case "DriveBack2":
                doneYet = robot.westCoast.driveBasedOnEncoders(driveAwayFromCryptobox, -1, true);
                if(doneYet){
                    currentCase = "End";
                }
                break;
            case "End":
                robot.westCoast.drive(0,0, true);
                break;
            default:
                robot.westCoast.drive(0,0, true);
                telemetry.addData("What Are You", " Doing");
                telemetry.update();
        }
    }
    @Override
    public void stop(){}








    private String choseColumnCase(String column){
        switch (column){
            case "RIGHT":
                return "ToRightColumn";
            case "CENTER":
                return "ToCenterColumn";
            case "LEFT":
                return "ToLeftColumn";
            default:
                telemetry.addData("Not Left Right Or", " Center");
                return "";
        }
    }
    long startTime = 0;
    boolean firstTime = true;
    private boolean time (){
        if(firstTime){
            firstTime = false;
            startTime = System.currentTimeMillis();
        }
        telemetry.addData("startTime", startTime);
        long duration = (System.currentTimeMillis()-startTime)/1000;
        if(duration > 2) {
            firstTime = true;
            telemetry.addData("startTime in duration", startTime);
            return true;
        }
        else return false;

    }

}
