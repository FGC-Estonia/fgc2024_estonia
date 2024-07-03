import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;

@TeleOp(name = "Main")
public class Main extends LinearOpMode {
    private BNO055IMU imu;

    private final double accelerationLimit=200; //RPSS revolutions per secon  second 

    private DcMotor leftFrontDrive   = null;  //  Used to control the left front drive wheel
    private DcMotor rightFrontDrive  = null;  //  Used to control the right front drive wheel
    private DcMotor leftBackDrive    = null;  //  Used to control the left back drive wheel
    private DcMotor rightBackDrive   = null;  //  Used to control the right back drive wheel

    @Override
    public void runOpMode() {
        
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "Motor_Port_0_CH");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "Motor_Port_2_CH");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "Motor_Port_1_CH");
        rightBackDrive = hardwareMap.get(DcMotor.class, "Motor_Port_3_CH");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Initialize the hardware variables
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        // Set up the parameters for the IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        // Initialize the IMU
        imu.initialize(parameters);

        // Wait for the start button to be pressed
        waitForStart();

        while (opModeIsActive()) {
            // Get the acceleration data from the IMU
            Acceleration acceleration = imu.getAcceleration();
            
            // Display acceleration data on telemetry
            telemetry.addData("X", acceleration.xAccel);
            telemetry.addData("Y", acceleration.yAccel);
            telemetry.addData("Z", acceleration.zAccel);
            telemetry.update();

            drive = -gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;
            turn = gamepad1.right_stick_x;

            moveRobot(drive, strafe, turn);
        }
    }
    public void moveRobot(double drive, double strafe, double turn) {
        // Calculates raw power to motors
        double leftFrontPowerRaw = -drive + strafe - turn;
        double leftBackPowerRaw = drive + strafe - turn;
        double rightFrontPowerRaw = drive + strafe + turn;
        double rightBackPowerRaw = -drive + strafe + turn;

        // Calculate the maximum absolute power value for normalization
        double maxRawPower = Math.max(Math.max(Math.abs(leftFrontPowerRaw), Math.abs(leftBackPowerRaw)),
        Math.max(Math.abs(rightFrontPowerRaw), Math.abs(rightBackPowerRaw)));

        double max = Math.max(maxRawPower, 1.0);

        // Calculate wheel powers.
        double leftFrontPower    =  (-drive + strafe -turn)/max/1.2039;
        double leftBackPower     =  (drive + strafe -turn)/max/1.2039;
        double rightFrontPower   =  (drive + strafe +turn)/max;
        double rightBackPower    =  (-drive + strafe +turn)/max;

        
        // Send powers to the wheels.
        leftFrontDrive.setPower(leftFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightFrontDrive.setPower(rightFrontPower);
        rightBackDrive.setPower(rightBackPower);
    }
    private void tractionControl(double pow1, double pow2, double pow3, double pow4){
        double a=accelerationLimit+1;
    }
}