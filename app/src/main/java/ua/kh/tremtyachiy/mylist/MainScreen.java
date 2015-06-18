package ua.kh.tremtyachiy.mylist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by User on 18.06.2015.
 */
public class MainScreen extends ActionBarActivity {
    Button btn;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        btn = (Button) findViewById(R.id.buttonAdd);
    }

    public void onClickMe(View view){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("УРА!!!");
        pDialog.show();
    }
}
