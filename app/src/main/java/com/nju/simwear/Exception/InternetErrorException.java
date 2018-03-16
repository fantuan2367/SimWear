package com.nju.simwear.Exception;

/**
 * Created by Onigiri on 2018/3/15.
 */

public class InternetErrorException extends Exception {
    public InternetErrorException(){
        super();
    }

    public InternetErrorException(String message){
        super(message);
    }
}
