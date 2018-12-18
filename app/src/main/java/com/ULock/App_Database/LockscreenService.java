package com.ULock.App_Database;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.ULock.MainActivity;
import com.ULock.Prefhelper.Prefshelper;

import com.google.gson.Gson;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class LockscreenService extends Service {

	String CURRENT_PACKAGE_NAME = "com.ULock";
	String lastAppPN = "";
	boolean noDelay = false;
	int time;
	public static LockscreenService instance;
	Prefshelper prefshelper;
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
	Thread thread;
	ArrayList<String> app_lock;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
//	@Override
//	public void onCreate() {
//		thread = new HandlerThread("handler", android.os.Process.THREAD_PRIORITY_BACKGROUND);
//		thread.start();
//	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
//		lockSelectionActivity=new LockscreenIntentReceiver();
//		registerReceiver(lockSelectionActivity, new IntentFilter(Intent.ACTION_SCREEN_ON));
//		registerReceiver(lockSelectionActivity, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		gson = new Gson();
		scheduleMethod();
		CURRENT_PACKAGE_NAME = getApplicationContext().getPackageName();
		Log.e("Current PN", "" + CURRENT_PACKAGE_NAME);

		instance = this;
        prefshelper=new Prefshelper(this);
		return START_STICKY;
	}
public void refresh_list()
{
Log.e("call","refresh");


	idea =new ArrayList<>();
	delete_app=new ArrayList<>();
	app_data=new ArrayList<App_Model>();
	arrayHelper=new ArrayHelper(LockscreenService.this);
	pkg_name=new ArrayList<>();
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

			json=idea.get(g);
			String name=delete_app.get(g);
			modelObject1 = gson.fromJson(json, App_Model.class);
			Log.e("model data ",""+g+" "+json);
			pkg_name.add(modelObject1.get_app_name().get(0).toString());
			start_time.add(modelObject1.get_start_time().get(0).toString());
			end_time.add(modelObject1.get_end_time().get(0).toString());
			start_date.add(modelObject1.get_start_date().get(0).toString());
			end_date.add(modelObject1.get_end_date().get(0).toString());
			days=modelObject1.get_days();
			Log.e("days in json",""+days.toString());


			if(name.equalsIgnoreCase("Camera"))
			{
				delete_app.remove(name);
//                            del_pos=g;

				idea.remove(g);
			}



		if(prefshelper.getservice().equalsIgnoreCase("1")) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Log.e("version", "version" );
				p_name = getLollipopFGAppPackageName(LockscreenService.this);
				Log.e("in check app", "" + p_name);
				if (!p_name.equalsIgnoreCase(prefshelper.getpkg())) {
					Log.e("pakg name",""+prefshelper.getpkg());
					checkRunningApps();
				}


			} else {
				ActivityManager manager =
						(ActivityManager) LockscreenService.this.getSystemService(Context.ACTIVITY_SERVICE);
				List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();

				p_name = processes.get(0).processName;
//				Log.e("process name", "" + p_name);
				if (!p_name.equalsIgnoreCase(prefshelper.getpkg())) {
					Log.e("pakg name",""+prefshelper.getpkg());
					checkRunningApps();
				}
			}
		}
		else {

			checkRunningApps();
		}
		}
		arrayHelper.save_app_process_name_array("app",delete_app);
		arrayHelper.save_app_model_Array("model",idea);

	}
	else
	{
		Log.e("no data saved","idea");
	}
}
	private void scheduleMethod() {
		thread=new Thread() {
			@Override
			public void run() {
				for(int i = 1;i>0;i++)

				{

					try {
						Thread.sleep(7000);
						refresh_list();

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		}
		};thread.start();
	}

	public void checkRunningApps() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Log.e("version", "version" );
			p_name = getLollipopFGAppPackageName(LockscreenService.this);
			Log.e("in check app", "" + p_name);
			loop();
		} else {
			ActivityManager manager =
					(ActivityManager) LockscreenService.this.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();

		p_name = processes.get(0).processName;
//		Log.e("process name", "" + p_name);
			loop();
		}
				}
	public void loop()
	{
		for (int i = 0; i < pkg_name.size(); i++) {
			Log.e("Position app", pkg_name.get(i));
			String packageNm = pkg_name.get(i);
			if (p_name.equalsIgnoreCase(packageNm)) {

				for(int d=0;d<days.size();d++)
				{

					String day_name=days.get(d);
					Log.e("day loop","day "+day_name);
					final Calendar c = Calendar.getInstance();
					int mYear = c.get(Calendar.YEAR);
					int mMonth = c.get(Calendar.MONTH);
					int mDay = c.get(Calendar.DAY_OF_MONTH);

					String s_date=start_date.get(0);
					String  e_date=end_date.get(0);
					String current_date=mDay + "-" + (mMonth + 1) + "-" + mYear;
//					Log.e("current date1",""+current_date);
					DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					try {
						Date c_date=df.parse(current_date);
						Log.e("current date2",""+c_date);
						Date ss_date=df.parse(s_date);
						Log.e("start date",""+ss_date+" "+s_date+" "+e_date);
						Date ee_date=df.parse(e_date);
						if(ss_date.before(c_date))
						{
							current_day_common(day_name);
							Log.e("date not match","date not match");
						}
						if(ss_date.after(c_date))
						{
							Log.e("date not match","date not match");
						}
						if(ee_date.equals(c_date))
						{
							Log.e("date match","date match");
							current_day_common(day_name);
						}

					} catch (ParseException e) {
						e.printStackTrace();
					}

//
//					SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
//					Date dd = new Date();
//					String current_day = sdf.format(dd);
//					Log.e("Current day",""+current_day);
//					if(day_name.equalsIgnoreCase(current_day))
//					{
//						prefshelper.storepkg(p_name);
//						Log.e("save app check","app check");
//						Intent mIntent = new Intent(LockscreenService.this, MainActivity.class);
//						mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						mIntent.putExtra("data", "0");
//						startActivity(mIntent);
////
//					}
				}

			} else {
				Log.e("name", "name");

			}
		}
        if (app_lock.size() > 0) {
            for (int l = 0; l < app_lock.size(); l++) {
                Log.e("Position app lock", app_lock.get(l));
                String packageNm1 = app_lock.get(l);
                if (p_name.equalsIgnoreCase(packageNm1)) {
                    prefshelper.storepkg(p_name);
					Log.e("save app lock check","app lock check");
                    Intent mIntent = new Intent(LockscreenService.this, MainActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent.putExtra("data", "0");
                    startActivity(mIntent);

                }
            }
        } else {
            Log.e("name", "name");
        }
	}
	public void current_day_common(String day_name)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date dd = new Date();
		String current_day = sdf.format(dd);
		Log.e("Current day",""+current_day+" "+day_name);
		if(day_name.equalsIgnoreCase(current_day))
		{
			String s_time=start_time.get(0);
			String e_time=end_time.get(0);
			Log.e("start and end time",""+s_time+" "+e_time);
			Calendar cal = Calendar.getInstance();
			DateFormat sdf2 = new SimpleDateFormat("HH:mm a");
			try {
				String current_time=sdf2.format(cal.getTime());
				Date ss_time1=sdf2.parse(s_time);
				String ss_time=sdf2.format(ss_time1);
				Date ee_time1=sdf2.parse(e_time);
				String ee_time=sdf2.format(ee_time1);

				Log.e("current time",""+cal.getTime()+" "+ss_time1+" "+ee_time1);
				Log.e("current time",""+current_time+" "+ss_time+" "+ee_time);
					if(ss_time1.before(cal.getTime())&&ee_time1.before(cal.getTime()))
					{
						Log.e("time haiga ","time haiga");

						prefshelper.storepkg(p_name);
						Log.e("save app check","app check");
						Intent mIntent = new Intent(LockscreenService.this, MainActivity.class);
						mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mIntent.putExtra("data", "0");
						startActivity(mIntent);
					}
				else if(ss_time1.before(cal.getTime())&&ee_time1.after(cal.getTime()))
				{
					Log.e("time chakta ","time chakta");
				}
				if(ee_time1.equals(cal.getTime()))
				{
					Log.e("chaklo app nu","chaklo app nu");
				}


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
			UsageStatsManager usageStatsManager = (UsageStatsManager) LockscreenService.this.getSystemService(Context.USAGE_STATS_SERVICE);
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
//
		Intent service = new Intent(LockscreenService.this, LockscreenService.class);
//		LockscreenService.this.startService(service);
		onTaskRemoved(service);

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
