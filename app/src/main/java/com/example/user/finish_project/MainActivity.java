package com.example.user.finish_project;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity
        implements MenuFragment.OnBtnPress{

    private MenuFragment menu;
    int rCnt;
    private DrawerLayout drawerlayout;
    private WeatherFragment weatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rCnt = 1;
        menu = new MenuFragment();

        setCurrentFragment(R.id.container, menu, false);
    }

    @Override
    public void btnPressed(int btn) {
        rCnt = 1;
        switch (btn) {
            case MenuFragment.BTN_MONEY:
                //setCurrentFragment(R.id.container, new MoneyFragment(), true);
                break;
            case MenuFragment.BTN_WEATHER:
                setCurrentFragment(R.id.container, new WeatherFragment(), true);
                break;
        }
    }

    @Override
    protected void onPause() {
        menu.removeBtnPressListener();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        menu.setBtnPressListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setCurrentFragment(int container, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.update) {
            System.out.println("Reconnect");
            WeatherFragment frag=(WeatherFragment)getSupportFragmentManager().findFragmentById(R.id.container);
            frag.ProgressTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
