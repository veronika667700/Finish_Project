package com.example.user.finish_project;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private Button btnMoney;
    private Button btnWeather;

    public static final int BTN_MONEY = 1;
    public static final int BTN_WEATHER = 2;

    public interface OnBtnPress {
        public void btnPressed(int btn);
    }

    private OnBtnPress listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        btnMoney = (Button) rootView.findViewById(R.id.btnMoney);
        btnWeather = (Button) rootView.findViewById(R.id.btnWeather);
        setOnClickListener();
        return rootView;
    }

    private void setOnClickListener() {
        btnMoney.setOnClickListener(this);
        btnWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            switch (v.getId()) {
                case R.id.btnMoney:
                    listener.btnPressed(BTN_MONEY);
                    break;
                case R.id.btnWeather:
                    listener.btnPressed(BTN_WEATHER);
                    break;
            }
        }
    }

    public void setBtnPressListener(OnBtnPress listener) {
        this.listener = listener;
    }

    public void removeBtnPressListener() {
        this.listener = null;
    }
}
