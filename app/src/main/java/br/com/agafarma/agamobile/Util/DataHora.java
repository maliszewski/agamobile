package br.com.agafarma.agamobile.Util;

import java.util.Calendar;
import java.util.Date;

public class DataHora {

    private Date data = new Date();

    public DataHora(Date data){
        this.data = data;
    }

    public Date addDay(int qt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DAY_OF_MONTH, qt);
        data.setTime(calendar.getTimeInMillis());
        return data;
    }

    public Date addMonth(int qt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.MONTH, qt);
        data.setTime(calendar.getTimeInMillis());
        return data;
    }

    public Date addYear(int qt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.YEAR, qt);
        data.setTime(calendar.getTimeInMillis());
        return data;
    }

    public Date addHour(int qt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.HOUR_OF_DAY, qt);
        data.setTime(calendar.getTimeInMillis());
        return data;
    }

    public Date addMinute(int qt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.MINUTE, qt);
        data.setTime(calendar.getTimeInMillis());
        return data;
    }

    public Date addSecond(int qt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.SECOND, qt);
        data.setTime(calendar.getTimeInMillis());
        return data;
    }

}
