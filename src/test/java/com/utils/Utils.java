package com.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service class, wraps utility methods.
 */
public final class Utils {
  
  private Utils() {
  
  }
  
  public static synchronized String getTimeStampNoMillis() {
    String currentTime = String.valueOf(Instant.now().toEpochMilli());
    return currentTime.substring(0, currentTime.length() - 3); //without millis to shorten the string
  }
  
  public static String getCurrentDateAndTime() {
    ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.systemDefault());
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String dateAndTime = dtf.format(currentDate);
    return dateAndTime;
  }

  
  public static void delay(int minutes, int seconds) throws IllegalArgumentException {
    if (minutes < 0 || seconds < 0) {
      throw new IllegalArgumentException("Arguments cannot be negative!");
    }
    LocalDateTime timeLimit = LocalDateTime.now().plusMinutes(minutes);
    timeLimit = timeLimit.plusSeconds(seconds);
    while (LocalDateTime.now().isBefore(timeLimit)) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ignored) {
      }
    }
  }
  
  public static Process getProcess(String... command) {
    Process process = null;
    try {
      process = new ProcessBuilder(command)
              .start();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return process;
  }
  
  public static void executeCommand(String commandName, String... command) {
    Process process;
    int exitValue;
    try {
      process = getProcess(command);
      exitValue = process.waitFor();
      System.out.println(commandName + "\n=====\n" + getProcessOutput(process) + "=====\n");
    } catch (InterruptedException e) {
      throw new RuntimeException(commandName, e);
    }
    if (exitValue != 0) {
      System.out.println(getProcessOutput(process));
      throw new RuntimeException(commandName);
    }
  }
  
  public static void copyFileToServer(String fileName) throws RuntimeException {
    Path filePath = Paths.get("src", "test", "resources", "pages", fileName).toAbsolutePath();
    String command;
    ProcessBuilder processBuilder;
    Process process;
    if (isWindows()) {
      command = String.format("scp -i combo.key %s ec2-user@18.212.88.58:/var/www/html",
                              filePath.toString().replaceAll("\\\\", "/"));
      processBuilder = new ProcessBuilder("cmd", "/c", command);
    } else {
      command = String
              .format("scp -v -o StrictHostKeyChecking=no -i %s %s ec2-user@172.31.27.116:/var/www/html",
                      System.getProperty("user.home") + "/combo.key",
                      filePath.toString());
      processBuilder = new ProcessBuilder("/bin/sh", "-c", command);
    }
    int exitValue;
    try {
      process = processBuilder.start();
      exitValue = process.waitFor();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("File copy process has failed!", e);
    }
    if (exitValue != 0) {
      System.out.println(getProcessOutput(process));
      throw new RuntimeException("File copy process has failed!");
    }
  }
  
  private static String getProcessOutput(Process process) {
    int exitValue = process.exitValue();
    StringBuilder sb = new StringBuilder();
    sb.append("File copy process output:").append(System.lineSeparator());
    String line;
    if (exitValue != 0) {
      sb.append("Error message:").append(System.lineSeparator());
      try (BufferedReader stdError
                   = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
        while ((line = stdError.readLine()) != null) {
          sb.append(line).append(System.lineSeparator());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      try (BufferedReader stdOut
                   = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        while ((line = stdOut.readLine()) != null) {
          sb.append(line).append(System.lineSeparator());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    sb.append("File copy process exit value: ").append(exitValue).append(System.lineSeparator());
    return sb.toString();
  }
  
  public static boolean isLinux() {
    String os = System.getProperty("os.name");
    return os.toLowerCase(Locale.ROOT).contains("linux");
  }
  
  public static boolean isWindows() {
    String os = System.getProperty("os.name");
    return os.toLowerCase(Locale.ROOT).contains("windows");
  }
  
  public static String getEntityIdFromUrl() {
    String siteUrl = DriverHelper.getDriver().getCurrentUrl();
    String[] splitUrl = siteUrl.split("/");
    return splitUrl[splitUrl.length - 1];
  }
  
  public static void serialize(Serializable object, File target) throws RuntimeException {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(target))) {
      out.writeObject(object);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Serialization has failed!", e);
    }
  }
  
  public static Object deserialize(File target) throws RuntimeException {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(target))) {
      return in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException("Deserialization has failed!", e);
    }
  }
}
