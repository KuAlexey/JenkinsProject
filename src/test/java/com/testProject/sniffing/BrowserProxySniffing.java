package com.testProject.sniffing;

import com.utils.Utils;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.apache.commons.io.FileUtils;
import org.apache.tika.io.IOUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.net.NetworkUtils;
import org.openqa.selenium.remote.CapabilityType;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BrowserProxySniffing {

    private static Process browsermobProxyProcess;
    private static BrowserMobProxy browserMobProxy = new BrowserMobProxyServer();

    public static BrowserMobProxy getBrowserMobProxy() {
        return browserMobProxy;
    }

    public static Process getBrowsermobProxyProcess() {
        return browsermobProxyProcess;
    }


    public HashMap<String, Object> getProxyCapabilities() {
        // Proxy seleniumProxy = ClientUtil.createSeleniumProxy(browserMobProxy);
        Proxy seleniumProxy = new Proxy();
        String hostIp;
        int port;
        // try {

        port = 9090;
        hostIp = "172.19.0.0";
        seleniumProxy.setHttpProxy(hostIp + ":" + port)
                .setSslProxy(hostIp + ":" + port);
        System.out.println(seleniumProxy.getHttpProxy());
    /*} catch (UnknownHostException e) {
      e.printStackTrace();
    }*/

        HashMap<String, Object> capabilities = new HashMap<>();
        capabilities.put(CapabilityType.PROXY, seleniumProxy);
        capabilities.put(CapabilityType.ACCEPT_SSL_CERTS, true);
        return capabilities;
    }

    public void startProxyServer(String address) {
        if (browserMobProxy.isStarted()) {
            browserMobProxy.stop();
        }
        try {
            browserMobProxy.start(9090, Inet4Address.getByName(address));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void startProxyServerInLocal() {
        startProxyServer(null);
    }

    public static void runBrowsermob() {
        browsermobProxyProcess =
                Utils.getProcess("/bin/sh", "-c", System.getProperty("user.home") +
                        "/browsermob-proxy-2.1.4/bin/browsermob-proxy -port 9090 -ttl 300");
    }

    public static void initializeProxy() {
        Utils.executeCommand("POST request ", "curl", "-X", "POST", "-d 'port=8082'",
                "http://localhost:9090/proxy");
        Utils.executeCommand("POST CHECK request ", "curl", "-X", "GET", "http://localhost:9090/proxy");
        Utils.executeCommand("PUT request ", "curl", "-X", "PUT", "http://localhost:9090/proxy/8082/har");
        Utils.executeCommand("GET CHECK request ", "curl", "-X", "GET", "http://localhost:9090/proxy/8082/har");
    }

    public static void writeHar(BrowserMobProxy browserMobProxy, String harFileName) {
        Har har = browserMobProxy.getHar();
        Path harFilePath = Paths.get("src", "test", "resources", "har", harFileName).toAbsolutePath();
        try {
            har.writeTo(harFilePath.toFile());
        } catch (IOException ex) {
            System.out.println(ex.toString());
            System.out.println("Could not find file " + harFileName);
        }
    }

    public static List<String> readHar(String harFileName) {
        List<String> requestsList = new LinkedList<>();
        List<String> subList;
        Path harFilePath = Paths.get("src", "test", "resources", "har", harFileName).toAbsolutePath();
        String result = "";
        try {
            result = FileUtils.readFileToString(harFilePath.toFile(), "UTF-8");
            result = IOUtils.toString(new URL("http://localhost:9090/proxy/8082/har").openStream(),
                    "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pattern pattern = Pattern.compile("(https://pixel2\\.cheqzone\\.com/tracker/vid_\\S+)");
        Matcher matcher = pattern.matcher(result.replaceAll(",", " "));
        while (matcher.find()) {
            requestsList.add(matcher.group(1));
        }
        if (requestsList.size() > 6) {
            subList = requestsList.subList(requestsList.size() - 6, requestsList.size());
        } else {
            subList = requestsList;
        }
        return subList;
    }
}
