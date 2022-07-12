package conceptdrift.algorithms;



public class GeometricMovingAverageDM extends AbstractDetectorAlgorithm {

    private static final long serialVersionUID = -3518369648142099719L;
    public String name = "GeometricMovingAverage";
    /*
    public IntOption minNumInstancesOption = new IntOption(
            "minNumInstances",
            'n',
            "The minimum number of instances before permitting detecting change.",
            30, 0, Integer.MAX_VALUE);

    public FloatOption lambdaOption = new FloatOption("lambda", 'l',
            "Threshold parameter of the Geometric Moving Average Test", 1, 0.0, Float.MAX_VALUE);

    public FloatOption alphaOption = new FloatOption("alpha", 'a',
            "Alpha parameter of the Geometric Moving Average Test", .99, 0.0, 1.0);


     */
    public int minNumInstancesOption = 30;
    public double lambdaOption =1;
    public double alphaOption = .99;
    private double m_n;

    private double sum;

    private double x_mean;

    private double alpha;

    private double delta;

    private double lambda;

    public GeometricMovingAverageDM() {
        resetLearning();
    }

    @Override
    public void resetLearning() {
        m_n = 1.0;
        x_mean = 0.0;
        sum = 0.0;
        alpha = this.alphaOption;
        lambda = this.lambdaOption;
    }

    @Override
    public void input(double x) {
        // It monitors the error rate
        if (this.isChangeDetected == true || this.isInitialized == false) {
            resetLearning();
            this.isInitialized = true;
        }

        x_mean = x_mean + (x - x_mean) / m_n;
        sum = alpha * sum + ( 1.0- alpha) * (x - x_mean);


        m_n++;

        // System.out.print(prediction + " " + m_n + " " + (m_p+m_s) + " ");
        this.estimation = x_mean;
        this.isChangeDetected = false;
        this.isWarningZone = false;
        this.delay = 0;

        if (m_n < this.minNumInstancesOption) {
            return;
        }

        if (sum > this.lambda) {
            this.isChangeDetected = true;
        }
    }


}