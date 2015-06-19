package ua.kh.tremtyachiy.mylist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by User on 18.06.2015.
 */
public class MainScreen extends ActionBarActivity {
    Maps maps = new Maps();
    SweetAlertDialog pDialog;
    Toolbar toolbar;
    private DrawerMenu drawerMyMenu = new DrawerMenu();
    private int OPEN_MAP = 0;

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
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
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
                        break;
                    case "tag2":
                        toolbar.getMenu().getItem(1).setVisible(false);
                        toolbar.getMenu().getItem(0).setVisible(true);
                        toolbar.getMenu().getItem(2).setVisible(true);
                        break;
                    case "tag3":
                        toolbar.getMenu().getItem(0).setVisible(false);
                        toolbar.getMenu().getItem(1).setVisible(true);
                        toolbar.getMenu().getItem(2).setVisible(false);
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
}
