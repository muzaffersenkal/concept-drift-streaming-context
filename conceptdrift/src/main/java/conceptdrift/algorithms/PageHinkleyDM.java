package conceptdrift.algorithms;


public class PageHinkleyDM extends AbstractDetectorAlgorithm {

    private static final long serialVersionUID = -3518369648142099719L;
    public String name = "PageHinkley";
    /*
    public IntOption minNumInstancesOption = new IntOption(
            "minNumInstances",
            'n',
            "The minimum number of instances before permitting detecting change.",
            30, 0, Integer.MAX_VALUE);

    public FloatOption deltaOption = new FloatOption("delta", 'd',
            "Delta parameter of the Page Hinkley Test", 0.005, 0.0, 1.0);

    public FloatOption lambdaOption = new FloatOption("lambda", 'l',
            "Lambda parameter of the Page Hinkley Test", 50, 0.0, Float.MAX_VALUE);

    public FloatOption alphaOption = new FloatOption("alpha", 'a',
            "Alpha parameter of the Page Hinkley Test", 1 - 0.0001, 0.0, 1.0);

            e
     */

    public int minNumInstancesOption = 30;
    public double deltaOption = 0.005;
    public double lambdaOption = 50;
    public double alphaOption =  1 - 0.0001;


    private int m_n;

    private double sum;

    private double x_mean;

    private double alpha;

    private double delta;

    private double lambda;

    public PageHinkleyDM() {
        resetLearning();
    }

    public PageHinkleyDM(double lambdaOption) {
        this.lambdaOption = lambdaOption;
        resetLearning();
    }

    public PageHinkleyDM(double lambdaOption, double deltaOption) {
        this.lambdaOption = lambdaOption;
        this.deltaOption = deltaOption;
        resetLearning();
    }

    @Override
    public void resetLearning() {
        m_n = 1;
        x_mean = 0.0;
        sum = 0.0;
        delta = this.deltaOption;
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

        x_mean = x_mean + (x - x_mean) / (double) m_n;
        sum = this.alpha * sum + (x - x_mean - this.delta);

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