package com.ULock.App_Database;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ULock.MainActivity;
import com.ULock.Prefhelper.Prefshelper;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class LockService extends Service {

    Prefshelper prefshelper;
    public static SharedPreferences preferences;
    ArrayList<String> delete_app;
    ArrayList<App_Model> app_data;
    ArrayHelper arrayHelper;
    Gson gson;
    String json;
    App_Model app_model;
    ArrayList<String> idea;
    App_Model modelObject1;

    private HandlerThread thread1;
    String p_name,pkname;
    List<String> pkg_name,days,start_time,end_time,start_date,end_date;
    List<String> pkg_name1,days1,start_time1,end_time1,start_date1,end_date1;
    Thread thread;
    ArrayList<String> app_lock;
    ArrayList<App_Model> new_idea;
    BroadcastReceiver mReceiver;
    String screenOn="on";
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new Screen_Receiver();
        registerReceiver(mReceiver, filter);
    }
    //	@Override
//	public void onCreate() {
//		thread = new HandlerThread("handler", android.os.Process.THREAD_PRIORITY_BACKGROUND);
//		thread.start();
//	}
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        screenOn = intent.getStringExtra("screen");
        if(screenOn==null)
        {
            screenOn="on";
        }
        if(screenOn.equalsIgnoreCase("off"))
        {
            preferences  = PreferenceManager.getDefaultSharedPreferences(LockService.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("service");
            editor.commit();
            thread.interrupt();
            Log.e("screen off hai","screen off hai");
            Intent i = new Intent(LockService.this, LockService.class);
            LockService.this.stopService(i);

        }
        else {
            gson = new Gson();
            new_idea = new ArrayList<>();
            scheduleMethod();
            prefshelper = new Prefshelper(this);

        }
        return START_STICKY;
    }



    public void refresh_list()
    {
        Log.e("call","refresh");
        if(new_idea.size()>0) {
            new_idea.clear();
        }
        idea =new ArrayList<>();
        delete_app=new ArrayList<>();
        app_data=new ArrayList<App_Model>();
        arrayHelper=new ArrayHelper(LockService.this);

        app_lock=new ArrayList<>();
        app_lock=arrayHelper.get_app_lock_pkg("pkg_only");
        delete_app=arrayHelper.get_app_process_name_array("app");
        idea=arrayHelper.get_app_model_Array("model");
        if(idea.size()>0) {

            for(int g=0;g<idea.size();g++) {
                days=new ArrayList<>();
                start_time=new ArrayList<>();
                end_time=new ArrayList<>();
                start_date=new ArrayList<>();
                end_date=new ArrayList<>();
                pkg_name=new ArrayList<>();
                json=idea.get(g);
                String name=delete_app.get(g);
                modelObject1 = gson.fromJson(json, App_Model.class);
                Log.e("model data ",""+g+" "+json);

                try {
                    JSONObject object=new JSONObject(json);
                    JSONArray array=object.optJSONArray("app_name");
                    pkg_name.add(array.get(0).toString());
                    JSONArray arraydays=object.optJSONArray("days");
                        for(int dd=0;dd<arraydays.length();dd++)
                        {
                            days.add(arraydays.get(dd).toString());
                        }

                    JSONArray arrayedate=object.optJSONArray("end_date");
                    end_date.add(arrayedate.get(0).toString());
                    JSONArray arrayetime=object.optJSONArray("end_time");
                    end_time.add(arrayetime.get(0).toString());
                    JSONArray arraysdate=object.optJSONArray("start_date");
                    start_date.add(arraysdate.get(0).toString());
                    JSONArray arraystime=object.optJSONArray("start_time");
                    start_time.add(arraystime.get(0).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new_idea.add(model(pkg_name,days,end_date,end_time,start_date,start_time));
                set_app_data(new_idea);
            }
            arrayHelper.save_app_process_name_array("app",delete_app);
            arrayHelper.save_app_model_Array("model",idea);

        }
        else
        {
            Log.e("no data saved","idea");
        }
    }
    public ArrayList<App_Model> get_app_data() {
        return new_idea;
    }

    public void set_app_data(ArrayList<App_Model> list) {
        this.new_idea = list;
    }
    public App_Model model(List<String> pkg_name,List<String> days,List<String> end_date,List<String> end_time,List<String> start_date,List<String> start_time)
    {
        app_model=new App_Model();
        app_model.set_days(days);
        app_model.set_app_name(pkg_name);
        app_model.set_start_date(start_date);
        app_model.set_end_date(end_date);
        app_model.set_start_time(start_time);
        app_model.set_end_time(end_time);

        return app_model;
    }



    private void scheduleMethod() {


        if (Looper.myLooper() == Looper.getMainLooper()) {
            thread = new Thread() {
                @Override
                public void run() {

                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(5000);
                            refresh_list();
                            if (prefshelper.getservice().equalsIgnoreCase("1")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    Log.e("version", "version");
                                    p_name = getLollipopFGAppPackageName(LockService.this);
                                    Log.e("in check app", "" + p_name);
                                    if (!p_name.equalsIgnoreCase(prefshelper.getpkg())) {
                                        Log.e("pakg name", "" + prefshelper.getpkg());
                                        checkRunningApps();
                                    }


                                } else {
                                    ActivityManager manager =
                                            (ActivityManager) LockService.this.getSystemService(Context.ACTIVITY_SERVICE);
                                    List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();

                                    p_name = processes.get(0).processName;

                                    if (p_name.equalsIgnoreCase("com.ULock")) {

                                    } else {
                                        Log.e("inthread process name", "" + p_name);
                                        if (!p_name.equalsIgnoreCase(prefshelper.getpkg())) {
                                            Log.e("pakg name", "" + prefshelper.getpkg());
                                            checkRunningApps();
                                        }
                                    }
                                }
                            } else {

                                checkRunningApps();
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            };
            thread.start();
        }
    }

    public void checkRunningApps() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("version", "version" );
            p_name = getLollipopFGAppPackageName(LockService.this);
            Log.e("in check app", "" + p_name);
            loop();
        } else {
            ActivityManager manager =
                    (ActivityManager) LockService.this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();

            p_name = processes.get(0).processName;
//		Log.e("process name", "" + p_name);
            loop();
        }
    }
    public void loop()
    {
        days1=new ArrayList<>();
        start_time1=new ArrayList<>();
        end_time1=new ArrayList<>();
        start_date1=new ArrayList<>();
        end_date1=new ArrayList<>();
        pkg_name1=new ArrayList<>();
        for(int aa=0;aa<new_idea.size();aa++) {
            pkg_name1 = new_idea.get(aa).get_app_name();
            if (pkg_name1.size() > 0) {

                for (int i = 0; i < pkg_name1.size(); i++) {
                    Log.e("Position app", pkg_name1.get(i));
                    String packageNm = pkg_name1.get(i);
                    if (p_name.equalsIgnoreCase(packageNm)) {
                            days1=new_idea.get(aa).get_days();
                        Log.e("days in json",""+days1.toString());
                            if(days1.size()>0) {
                                for (int d = 0; d < days1.size(); d++) {

                                    String day_name = days1.get(d);
                                    Log.e("day loop", "day " + day_name);
                                    final Calendar c = Calendar.getInstance();
                                    int mYear = c.get(Calendar.YEAR);
                                    int mMonth = c.get(Calendar.MONTH);
                                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                                    start_date1=new_idea.get(aa).get_start_date();
                                    end_date1=new_idea.get(aa).get_start_date();
                                    String s_date = start_date1.get(0);
                                    String e_date = end_date1.get(0);
                                    String current_date = mDay + "-" + (mMonth + 1) + "-" + mYear;
//					Log.e("current date1",""+current_date);
                                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    try {
                                        Date c_date = df.parse(current_date);
                                        Log.e("current date2", "" + c_date);
                                        Date ss_date = df.parse(s_date);
                                        Log.e("start end date", "" + ss_date + " " + s_date + " " + e_date);
                                        Date ee_date = df.parse(e_date);
                                        if (ss_date.before(c_date)&&ee_date.before(c_date)) {
                                            Log.e("before date","before date");
                                            current_day_common(day_name,aa);

                                        }
                                        if (ss_date.after(c_date)) {
                                            Log.e("date not match", "date not match");
                                            String name=delete_app.get(i);
                                            if(name.equalsIgnoreCase(packageNm))
                                            {
                                                delete_app.remove(name);
                                                idea.remove(i);
                                            }
                                        }
                                        if (ee_date.equals(c_date)) {
                                            Log.e("date match", "date match");
                                            current_day_common(day_name,aa);
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                    } else {
                        Log.e("name", "name");

                    }
                }
            }
        }
        if (app_lock.size() > 0) {
            for (int l = 0; l < app_lock.size(); l++) {
                Log.e("Position app lock", app_lock.get(l));
                String packageNm1 = app_lock.get(l);
                if (p_name.equalsIgnoreCase(packageNm1)) {
                    prefshelper.storepkg(p_name);
                    Log.e("save app lock check","app lock check");
//
                    Intent mIntent = new Intent(LockService.this, All_Password.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent.putExtra("data", "0");
                    startActivity(mIntent);

                }
            }
        } else {
            Log.e("name", "name");
        }
    }
    public void current_day_common(String day_name,int aa)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date dd = new Date();
        String current_day = sdf.format(dd);
        Log.e("Current day",""+current_day+" "+day_name);
        if(day_name.equalsIgnoreCase(current_day))
        {
            start_time1=new_idea.get(aa).get_start_time();
            end_time1=new_idea.get(aa).get_end_time();
            String s_time=start_time1.get(0);
            String e_time=end_time1.get(0);
            Log.e("start and end time",""+s_time+" "+e_time);
            Calendar cal = Calendar.getInstance();
            DateFormat sdf2 = new SimpleDateFormat("HH:mm");
            try {
                String current_time=sdf2.format(cal.getTime());
                Date ss_time1=sdf2.parse(s_time);
                String ss_time=sdf2.format(ss_time1);
                Date ee_time1=sdf2.parse(e_time);
                String ee_time=sdf2.format(ee_time1);

                Log.e("current time",""+cal.getTime()+" "+ss_time1+" "+ee_time1);
                Log.e("current time",""+current_time+" "+ss_time+" "+ee_time);
                String a[]=current_time.split(":");
                String b[]=ss_time.split(":");
                String c[]=ee_time.split(":");

                int cchr=Integer.parseInt(a[0]);
                int ccmnt=Integer.parseInt(a[1]);
                int sthr=Integer.parseInt(b[0]);
                int stmnt=Integer.parseInt(b[1]);
                int ethr=Integer.parseInt(c[0]);
                int etmnt=Integer.parseInt(c[1]);

                if(sthr<=cchr&&cchr<=ethr)
                {
                    if(stmnt<=ccmnt&&ccmnt<=etmnt)
                    {

//                            if(ccmnt<=etmnt) {
                                Log.e("time haiga ", "time haiga");

                                prefshelper.storepkg(p_name);
                                Log.e("save app check", "app check");
//                                Intent mIntent = new Intent(LockService.this, MainActivity.class);
//                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                mIntent.putExtra("data", "0");
//                                startActivity(mIntent);

                        Intent mIntent = new Intent(LockService.this, All_Password.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mIntent.putExtra("data", "0");
                        startActivity(mIntent);

//                            }
//                            else
//                            {
//                                Log.e("chaklo app nu 2","chaklo app nu 2");
//                            }
                    }
                    else
                    {
                        Log.e("chaklo app nu","chaklo app nu");
                    }

                }


//
//                if(ss_time1.before(cal.getTime())&&ee_time1.before(cal.getTime()))
//                {
//
//                }
//                else if(ss_time1.before(cal.getTime())&&ee_time1.after(cal.getTime()))
//                {
//                    Log.e("time chakta ","time chakta");
//                }
//                if(ee_time1.equals(cal.getTime()))
//                {
//
//                }


            } catch (ParseException e) {
                e.printStackTrace();
            }


//			prefshelper.storepkg(p_name);
//			Log.e("save app check","app check");
//			Intent mIntent = new Intent(LockscreenService.this, MainActivity.class);
//			mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			mIntent.putExtra("data", "0");
//			startActivity(mIntent);
//
        }
    }

    private String getLollipopFGAppPackageName(Context ctx) {

        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) LockService.this.getSystemService(Context.USAGE_STATS_SERVICE);
            long milliSecs = 60 * 1000;
            Date date = new Date();
            List<UsageStats> queryUsageStats = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, date.getTime() - milliSecs, date.getTime());
            }
            if (queryUsageStats.size() > 0) {
                Log.e("size: ","" + queryUsageStats.size());
            }
            long recentTime = 0;
            String recentPkg = "";
            for (int i = 0; i < queryUsageStats.size(); i++) {
                UsageStats stats = queryUsageStats.get(i);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (i == 0 && !"com.ulock".equals(stats.getPackageName())) {
                        Log.e("PackageName: ","" + stats.getPackageName() + " " + stats.getLastTimeStamp());
                    }
                    if (stats.getLastTimeStamp() > recentTime) {
                        recentTime = stats.getLastTimeStamp();
                        recentPkg = stats.getPackageName();
                        Log.e("pkg",""+recentPkg);
                    }
                }
            }
            return recentPkg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



//	public static void stop() {
//		if (instance != null) {
//			instance.stopSelf();
//		}
//	}

    @Override
    public void onDestroy() {

        super.onDestroy();
unregisterReceiver(mReceiver);
       // Intent service = new Intent(LockService.this, LockService.class);
//		LockscreenService.this.startService(service);
       // onTaskRemoved(service);

    }
    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1,
                restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}
