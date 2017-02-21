package ReportUtility;

import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

public class ParallelClass2 extends BaseClass {
    @Test
    public void parallelClass2TestResultMustEqualFail() throws Exception 
    {
        ExtentTestManager.getTest().log(LogStatus.PASS, "Log from threadId: " + Thread.currentThread().getId());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Log from threadId: " + Thread.currentThread().getId());
        
 /*       throw new Exception("intentional failure");*/
    }
}
