package ua.kh.tremtyachiy.mylist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by User on 18.06.2015.
 */
public class MainScreen extends ActionBarActivity {
    SweetAlertDialog pDialog;
    Toolbar toolbar;
    private DrawerMenu drawerMyMenu = new DrawerMenu();

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
    }

    public void onClickMe(View view){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("УРА!!!");
        pDialog.show();
    }
}
