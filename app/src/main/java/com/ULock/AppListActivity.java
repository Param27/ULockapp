package com.ULock;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ULock.App_Database.App_Model;
import com.ULock.App_Database.ArrayHelper;
//import com.ULock.App_Database.LockscreenService;
import com.ULock.App_Database.LockService;
import com.ULock.App_Database.LockscreenService;
import com.ULock.Prefhelper.Prefshelper;
import com.ULock.utils.IabHelper;
import com.ULock.utils.IabResult;
import com.ULock.utils.Inventory;
import com.ULock.utils.Purchase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AppListActivity extends AppCompatActivity {
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
   // private ApplicationAdapter listadaptor = null;
  //   public static ListView listView;
    public  static GridView gridView;
    TextView txtTitle;
    ImageView imgBack;
    ImageView imgCart;
    CheckBox gridImageLock;
    TextView tv;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private CharSequence mDrawerTitle;
    FrameLayout f;
    ActionBarDrawerToggle mDrawerToggle;
    Prefshelper prefshelper;
    TextView spinnerDateFrom, spinnerDateTo, spinnerTimeFrom, spinnerTimeTo;
    List<String> dateFromList, dateToList, timeFromList, timeToList;
    private LayoutInflater mInflater;
    boolean isVisible=false;
    CheckBox checkBox;
    ImageView imgOne, imgTwo,imgThree;
    static Boolean state=false;
    int positon;
    int newPosition;
    String date, packageNameLocked;
    String time;
    String startDate;
    String stopDate;
    String startTime;
    String stopTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int visibleFlag = 0;
    LinearLayout llThreeAppsLock,llAds;

    String base64EncodedPublicKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxkqFO7wo7dWYkGz8wgQDj+hD9nKvPT8jdpglPuu0nmACQqer1QDngHkKC6ALM20JNls3GzPNcm2AHtSk5CHDjEXDA70/63E5oqQXIyPP/kzEBu2J92qa7CPn7wtYtnXXquphGFamWNmiORKPQywZQKcIiU4czrvUJu7LSvTzZ7XnTeo2ZbQf6vlmkZxBo/r/SOl8YxJq1XF15VxYUobboBg1CmnCaIZqryiB2uTv4dRHbaK+h2d6XN+JFS1v4syULg+Auc2/hitcONu8sZuM4UbnIattClcyzo/nuxAczmUI469Whsy4q95qEPm6QuDup7gowd3nwGJg/L4lhWsPLwIDAQAB";
    private static final String TAG = "com.ULock.inappbilling";
    IabHelper mHelper;

   // static final String ITEM_SKU = "android.test.purchased";
    static final String ITEM_SKU = "u_lock_paid";
    List<String> days,appname,start_date,end_date,start_time,end_time;
    ArrayList<String> delete_app,app_lock_pkg_only,app_lock, app_lock_time;
    ArrayList<App_Model> app_data;
    ArrayHelper arrayHelper;
    Gson gson;
    String json;
    App_Model app_model;
    ArrayList<String> idea;
    AppsAdapter appsAdapter;
    int sun=0,mon=0,tue=0,wed=0,thu=0,fri=0,sat=0;
    public AppListActivity()
    {

    }
    /*public AppListActivity(Boolean state)
    {
        Log.e("switch clicked","yes");
        this.state=state;
        Log.e("switch clicked",""+this.state);


    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list_grid);
        Intent service = new Intent(AppListActivity.this, LockService.class);
        AppListActivity.this.startService(service);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtTitle =(TextView)toolbar.findViewById(R.id.toolbar_title);
        txtTitle.setText("Home");

        imgBack =(ImageView)toolbar.findViewById(R.id.imageView_back);
        imgCart =(ImageView)toolbar.findViewById(R.id.image_cart);
        imgBack.setVisibility(View.GONE);
        gson = new Gson();
        idea =new ArrayList<>();
        delete_app=new ArrayList<>();
        app_data=new ArrayList<App_Model>();
        arrayHelper=new ArrayHelper(AppListActivity.this);
        app_lock_pkg_only=new ArrayList<String>();
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imgOne=(ImageView)findViewById(R.id.ist_app);
        imgTwo=(ImageView)findViewById(R.id.second_app);
        imgThree=(ImageView)findViewById(R.id.third_app);
        gridView=(GridView) findViewById(R.id.gridView);
       // listView=(ListView)findViewById(R.id.listView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigation);
        llThreeAppsLock=(LinearLayout)findViewById(R.id.ll_three_apps);
        llAds=(LinearLayout)findViewById(R.id.ll_ads);
        prefshelper=new Prefshelper(this);
        packageManager = getPackageManager();
        new LoadApplications().execute();
        navigationView.setItemIconTintList(null);

        /*Adding adds*/
        AdView adView = (AdView)this.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("TEST_DEVICE_ID")
                .build();
        adView.loadAd(adRequest);
       imgCart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dialogGoPro();
           }
       });
      /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ApplicationInfo app = applist.get(position);
                try {
                  *//*  Intent intent = packageManager
                            .getLaunchIntentForPackage(app.packageName);

                    if (null != intent) {
                        startActivity(intent);
                    }*//*
                    View row;
                    if (null == view) {
                        row = mInflater.inflate(R.layout.textview, null);
                    } else {
                        row = view;
                    }

                    TextView name = (TextView) row.findViewById(R.id.text);
                    name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogTimer();
                        }
                    });

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AppListActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(AppListActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

*/
        //IInAppPurchase start
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess()) {
                                               Log.d(TAG, "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d(TAG, "In-app Billing is set up OK");
                                           }
                                       }
                                   });

        if (drawerLayout != null) {
            drawerLayout.setDrawerShadow(R.drawable.list_back, GravityCompat.START);

            mDrawerToggle = new ActionBarDrawerToggle(AppListActivity.this, drawerLayout,
                    toolbar, R.string.drawer_open, R.string.drawer_close) {
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(mDrawerTitle);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    super.onDrawerOpened(drawerView);

                    invalidateOptionsMenu();

                }
            };
            drawerLayout.setDrawerListener(mDrawerToggle);

        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.drawer_how:

                        Intent intent = new Intent(AppListActivity.this, IntroductionActivity.class);
                        startActivity(intent);
                        finish();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.drawer_chngP:
                        String lockType = prefshelper.getLockType();

                        if (lockType.equalsIgnoreCase("gesture")) {
                            Intent i1 = new Intent(AppListActivity.this, ConfirmGestureActivity.class);
                            i1.putExtra("confirm", "");
                            i1.putExtra("change", "change");
                            startActivity(i1);
                            finish();
                        } else if (lockType.equalsIgnoreCase("password")) {
                            Intent i2 = new Intent(AppListActivity.this, ConfirmPassLockActivity.class);
                            i2.putExtra("change", "change");
                            i2.putExtra("confirm", "");
                            i2.putExtra("data1","data1");
                            startActivity(i2);
                            finish();
                        } else if (lockType.equalsIgnoreCase("pattern")) {
                            Intent i3 = new Intent(AppListActivity.this, ConfirmPatternActivity.class);
                            i3.putExtra("change", "change");
                            i3.putExtra("data1","data1");
                            i3.putExtra("confirm", "");
                            startActivity(i3);
                            finish();
                        } else if (lockType.equalsIgnoreCase("pin")) {
                            Intent i4 = new Intent(AppListActivity.this, ConfirmPinLockActivity.class);
                            i4.putExtra("change", "change");
                            i4.putExtra("confirm", "");
                            i4.putExtra("data1","data1");
                            startActivity(i4);
                            finish();
                        }

                        return true;

                    case R.id.drawer_premium:
                          dialogGoPro();
                        return true;
                    case R.id.drawer_share:
                            dialogShareApp();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
        if(!(prefshelper.getIstImage().equalsIgnoreCase("")) || !(prefshelper.getSecondImage().equalsIgnoreCase("")) ||!(prefshelper.getThirdImage().equalsIgnoreCase("")))
        {
            if(!(prefshelper.getIstImage().equalsIgnoreCase("")))
            {
                imgOne.setImageDrawable(new BitmapDrawable(getResources(), decodeBase64(prefshelper.getIstImage())));
            }
            if (!(prefshelper.getSecondImage().equalsIgnoreCase("")))
            {
                imgTwo.setImageDrawable(new BitmapDrawable(getResources(), decodeBase64(prefshelper.getSecondImage())));
            }
            if (!(prefshelper.getThirdImage().equalsIgnoreCase("")))
            {
                imgThree.setImageDrawable(new BitmapDrawable(getResources(), decodeBase64(prefshelper.getThirdImage())));
            }

        }
        if(prefshelper.getIInAppPurchased().equalsIgnoreCase("1"))
        {
            llAds.setVisibility(View.GONE);
            llThreeAppsLock.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)gridView.getLayoutParams();
            mlp.setMargins(0, 0, 0, 0);
        }
        else
        {
            llAds.setVisibility(View.VISIBLE);
            llThreeAppsLock.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)gridView.getLayoutParams();
            mlp.setMargins(0, 0, 0, 50);
        }


    }

    public void dialogShareApp(){
        String message = "https://play.google.com/store/apps/details?id=com.ULock&hl=en";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Via"));
    }
    public void dialogTimer() {
        days= new ArrayList<>();
        appname=new ArrayList<>();
        start_date=new ArrayList<>();
        end_date=new ArrayList<>();
        start_time=new ArrayList<>();
        end_time=new ArrayList<>();

        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_timer_popup);
        dialog.setTitle("Select Date and Time");
        spinnerDateFrom = (TextView) dialog.findViewById(R.id.spinner_date_from);
        spinnerDateTo = (TextView)  dialog.findViewById(R.id.spinner_date_to);
        spinnerTimeFrom = (TextView)  dialog.findViewById(R.id.spinner_time_from);
        spinnerTimeTo = (TextView)  dialog.findViewById(R.id.spinner_time_to);
        Button btnCancel=(Button)dialog.findViewById(R.id.button_cancel);
        Button btnOk=(Button)dialog.findViewById(R.id.button_ok);
        checkBox=(CheckBox)dialog.findViewById(R.id.checkBox);
        final TextView txtSunday=(TextView)dialog.findViewById(R.id.txt_sunday);
        final TextView txtMonday=(TextView)dialog.findViewById(R.id.txt_monday);
        final TextView txtTuesday=(TextView)dialog.findViewById(R.id.txt_tuesday);
        final TextView txtWednesday=(TextView)dialog.findViewById(R.id.txt_wednesday);
        final TextView txtThursday=(TextView)dialog.findViewById(R.id.txt_thursday);
        final TextView txtFriday=(TextView)dialog.findViewById(R.id.txt_friday);
        final TextView txtSaturday=(TextView)dialog.findViewById(R.id.txt_saturday);
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //Get Current Time
        final Calendar c1 = Calendar.getInstance();
        mHour = c1.get(Calendar.HOUR_OF_DAY);
        mMinute = c1.get(Calendar.MINUTE);
        date=mDay + "-" + (mMonth + 1) + "-" + mYear;
        time= mHour + ":" + mMinute;
        String time1 = null;
        try {
            String _24HourTime = time;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
//            time1=_12HourSDF.format(_24HourDt);
            time1=_24HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spinnerDateFrom.setText(date);
        spinnerDateTo.setText(date);
        spinnerTimeFrom.setText(time1);
        spinnerTimeTo.setText(time1);

        spinnerDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AppListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                startDate=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                spinnerDateFrom.setText(startDate);
                                if(start_date.size()>0)
                                {
                                    start_date.clear();
                                    start_date.add(startDate);
                                }
                                else
                                {
                                    start_date.add(startDate);
                                }
                                prefshelper.storeStartTimeOfApp(startDate+ "  " +positon);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });
     spinnerDateTo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AppListActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            stopDate=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            spinnerDateTo.setText(stopDate);
                            if(end_date.size()>0)
                            {
                                end_date.clear();
                                end_date.add(stopDate);
                            }
                            else
                            {
                                end_date.add(stopDate);
                            }
                            prefshelper.storeStopTimeOfApp(stopDate+ "  " +positon);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

     });
        spinnerTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AppListActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    String _24HourTime = hourOfDay + ":" + minute;
                                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                    Date _24HourDt = _24HourSDF.parse(_24HourTime);
                                    startTime=_24HourSDF.format(_24HourDt);
                                    spinnerTimeFrom.setText(startTime);
                                    if(start_time.size()>0)
                                    {
                                        start_time.clear();
                                        start_time.add(startTime);
                                    }
                                    else
                                    {
                                        start_time.add(startTime);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });
        spinnerTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AppListActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                try {
                                    String _24HourTime = hourOfDay + ":" + minute;
                                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                    Date _24HourDt = _24HourSDF.parse(_24HourTime);
                                    stopTime=_24HourSDF.format(_24HourDt);
                                    
                                    spinnerTimeTo.setText(stopTime);
                                    if(end_time.size()>0)
                                    {
                                        end_time.clear();
                                        end_time.add(stopTime);
                                    }
                                    else
                                    {
                                        end_time.add(stopTime);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(days.size()>0)
                    {
                        days.clear();
                        sun=1;mon=1;tue=1;wed=1;thu=1;fri=1;sat=1;
                        days.add("Sunday");
                        days.add("Monday");
                        days.add("Tuesday");
                        days.add("Wednesday");
                        days.add("Thursday");
                        days.add("Friday");
                        days.add("Saturday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                    }
                    else
                    {
                        sun=1;mon=1;tue=1;wed=1;thu=1;fri=1;sat=1;
                        days.add("Sunday");
                        days.add("Monday");
                        days.add("Tuesday");
                        days.add("Wednesday");
                        days.add("Thursday");
                        days.add("Friday");
                        days.add("Saturday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txtSunday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        txtMonday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        txtTuesday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        txtWednesday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        txtThursday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        txtFriday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        txtSaturday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                    }
                    else
                    {
                        txtSunday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        txtMonday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        txtTuesday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        txtWednesday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        txtThursday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        txtFriday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        txtSaturday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                    }
                }
                else
                {
                    if(days.size()>0)
                    {
                        sun=0;mon=0;tue=0;wed=0;thu=0;fri=0;sat=0;
                        days.clear();
                        start_date.clear();
                        end_date.clear();
                        start_time.clear();
                        end_time.clear();
                    }
//
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txtSunday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        txtMonday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        txtTuesday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        txtWednesday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        txtThursday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        txtFriday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        txtSaturday.setBackground(getResources().getDrawable(R.drawable.day, null));
                    }
                    else
                    {
                        txtSunday.setBackground(getResources().getDrawable(R.drawable.day));
                        txtMonday.setBackground(getResources().getDrawable(R.drawable.day));
                        txtTuesday.setBackground(getResources().getDrawable(R.drawable.day));
                        txtWednesday.setBackground(getResources().getDrawable(R.drawable.day));
                        txtThursday.setBackground(getResources().getDrawable(R.drawable.day));
                        txtFriday.setBackground(getResources().getDrawable(R.drawable.day));
                        txtSaturday.setBackground(getResources().getDrawable(R.drawable.day));
                    }
                }
            }
        });
        txtSunday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    if(sun==0)
                    {
                        days.add("Sunday");
                        Log.e("add ",""+days.toString());
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtSunday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        sun=1;
//                        isVisible=false;
                    }
//                    else if(!isVisible)
                    else if(sun==1)
                    {
                        days.remove("Sunday");
                        Log.e("add ",""+days.toString());
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtSunday.setBackground(getResources().getDrawable(R.drawable.day, null));
//                        isVisible=true;
                        sun=0;
                    }
                }
                else
                {

//                    if(isVisible)
                    if(sun==0)
                    {
                        days.add("Sunday");
                        Log.e("add ",""+days.toString());
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtSunday.setBackground(getResources().getDrawable(R.drawable.day_grey));
//                        isVisible=false;
                        sun=1;
                    }
//                    else if(!isVisible)
                    else if(sun==1)
                    {
                        days.remove("Sunday");
                        Log.e("add ",""+days.toString());
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtSunday.setBackground(getResources().getDrawable(R.drawable.day));
                        sun=0;
//                        isVisible=true;
                    }
                }
            }
        });
        txtMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if(isVisible)
                    if(mon==0)
                    {
                        days.add("Monday");
                        Log.e("add ",""+days.toString());
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtMonday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
//                        isVisible=false;
                        mon=1;
                    }
//                    else if(!isVisible)
                    else if(mon==1)
                    {
                        days.remove("Monday");
                        Log.e("add ",""+days.toString());
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtMonday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        mon=0;
//                        isVisible=true;
                    }
                }
                else
                {

                    if(mon==0)
                    {
                        days.add("Monday");
                        Log.e("add ",""+days.toString());
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtMonday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        mon=1;
//                        isVisible=false;
                    }
                    else if(mon==1)
                    {
                        days.remove("Monday");
                        Log.e("add ",""+days.toString());
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtMonday.setBackground(getResources().getDrawable(R.drawable.day));
                        mon=0;
//                        isVisible=true;
                    }
                }
            }
        });
        txtTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    if(tue==1)
                    {
                        days.add("Tuesday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtTuesday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        tue=0;
//                        isVisible=false;
                    }
                    else if(tue==0)
                    {
                        days.remove("Tuesday");
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtTuesday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        tue=1;
//                        isVisible=true;
                    }
                }
                else
                {

                    if(tue==1)
                    {
                        days.add("Tuesday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtTuesday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        tue=0;
//                        isVisible=false;
                    }
                    else if(tue==0)
                    {
                        days.remove("Tuesday");
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtTuesday.setBackground(getResources().getDrawable(R.drawable.day));
                        tue=1;
//                        isVisible=true;
                    }
                }
            }
        });
        txtWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(wed==1)
                    {
                        days.add("Wednesday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtWednesday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        wed=0;
//                        isVisible=false;
                    }
                    else if(wed==0)
                    {
                        days.remove("Wednesday");
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtWednesday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        wed=1;
//                        isVisible=true;
                    }
                }
                else
                {

                    if(wed==1)
                    {
                        days.add("Wednesday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtWednesday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        wed=0;
//                        isVisible=false;
                    }
                    else if(wed==0)
                    {
                        days.remove("Wednesday");
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtWednesday.setBackground(getResources().getDrawable(R.drawable.day));
                        wed=1;
//                        isVisible=true;
                    }
                }
            }
        });
        txtThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(thu==1)
                    {
                        days.add("Thursday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtThursday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        thu=0;
//                        isVisible=false;
                    }
                    else if(thu==0)
                    {
                        days.remove("Thursday");
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtThursday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        thu=1;
//                        isVisible=true;
                    }
                }
                else
                {

                    if(thu==1)
                    {
                        days.add("Thursday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtThursday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        thu=0;
//                        isVisible=false;
                    }
                    else if(thu==0)
                    {
                        days.remove("Thursday");
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtThursday.setBackground(getResources().getDrawable(R.drawable.day));
                        thu=1;
//                        isVisible=true;
                    }
                }
            }
        });
        txtFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(fri==1)
                    {
                        days.add("Friday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtFriday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        fri=0;
//                        isVisible=false;
                    }
                    else if(fri==0)
                    {
                        days.remove("Friday");
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtFriday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        fri=1;
//                        isVisible=true;
                    }
                }
                else
                {


                    if(fri==1)
                    {
                        days.add("Friday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtFriday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        fri=0;
//                        isVisible=false;
                    }
                    else if(fri==0)
                    {
                        days.remove("Friday");
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtFriday.setBackground(getResources().getDrawable(R.drawable.day));
                        fri=1;
//                        isVisible=true;
                    }
                }
            }
        });
        txtSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(sat==1)
                    {
                        days.add("Saturday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtSaturday.setBackground(getResources().getDrawable(R.drawable.day_grey, null));
                        sat=0;
//                        isVisible=false;
                    }
                    else if(sat==0)
                    {
                        days.remove("Saturday");
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtSaturday.setBackground(getResources().getDrawable(R.drawable.day, null));
                        sat=1;
//                        isVisible=true;
                    }
                }
                else
                {

                    if(sat==1)
                    {
                        days.add("Saturday");
                        start_date.add(spinnerDateFrom.getText().toString());
                        end_date.add(spinnerDateTo.getText().toString());
                        start_time.add(spinnerTimeFrom.getText().toString());
                        end_time.add(spinnerTimeTo.getText().toString());
                        txtSaturday.setBackground(getResources().getDrawable(R.drawable.day_grey));
                        sat=0;
//                        isVisible=false;
                    }
                    else if(sat==0)
                    {
                        days.remove("Saturday");
                        if(start_date.size()>0) {
                            start_date.clear();
                            end_date.clear();
                            start_time.clear();
                            end_time.clear();
                            start_date.add(spinnerDateFrom.getText().toString());
                            end_date.add(spinnerDateTo.getText().toString());
                            start_time.add(spinnerTimeFrom.getText().toString());
                            end_time.add(spinnerTimeTo.getText().toString());
                        }
                        txtSaturday.setBackground(getResources().getDrawable(R.drawable.day));
                        sat=1;
//                        isVisible=true;
                    }
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(days.size()>0) {
                    dialog.dismiss();

                    idea = arrayHelper.get_app_model_Array("model");
                    delete_app = arrayHelper.get_app_process_name_array("app");
                    ApplicationInfo applicationInfo1 = applist.get(positon);
                    appname.add("" + applicationInfo1.processName);
                    delete_app.add("" + applicationInfo1.processName);

                    app_data.add(model(days, appname, start_date, end_date, start_time, end_time));
                    set_app_data(app_data);

                    Log.e("save array", "" + app_data.toString());
                    json = gson.toJson(app_model);
                    Log.e("json object ", "" + json.toString());
                    idea.add(json);
                    arrayHelper.save_app_model_Array("model", idea);
                    arrayHelper.save_app_process_name_array("app", delete_app);
                    common();
                    Log.e("image.......", "" + applicationInfo1.loadLabel(packageManager));
                }
                else
                {
                    Toast.makeText(AppListActivity.this,"Please specify a day for your lock.",Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }
    public ArrayList<App_Model> get_app_data() {
        return app_data;
    }

    public void set_app_data(ArrayList<App_Model> list) {
        this.app_data = list;
    }
    public App_Model model(List<String> days,List<String> appname,List<String> start_date,List<String> end_date,List<String> start_time,List<String> end_time)
    {
        app_model=new App_Model();
        app_model.set_days(days);
        app_model.set_app_name(appname);
        app_model.set_start_date(start_date);
        app_model.set_end_date(end_date);
        app_model.set_start_time(start_time);
        app_model.set_end_time(end_time);

        return app_model;
    }
    public void dialogGoPro() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_gopro_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnBuyNow= (Button)dialog.findViewById(R.id.button_buynow);
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHelper.launchPurchaseFlow(AppListActivity.this, ITEM_SKU, 10001,
                        mPurchaseFinishedListener, "mypurchasetoken");
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();

            }

        }
    };
    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        prefshelper.isIInAppPurchased("1");
                        llThreeAppsLock.setVisibility(View.GONE);
                        llAds.setVisibility(View.GONE);
                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)gridView.getLayoutParams();
                        mlp.setMargins(0, 0, 0, 0);
                      /*  if(prefshelper.getPositionOfIstApp()!=null)
                        {

                        }
                        if(prefshelper.getPositionOfSecApp()!=null)
                        {

                        }
                        if(prefshelper.getPositionOfThirdApp()!=null)
                        {

                        }*/

                    } else {
                        Toast.makeText(AppListActivity.this,"Please try again",Toast.LENGTH_SHORT).show();
                    }
                }
            };
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null)
            mHelper.dispose();
            mHelper = null;
    }
    public void dialogSelectTask() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_task);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout llSetTimer=(LinearLayout)dialog.findViewById(R.id.ll_timer);
        LinearLayout llLockApp=(LinearLayout)dialog.findViewById(R.id.ll_lock);
        Button btnCancel=(Button)dialog.findViewById(R.id.button_buynow);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        llSetTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prefshelper.storeTimer("1");
                if (prefshelper.getIInAppPurchased().equalsIgnoreCase("")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (encodeTobase64(drawableToBitmap(imgOne.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial,null))))) {
                            dialogTimer();
                        } else if (encodeTobase64(drawableToBitmap(imgTwo.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial,null))))) {
                            dialogTimer();
                        } else  if (encodeTobase64(drawableToBitmap(imgThree.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial,null))))) {
                            dialogTimer();
                        } else {
                            dialogGoPro();
                        }
                    } else {
                        if (encodeTobase64(drawableToBitmap(imgOne.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial))))) {
                            dialogTimer();
                        } else if (encodeTobase64(drawableToBitmap(imgTwo.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial))))) {
                            dialogTimer();
                        } else  if (encodeTobase64(drawableToBitmap(imgThree.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial))))) {
                            dialogTimer();
                        } else {
                            dialogGoPro();
                        }
                    }
                } else {
                    dialogTimer();
                }
            }
        });
        llLockApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                app_lock_pkg_only=arrayHelper.get_app_lock_pkg("pkg_only");
                ApplicationInfo applicationInfo1=applist.get(positon);
                app_lock_pkg_only.add(""+applicationInfo1.processName);
                arrayHelper.save_app_lock_pkg("pkg_only",app_lock_pkg_only);
                    Log.e("app lock data",""+arrayHelper.get_app_lock_pkg("pkg_only"));
                common();

            }
        });
        dialog.show();
    }
public void common()
{
    if (!(prefshelper.getIInAppPurchased().equalsIgnoreCase("1"))) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (encodeTobase64(drawableToBitmap(imgOne.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial, null))))) {
                imgOne.setImageDrawable((applist.get(positon)).loadIcon(packageManager));
                prefshelper.istImage(encodeTobase64(drawableToBitmap(((applist.get(positon)).loadIcon(packageManager)))));
                prefshelper.positionOfIstApp(String.valueOf(positon));
                appsAdapter.toggle(positon);
                ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();


            } else if (encodeTobase64(drawableToBitmap(imgTwo.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial, null))))) {
                if (!(prefshelper.getPositionOfIstApp().equalsIgnoreCase(String.valueOf(positon)))) {
                    if (encodeTobase64(drawableToBitmap(applist.get(positon).loadIcon(packageManager))).equals(prefshelper.getIstImage())) {
                        Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();
                    }
                    else {

                        imgTwo.setImageDrawable((applist.get(positon)).loadIcon(packageManager));
                        prefshelper.secondImage(encodeTobase64(drawableToBitmap(((applist.get(positon)).loadIcon(packageManager)))));
                        prefshelper.positionOfSecApp(String.valueOf(positon));

                        appsAdapter.toggle(positon);
                        ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();

                    }
                }
                else {
                    Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();

                }

            } else if (encodeTobase64(drawableToBitmap(imgThree.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial, null))))) {
                if (!(prefshelper.getPositionOfSecApp().equalsIgnoreCase(String.valueOf(positon))) && !(prefshelper.getPositionOfIstApp().equalsIgnoreCase(String.valueOf(positon)))) {

                    if (encodeTobase64(drawableToBitmap(applist.get(positon).loadIcon(packageManager))).equals(prefshelper.getIstImage())) {
                        Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();
                    } else if (encodeTobase64(drawableToBitmap(applist.get(positon).loadIcon(packageManager))).equals(prefshelper.getSecondImage())) {
                        Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();
                    } else {
                        imgThree.setImageDrawable((applist.get(positon)).loadIcon(packageManager));
                        prefshelper.thirdImage(encodeTobase64(drawableToBitmap(((applist.get(positon)).loadIcon(packageManager)))));
                        prefshelper.positionOfThirdApp(String.valueOf(positon));
                        appsAdapter.toggle(positon);
                        ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();



                    }
                } else {
                    Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();

                }
            } else {
                dialogGoPro();
            }
        } else {
            if (encodeTobase64(drawableToBitmap(imgOne.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial))))) {
                Log.e("Hiiiiiiiiiiiiiii", "u der?");
                imgOne.setImageDrawable((applist.get(positon)).loadIcon(packageManager));
                prefshelper.istImage(encodeTobase64(drawableToBitmap(((applist.get(positon)).loadIcon(packageManager)))));
                prefshelper.positionOfIstApp(String.valueOf(positon));


                appsAdapter.toggle(positon);
                ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();

            }

           else if (encodeTobase64(drawableToBitmap(imgTwo.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial))))) {
                if (!(prefshelper.getPositionOfIstApp().equalsIgnoreCase(String.valueOf(positon)))) {

                    if (encodeTobase64(drawableToBitmap(applist.get(positon).loadIcon(packageManager))).equals(prefshelper.getIstImage())) {
                         Log.e("i am showing", "hereee");
                        Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();
                    } else {
                        imgTwo.setImageDrawable((applist.get(positon)).loadIcon(packageManager));
                        prefshelper.secondImage(encodeTobase64(drawableToBitmap(((applist.get(positon)).loadIcon(packageManager)))));
                        prefshelper.positionOfSecApp(String.valueOf(positon));
                        appsAdapter.toggle(positon);
                        ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();



                    }
                } else {
                    Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();
                    Log.e("no..i am showing", "hereee");
                }
            } else if (encodeTobase64(drawableToBitmap(imgThree.getDrawable())).equals(encodeTobase64(drawableToBitmap(getResources().getDrawable(R.drawable.initial))))) {
                if (!(prefshelper.getPositionOfSecApp().equalsIgnoreCase(String.valueOf(positon))) && !(prefshelper.getPositionOfIstApp().equalsIgnoreCase(String.valueOf(positon)))) {

                    if (encodeTobase64(drawableToBitmap(applist.get(positon).loadIcon(packageManager))).equals(prefshelper.getIstImage())) {
                        Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();
                    } else if (encodeTobase64(drawableToBitmap(applist.get(positon).loadIcon(packageManager))).equals(prefshelper.getSecondImage())) {
                        Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();
                    } else {
                        imgThree.setImageDrawable((applist.get(positon)).loadIcon(packageManager));
                        prefshelper.thirdImage(encodeTobase64(drawableToBitmap(((applist.get(positon)).loadIcon(packageManager)))));
                        prefshelper.positionOfThirdApp(String.valueOf(positon));

                        appsAdapter.toggle(positon);
                        ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();


                    }
                } else {
                    Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();
                    Log.e("nope..i am showing", "hereee");
                }
            } else {
                dialogGoPro();

            }
        }

    }
    else
    {
        appsAdapter.toggle(positon);
        ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();

    }
}

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {

                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Collections.sort(applist, new ApplicationInfo.DisplayNameComparator(packageManager));

        return applist;
    }
    @Override

    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.

        mDrawerToggle.syncState();

    }


    @Override

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles

        mDrawerToggle.onConfigurationChanged(newConfig);

    }
    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            /*listadaptor = new ApplicationAdapter(AppListActivity.this,
                    R.layout.list_item, applist);*/

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            appsAdapter=new AppsAdapter(AppListActivity.this);
            gridView.setAdapter(appsAdapter);
            //listView.setAdapter(listadaptor);
            progress.dismiss();
 /* Check for selecting more than three apps*/
            app_lock=new ArrayList<>();
            app_lock_time=new ArrayList<>();
            if(prefshelper.getTimer().equalsIgnoreCase("1"))
            {
                idea=arrayHelper.get_app_model_Array("model");
                if(idea.size()>0) {
                    Log.e("jsonArray",idea.size()+"");
                    for (int g = 0; g < idea.size(); g++) {

                        json = idea.get(g);

                        try {
                            JSONObject object = new JSONObject(json);
                            JSONArray array = object.optJSONArray("app_name");

                            for(int a=0; a<array.length();a++)
                            {
                                app_lock_time.add(array.get(a).toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (app_lock_time.size() > 0) {
                        for (int j = 0; j < applist.size(); j++) {
                            Log.e("Position app lock", applist.get(j).packageName);
                            String packageName = applist.get(j).packageName;
                            for (int k = 0; k < app_lock_time.size(); k++) {
                                packageNameLocked = app_lock_time.get(k);
                                if (packageNameLocked.equalsIgnoreCase(packageName)) {

                                    appsAdapter.toggle(j);
                                    ((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();

                                }
                            }
                        }
                    }
                }
            }
            else {
                app_lock = arrayHelper.get_app_lock_pkg("pkg_only");
                if (app_lock.size() > 0) {
                    for (int j = 0; j < applist.size(); j++) {
                        Log.e("Position app lock", applist.get(j).packageName);
                        String packageName = applist.get(j).packageName;
                        for (int k = 0; k < app_lock.size(); k++) {
                            packageNameLocked = app_lock.get(k);
                            if (packageNameLocked.equalsIgnoreCase(packageName)) {

                                appsAdapter.toggle(j);
                                ((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();

                            }
                        }
                    }
                }
            }

            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AppListActivity.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
    public class AppsAdapter extends ArrayAdapter implements  CompoundButton.OnCheckedChangeListener {
        SparseBooleanArray sba;
        LayoutInflater layoutInflater;

        public AppsAdapter(Context context) {
            super(context,0);
            sba=new SparseBooleanArray(gridView.getCount());
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public View getView(final int position, View convertView, final ViewGroup parent) {
            ImageView i;
            View view = convertView;

            if (null == view) {
                 view = layoutInflater.inflate(R.layout.grid_item, parent,false);
            }

            final ApplicationInfo applicationInfo = applist.get(position);
            if (null != applicationInfo) {
                RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.ll_grid_item);
                TextView appName = (TextView) view.findViewById(R.id.app_name);
                gridImageLock = (CheckBox) view.findViewById(R.id.lock);

                ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
                gridImageLock.setTag(position);
                gridImageLock.setOnCheckedChangeListener(this);
                gridImageLock.setEnabled(false);
                gridImageLock.setChecked(sba.get(position, false));
                if (!((applicationInfo.loadLabel(packageManager)).equals("ULock"))) {
                    appName.setText(applicationInfo.loadLabel(packageManager));
                    iconview.setImageDrawable(applicationInfo.loadIcon(packageManager));
                    relativeLayout.setTag(position);
                }


            }
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    newPosition = i;
                    positon = (Integer) view.getTag();
                    if (prefshelper.getIInAppPurchased().equalsIgnoreCase("1")) {
                        if (isChecked(i)) {
                            toggle(i);
                            ((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
                            if (prefshelper.getTimer().equalsIgnoreCase("1")) {
                                delete_app = arrayHelper.get_app_process_name_array("app");
                                String name = "";
                                idea = arrayHelper.get_app_model_Array("model");
                                if (idea.size() > 0) {
                                    Log.e("jsonArray", idea.size() + "");
                                    for (int g = 0; g < idea.size(); g++) {

                                        json = idea.get(g);

                                        try {
                                            JSONObject object = new JSONObject(json);
                                            JSONArray array = object.optJSONArray("app_name");

                                            for (int a = 0; a < array.length(); a++) {
                                                app_lock_time.add(array.get(a).toString());
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (app_lock_time.size() > 0) {
                                            for (int h = 0; h < app_lock_time.size(); h++) {
                                                String pName = app_lock_time.get(h);
                                                if (delete_app.size() > 0) {
                                                    for (int k = 0; k < delete_app.size(); k++) {
                                                        name = delete_app.get(k);
                                                        if (name.equalsIgnoreCase(pName)) {
                                                            delete_app.remove(name);
                                                            idea.remove(g);
                                                        }
                                                    }
                                                }
                                            }
                                            arrayHelper.save_app_process_name_array("app", delete_app);
                                            arrayHelper.save_app_model_Array("model", idea);

                                        } else {
                                            Log.e("no data saved", "idea");
                                        }
                                    }
                                }


                            } else {

                                app_lock_pkg_only = arrayHelper.get_app_lock_pkg("pkg_only");
                                ApplicationInfo applicationInfo1 = applist.get(i);
                                app_lock_pkg_only.remove("" + applicationInfo1.packageName);
                                arrayHelper.save_app_lock_pkg("pkg_only", app_lock_pkg_only);
                                Log.e("app lock data", "" + arrayHelper.get_app_lock_pkg("pkg_only"));
                            }
                        } else {
                            dialogSelectTask();
                        }

                    } else {

                        if (prefshelper.getPositionOfIstApp().equalsIgnoreCase("") ||
                                prefshelper.getPositionOfSecApp().equalsIgnoreCase("") || prefshelper.getPositionOfThirdApp().equalsIgnoreCase("")) {

                            if (isChecked(i)) {
                                Toast.makeText(AppListActivity.this, "Already Locked, please select another app.", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                dialogSelectTask();
                            }

                        } else {
                            dialogGoPro();
                        }
                    }

                }
            });

            return view;
            }



        public final int getCount() {
            return applist.size();
        }

        public final Object getItem(int position) {
            return applist.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
        public boolean isChecked(int position) {
            return sba.get(position, false);
        }

        public void setChecked(int position, boolean isChecked) {
            sba.put(position, isChecked);

        }

        public void toggle(int position) {
            setChecked(position, !isChecked(position));
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
           sba.put((Integer) buttonView.getTag(), isChecked);

        }


    }
}


