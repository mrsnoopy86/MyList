package ua.kh.tremtyachiy.mylist;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ua.kh.tremtyachiy.mylist.fragment.FragmentAdd;

/**
 * Created by User on 18.06.2015.
 */
public class MainScreen extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {
    Maps maps = new Maps();
    private Toolbar toolbar;
    private DrawerMenu drawerMyMenu = new DrawerMenu();
    private int OPEN_MAP = 0;
    private final int DIALOG = 1;
    private final int DIALOG_SECOND = 2;
    private EditText titleList;
    private EditText aboutList;
    private Button okeyDialog;
    private Button cancelDialog;
    private CheckBox checkBoxProduct;
    private CheckBox checkBoxBuild;
    private CheckBox checkBoxTech;
    private CheckBox checkBoxRemember;
    private CheckBox checkBoxToday;
    private CheckBox checkBoxImportant;
    private TextView textViewProduct;
    private TextView textViewBuild;
    private TextView textViewTech;
    private TextView textViewRemember;
    private TextView textViewToday;
    private TextView textViewImportant;
    private FragmentTransaction fragmentTransaction;
    FragmentAdd fragmentAdd = new FragmentAdd();
    private FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        initElements();
        drawerMyMenu.initDrawerMenu(this, toolbar);
    }

    private void initElements() {
        /*
        init Toolbar
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Списки");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add) {
                    showDialog(DIALOG);
                    return true;
                }
                return true;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerMyMenu.getDrawerMenu().openDrawer();
            }
        });
        /*
        Tabs
         */
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.ic_view_headline_white_24dp));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.ic_playlist_add_white_24dp));
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.ic_domain_white_24dp));
        tabSpec.setContent(R.id.tab3);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.ic_map_white_24dp));
        tabSpec.setContent(R.id.tab4);
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTabByTag("tag1");
        toolbar.getMenu().getItem(2).setVisible(false);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "tag1":
                        toolbar.getMenu().getItem(0).setVisible(true);
                        toolbar.getMenu().getItem(1).setVisible(true);
                        toolbar.getMenu().getItem(2).setVisible(false);
                        toolbar.setTitle("Списки");
                        break;
                    case "tag2":
                        toolbar.getMenu().getItem(1).setVisible(false);
                        toolbar.getMenu().getItem(0).setVisible(true);
                        toolbar.getMenu().getItem(2).setVisible(true);
                        toolbar.setTitle("Создать");
                        break;
                    case "tag3":
                        toolbar.getMenu().getItem(0).setVisible(false);
                        toolbar.getMenu().getItem(1).setVisible(true);
                        toolbar.getMenu().getItem(2).setVisible(false);
                        toolbar.setTitle("Адреса");
                        break;
                    case "tag4":
                        toolbar.getMenu().getItem(0).setVisible(false);
                        toolbar.getMenu().getItem(1).setVisible(false);
                        toolbar.getMenu().getItem(2).setVisible(false);
                        if (OPEN_MAP == 0) {
                            if (hasConnection()) {
                                maps.initMap(getFragmentManager());
                                maps.onMapReadyCamera();
                            } else {
                                createConnectionDialog();
                                maps.initMap(getFragmentManager());
                            }
                        }
                        OPEN_MAP = 1;
                        toolbar.setTitle("Карта");
                        break;
                }
            }
        });
    }

    private void createConnectionDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Отсутствует подключени.")
                .setContentText("Желаете перейти в настройки?")
                .setCancelText("Нет, не надо")
                .setConfirmText("Да, перейти")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                        maps.initMap(getFragmentManager());
                        maps.onMapReadyCamera();
                        sweetAlertDialog.cancel();
                    }
                })
                .show();
    }

    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        switch (id) {
            case 1:
                LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.my_alert_dialog, null);
                okeyDialog = (Button) view.findViewById(R.id.confirm_button);
                cancelDialog = (Button) view.findViewById(R.id.cancel_button);
                titleList = (EditText) view.findViewById(R.id.editText);
                aboutList = (EditText) view.findViewById(R.id.editText2);
                adb.setView(view);
                break;
            case 2:
                LinearLayout viewSecond = (LinearLayout) getLayoutInflater().inflate(R.layout.my_alert_dialog_second, null);
                checkBoxProduct = (CheckBox) viewSecond.findViewById(R.id.checkBoxProduct);
                checkBoxBuild = (CheckBox) viewSecond.findViewById(R.id.checkBoxBuild);
                checkBoxTech = (CheckBox) viewSecond.findViewById(R.id.checkBoxTech);
                checkBoxRemember = (CheckBox) viewSecond.findViewById(R.id.checkBoxRemeber);
                checkBoxToday = (CheckBox) viewSecond.findViewById(R.id.checkBoxToday);
                checkBoxImportant = (CheckBox) viewSecond.findViewById(R.id.checkBoxImportant);

                textViewProduct = (TextView) viewSecond.findViewById(R.id.textViewProduct);
                textViewBuild = (TextView) viewSecond.findViewById(R.id.textViewBuild);
                textViewTech = (TextView) viewSecond.findViewById(R.id.textViewTech);
                textViewRemember = (TextView) viewSecond.findViewById(R.id.textViewRemember);
                textViewToday = (TextView) viewSecond.findViewById(R.id.textViewToday);
                textViewImportant = (TextView) viewSecond.findViewById(R.id.textViewImportant);

                checkBoxProduct.setOnCheckedChangeListener(this);
                checkBoxBuild.setOnCheckedChangeListener(this);
                checkBoxTech.setOnCheckedChangeListener(this);
                checkBoxRemember.setOnCheckedChangeListener(this);
                checkBoxToday.setOnCheckedChangeListener(this);
                checkBoxImportant.setOnCheckedChangeListener(this);
                adb.setView(viewSecond);
                break;
            case 3:
                LinearLayout viewThird = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_dialog_succes, null);
                adb.setView(viewThird);
        }
        adb.setCancelable(false);
        return adb.create();
    }

    private void checkCancel() {
        checkBoxProduct.setChecked(false);
        checkBoxBuild.setChecked(false);
        checkBoxTech.setChecked(false);
        checkBoxRemember.setChecked(false);
        checkBoxToday.setChecked(false);
        checkBoxImportant.setChecked(false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_button:
                titleList.setText("");
                aboutList.setText("");
                dismissDialog(DIALOG);
                showDialog(DIALOG_SECOND);
                break;
            case R.id.cancel_button:
                titleList.setText("");
                aboutList.setText("");
                dismissDialog(DIALOG);
                break;
            case R.id.confirm_button2:
                dismissDialog(DIALOG_SECOND);
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Успешно.")
                        .setConfirmText("Да")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                if(fragmentAdd.isAdded()){ } else {
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    fragmentTransaction.add(R.id.tab2, fragmentAdd);
                                    fragmentTransaction.commit();
                                }
                            }
                        })
                        .show();
                checkCancel();
                break;
            case R.id.cancel_button2:
                dismissDialog(DIALOG_SECOND);
                checkCancel();
                checkBoxBuild.setOnCheckedChangeListener(this);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkBoxProduct:
                if (isChecked) {
                    checkBoxBuild.setChecked(false);
                    checkBoxTech.setChecked(false);
                    textViewProduct.setTextColor(getResources().getColor(R.color.blue_main_dark));
                } else {
                    textViewProduct.setTextColor(getResources().getColor(R.color.hint_text));
                }
                break;
            case R.id.checkBoxBuild:
                if (isChecked) {
                    checkBoxProduct.setChecked(false);
                    checkBoxTech.setChecked(false);
                    textViewBuild.setTextColor(getResources().getColor(R.color.blue_main_dark));
                } else {
                    textViewBuild.setTextColor(getResources().getColor(R.color.hint_text));
                }
                break;
            case R.id.checkBoxTech:
                if (isChecked) {
                    checkBoxBuild.setChecked(false);
                    checkBoxProduct.setChecked(false);
                    textViewTech.setTextColor(getResources().getColor(R.color.blue_main_dark));
                } else {
                    textViewTech.setTextColor(getResources().getColor(R.color.hint_text));
                }
                break;
            case R.id.checkBoxRemeber:
                if (isChecked) {
                    textViewRemember.setTextColor(getResources().getColor(R.color.blue_main_dark));
                } else {
                    textViewRemember.setTextColor(getResources().getColor(R.color.hint_text));
                }
                break;
            case R.id.checkBoxToday:
                if (isChecked) {
                    textViewToday.setTextColor(getResources().getColor(R.color.blue_main_dark));
                } else {
                    textViewToday.setTextColor(getResources().getColor(R.color.hint_text));
                }
                break;
            case R.id.checkBoxImportant:
                if (isChecked) {
                    textViewImportant.setTextColor(getResources().getColor(R.color.blue_main_dark));
                } else {
                    textViewImportant.setTextColor(getResources().getColor(R.color.hint_text));
                }
                break;
        }
    }
}
