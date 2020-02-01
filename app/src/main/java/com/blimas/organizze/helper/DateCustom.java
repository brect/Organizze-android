package com.blimas.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {


    public static String dataAtual(){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;
    }

    public static String dataMes(String data){

        String dateArray[] = data.split("/");
        String dia = dateArray[0];
        String mes = dateArray[1];
        String ano = dateArray[2];

        String mesAno = mes + ano;
        return mesAno;
    }
}
