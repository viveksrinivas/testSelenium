package ReportUtility;

import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
    static ExtentReports extent;
    final static String filePath = System.getProperty("user.dir")+"\\NewReport\\Extent1.html";
    
    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            extent = new ExtentReports(filePath, true);
        }
        
        return extent;
    }
}