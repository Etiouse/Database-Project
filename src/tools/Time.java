package tools;

import java.util.Calendar;

public class Time {

    private int seconds;
    private int minutes;
    private int timeAtStart;
    private String minutesString;
    private String secondsString;
    private int[] secPassed;
    private int[] lastSend;
    private int fps;
    private int lastFps;

    public Time(){
        seconds = 0;
        minutes = 0;
        timeAtStart = (int) Calendar.getInstance().getTime().getTime();
        minutesString = "";
        secondsString = "";
        secPassed = new int[10];
        lastSend = new int[10];
        for (int i=0; i<secPassed.length; i++){
            secPassed[i] = 0;
            lastSend[i] = timeAtStart;
        }
        fps = 0;
        lastFps = timeAtStart;
    }

    public String getTime(){
        int currentTime = (int) Calendar.getInstance().getTime().getTime();
        minutes = (int) Math.floor((currentTime - timeAtStart)/60000);
        seconds = (int) (Math.floor((currentTime - timeAtStart)/1000) % 60);

        if (seconds > 59){
            seconds = 0;
        }
        if (seconds < 10){
            secondsString = "0" + seconds;
        } else {
            secondsString = seconds + "";
        }
        if (minutes < 10){
            minutesString = "0" + minutes;
        } else {
            minutesString = minutes + "";
        }
        return minutesString + ":" + secondsString;
    }
    
    public int getChrono(){
        int currentTime = (int) Calendar.getInstance().getTime().getTime();
        int chrono = currentTime - timeAtStart;
        return chrono/100;
    }

    public String getHorloge(){
        @SuppressWarnings("deprecation")
		int hours = Calendar.getInstance().getTime().getHours();
        @SuppressWarnings("deprecation")
		int minutes = Calendar.getInstance().getTime().getMinutes();
        if (hours < 10 && minutes < 10){
            return "0" + hours + ":0" + minutes;
        } else if (hours < 10){
            return "0" + hours + ":" + minutes;
        } else if (minutes < 10){
            return hours + ":0" + minutes;
        } else {
            return hours + ":" + minutes;
        }
    }

    public void restartClock(){
        timeAtStart = (int) Calendar.getInstance().getTime().getTime();
    }

    public boolean clock(){
        int currentTime = (int) Calendar.getInstance().getTime().getTime();
        if ((currentTime - timeAtStart)%1000 > 500){
            return false;
        } else {
            return true;
        }
    }

    public boolean timer(double delay, int n){
        int time = (int) Calendar.getInstance().getTime().getTime();
        secPassed[n] = time - lastSend[n];
        if (secPassed[n] > delay){
            lastSend[n] = time;
            return true;
        } else {
            return false;
        }
    }
    
    public void resetTimer(int n){
        int time = (int) Calendar.getInstance().getTime().getTime();
        lastSend[n] = time;
    }

    public int getFps(){
        fps++;
        int sendFps = -1;
        int time = (int) Calendar.getInstance().getTime().getTime();
        int timePassed = time - lastFps;
        if (timePassed > 1000){
            sendFps = fps;
            fps = 0;
            lastFps = time;
        }
        return sendFps;
    }

}
