package com.ULock.App_Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazer on 7/21/2016.
 */
public class App_Model implements Serializable {

    //===========================start date
    List<String> start_date=new ArrayList();

    public void set_start_date(List date)
    {
        this.start_date=date;
    }
    public List get_start_date()
    {
        return start_date;
    }
//===========================end date
    List<String> end_date=new ArrayList();

    public void set_end_date(List date)
    {
        this.end_date=date;
    }
    public List get_end_date()
    {
        return end_date;
    }

    //===========================start time

    List<String> start_time=new ArrayList();

    public void set_start_time(List date)
    {
        this.start_time=date;
    }
    public List get_start_time()
    {
        return start_time;
    }
    //===========================end time

    List<String> end_time=new ArrayList();

    public void set_end_time(List date)
    {
        this.end_time=date;
    }
    public List get_end_time()
    {
        return end_time;
    }

    //===========================days

    List<String> days=new ArrayList();

    public void set_days(List date)
    {
        this.days=date;
    }
    public List get_days()
    {
        return days;
    }

    //===========================app_Name

    List<String> app_name=new ArrayList();

    public void set_app_name(List date)
    {
        this.app_name=date;
    }
    public List get_app_name()
    {
        return app_name;
    }


}
