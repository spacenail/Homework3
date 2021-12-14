package adapter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class ChatHistory {
    private String login;
    private BufferedOutputStream bufferedOutputStream;

    ChatHistory(String login) {
        this.login = login;
        File path = new File("history_"+ login + ".txt");
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path,true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void close(){
        try {
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void record(String message){
        try {
            bufferedOutputStream.write((message+"\n").getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
