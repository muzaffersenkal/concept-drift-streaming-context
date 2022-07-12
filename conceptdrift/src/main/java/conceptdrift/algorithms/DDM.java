
package conceptdrift.algorithms;


public class DDM extends AbstractDetectorAlgorithm {


    private static final long serialVersionUID = -3518369648142099719L;
    public String name = "DDM";

    //private static final int DDM_MINNUMINST = 30;
    /*
    public IntOption minNumInstancesOption = new IntOption(
            "minNumInstances",
            'n',
            "The minimum number of instances before permitting detecting change.",
            30, 0, Integer.MAX_VALUE);

    public FloatOption warningLevelOption = new FloatOption(
            "warningLevel", 'w', "Warning Level.",
            2.0, 1.0, 4.0);

    public FloatOption outcontrolLevelOption = new FloatOption(
            "outcontrolLevel", 'o', "Outcontrol Level.",
            3.0, 1.0, 5.0);


     */

    private  int  minNumInstancesOption = 30;
    private  double  warningLevelOption = 2.0;
    private  double  outcontrolLevelOption = 3.0;

    
    private int m_n;

    private int minNumInstances;

    private double m_p;

    private double m_s;

    private double m_psmin;

    private double m_pmin;

    private double m_smin;

    private double warningLevel;

    private double outcontrolLevel;

    public DDM() {
        resetLearning();
    }

    @Override
    public void resetLearning() {
        m_n = 1;
        m_p = 1;
        m_s = 0;
        m_psmin = Double.MAX_VALUE;
        m_pmin = Double.MAX_VALUE;
        m_smin = Double.MAX_VALUE;
        minNumInstances = this.minNumInstancesOption;
        warningLevel = this.warningLevelOption;
        outcontrolLevel = this.outcontrolLevelOption;
    }

    @Override
    public void input(double prediction) {
        // prediction must be 1 or 0
        // It monitors the error rate
        if (this.isChangeDetected == true || this.isInitialized == false) {
            resetLearning();
            this.isInitialized = true;
        }
        m_p = m_p + (prediction - m_p) / (double) m_n;
        m_s = Math.sqrt(m_p * (1 - m_p) / (double) m_n);

        m_n++;

        // System.out.println(prediction + " " + m_n + " " + (m_p+m_s) + " ");
        this.estimation = m_p;
        this.isChangeDetected = false;
        this.isWarningZone = false;
        this.delay = 0;

        if (m_n < minNumInstances) {
            return;
        }
        // System.out.println(prediction + " " + m_n + " " + (m_p+m_s) + " ");
        if (m_p + m_s <= m_psmin) {
            m_pmin = m_p;
            m_smin = m_s;
            m_psmin = m_p + m_s;
        }

        if (m_n > minNumInstances && m_p + m_s > m_pmin + outcontrolLevel * m_smin) {
            // System.out.println(m_p + ",D");
            this.isChangeDetected = true;
            //resetLearning();
        } else if (m_p + m_s > m_pmin + warningLevel * m_smin) {
            //System.out.println(m_p + ",W");
            this.isWarningZone = true;
        } else {
            this.isWarningZone = false;
            //System.out.println(m_p + ",N");
        }
    }


}