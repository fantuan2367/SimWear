package com.nju.simwear.Utils;

import android.os.Bundle;

import com.nju.simwear.Exception.InternetErrorException;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InternetUtils {
    private static int i = 0;
    private static final String SERVER_URL = "192.168.43.58";
    private static final int HTTP_URL_CONNECTION_TIMEOUT = 5000;

    private static JSONObject doget(){
        try{
            URL httpUrl = new URL(SERVER_URL);

            HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
            httpURLConnection.setReadTimeout(HTTP_URL_CONNECTION_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");

            StringBuffer stringBuffer = new StringBuffer();
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            System.out.println(line);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkUsernameAndPassword(String username, String password){
        if(username.equals("a") && password.equals("b")){
            return true;
        }
        else {
            return false;
        }
    }

    public static Bundle getInfo() throws InternetErrorException {
        Bundle bundle = new Bundle();

        bundle.putString("temperature", String.valueOf(i));
        bundle.putString("heartRate", String.valueOf(i+1));
        ++i;

        doget();

        if(bundle.size() == 0){
            throw new InternetErrorException();
        }
        else {
            return bundle;
        }
    }
}
