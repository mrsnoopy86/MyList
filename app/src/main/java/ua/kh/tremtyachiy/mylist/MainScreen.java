package ua.kh.tremtyachiy.mylist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by User on 18.06.2015.
 */
public class MainScreen extends ActionBarActivity{
    Maps maps = new Maps();
    private Toolbar toolbar;
    private DrawerMenu drawerMyMenu = new DrawerMenu();
    private int OPEN_MAP = 0;
    private final int DIALOG = 1;
    private EditText titleList;
    private EditText aboutList;
    private Button okeyDialog;
    private Button cancelDialog;

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
                        if(OPEN_MAP == 0) {
                            if (hasConnection()) {
                                maps.initMap(getFragmentManager());
                            } else { createConnectionDialog(); }
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
                .setConfirmText("Да, прейти")
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
        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_dialog, null);
        okeyDialog = (Button) view.findViewById(R.id.confirm_button);
        cancelDialog = (Button) view.findViewById(R.id.cancel_button);
        titleList = (EditText) view.findViewById(R.id.editText);
        aboutList = (EditText) view.findViewById(R.id.editText2);
        adb.setView(view);
        return adb.create();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.confirm_button:
                titleList.setText("");
                aboutList.setText("");
                dismissDialog(DIALOG);
                break;
            case R.id.cancel_button:
                titleList.setText("");
                aboutList.setText("");
                dismissDialog(DIALOG);
                break;
        }
    }
}
