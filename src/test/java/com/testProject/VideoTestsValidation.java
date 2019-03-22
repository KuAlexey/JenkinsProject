package com.testProject;

import com.testProject.base.BaseTest;
import com.testProject.enums.VastRequestParameter;
import com.testProject.enums.VpaidMode;
import com.testProject.sniffing.BrowserProxySniffing;
import com.testProject.validation.SiteForValidation;
import com.testProject.validation.VastEntity;
import com.utils.DriverHelper;
import com.utils.Utils;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.proxy.CaptureType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.testProject.validation.VastContainer;


import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VideoTestsValidation extends BaseTest {


    private BrowserMobProxy browserMobProxy = BrowserProxySniffing.getBrowserMobProxy();
    BrowserProxySniffing sniffing = new BrowserProxySniffing();
    private VastContainer vastContainer = new VastContainer();

    @DataProvider
    private Iterator<Object[]> generateVpaidMode() {
        List<Object[]> outputList = new ArrayList<>();
        List<String> listOfVasts = Arrays.asList("https://serve2.cheqzone.com/show_vast?id=621"/*,
                                             "https://serve2.cheqzone.com/show_vast?id=622",
                                             "https://serve2.cheqzone.com/show_vast?id=623"*/);
        List<VpaidMode> listOfVpaidModes = Arrays.asList(/*VpaidMode.DISABLED, VpaidMode.ENABLED,*/ VpaidMode.INSECURE);
        List<List<Boolean>> listOfTheView_HearLogic = Arrays.asList(Arrays.asList(true, true)/*,
                                                                Arrays.asList(false, true),
                                                                Arrays.asList(true, false),
                                                                Arrays.asList(false, false)*/);
        for (String vast : listOfVasts) {
            for (VpaidMode vpaidMode : listOfVpaidModes) {
                for (List<Boolean> view_hearLogic : listOfTheView_HearLogic) {
                    outputList.add(new Object[]{vast, vpaidMode, view_hearLogic.get(0), view_hearLogic.get(1)});
                }
            }
        }
        return outputList.iterator();
    }

  /*  @BeforeSuite(alwaysRun = true)
    public void initCapabilities() {

        sniffing.startProxyServerInLocal();
        HashMap<String, Object> capabilities = sniffing.getProxyCapabilities();
        setCapabilities(capabilities);
        browserMobProxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT);
        browserMobProxy.newHar("video.validation");
        //   runBrowserProxy();
    }*/

    private void stopBrowsermobProxyProcess() {
        Utils.delay(5, 10);
        BrowserProxySniffing.getBrowsermobProxyProcess().destroy();
    }

    private void runBrowserProxy() {
        Runnable runBrowserProxyInBackGround = BrowserProxySniffing::runBrowsermob;
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(runBrowserProxyInBackGround);
        Utils.delay(0, 15);
        BrowserProxySniffing.initializeProxy();
    }

    @Test(testName = "C619055", description = "Video. VAST validation test",
            dataProvider = "generateVpaidMode", priority = 1)
    public void vastValidationTest(String sample, VpaidMode vpaidMode, boolean isVolumeEnabled,
                                   boolean isVideoInTheView) {
        VastEntity vastEntity = new VastEntity(sample, vpaidMode, isVolumeEnabled, isVideoInTheView);
        int mutedVideoInc = isVolumeEnabled ? 1 : 0;
        int outOfViewVideoInc = isVideoInTheView ? 1 : 0;
        DriverHelper.openUrl("https://developer.jwplayer.com/tools/ad-tester/");
        SiteForValidation siteForValidation = new SiteForValidation();
        siteForValidation.clickGoogleIma()
                .clickVpaidMode(vpaidMode)
                .inputTextArea(sample)
                .clickToPlayVAST()
                .enableVolumeInVast(isVolumeEnabled)
                .stayInTheView(isVideoInTheView);
        String uuid = Utils.getTimeStampNoMillis();
        String fileName = String.format(vastEntity.getIdOfVast(sample) + vpaidMode.toString().substring(0, 3)
                        + "_v%d_a%d_(%s).har", outOfViewVideoInc, mutedVideoInc,
                uuid.substring(uuid.length() - 3));
        Utils.delay(1, 0);
        BrowserProxySniffing.writeHar(browserMobProxy, fileName);
        vastEntity.setListOfRequests(BrowserProxySniffing.readHar(fileName));
        vastContainer.containVastEntity(vastEntity);
        System.out.println(vastEntity.getSampleVast() + "___" + vastEntity.getVpaidMode() + "___" +
                "VOLUME--" + vastEntity.isVolumeEnabled() + " VIEWED--" + vastEntity.isVideoInTheView());
        for (int i = 1; i <= vastEntity.getAmountOfDuration(); i++) {
            System.out.println("PER(" + i + ")--VIDEO--" + vastEntity.getParameterForQuartile(String.valueOf(i), VastRequestParameter.VIEW) +
                    "\t" + "AUDIO--" +
                    vastEntity.getParameterForQuartile(String.valueOf(i), VastRequestParameter.AUDIBLE) + "\t");
        }
    }

    @Test(priority = 1, enabled = false)
    public void test() {
        List<VastEntity> vastEntityList =
                vastContainer.getVastEntityList();
        for (VastEntity vastEntity : vastEntityList) {
            System.out.println(vastEntity.getSampleVast() + "___" + vastEntity.getVpaidMode() + "___" +
                    "MUTED--" + vastEntity.isVolumeEnabled() + "VIEWED--" + vastEntity.isVideoInTheView());
            for (int i = 1; i <= vastEntity.getAmountOfDuration(); i++) {
                System.out.println("PER(" + i + ")--VIDEO--" + vastEntity.getParameterForQuartile(String.valueOf(i), VastRequestParameter.VIEW) +
                        "\t" + "AUDIO--" +
                        vastEntity.getParameterForQuartile(String.valueOf(i), VastRequestParameter.AUDIBLE) + "\t");
            }
        }
    }

    @AfterClass(alwaysRun = true)
    public void clean() {
        clearCapabilities();
        //stopBrowsermobProxyProcess();
        if (browserMobProxy.isStarted()) {
            browserMobProxy.stop();
        }
    }
}
