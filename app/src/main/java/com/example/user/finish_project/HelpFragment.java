/*package com.example.user.finish_project;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFragment extends Fragment {

    private TextView Celcium;
    private TextView Farenheight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.navigation_header, container, false);
        Celcium = (TextView) rootView.findViewById(R.id.celcium);
        Farenheight = (TextView) rootView.findViewById(R.id.farenheight);
        return rootView;
    }
}
*/

package com.example.user.finish_project;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFragment extends Fragment {

    private TextView Celcium;
    private TextView Farenheight;

    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mainActivity = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new RuntimeException("ALERT!" + MainActivity.class.getName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.navigation_header, container, false);
        Celcium = (TextView) rootView.findViewById(R.id.celcium);
        Farenheight = (TextView) rootView.findViewById(R.id.farenheight);
        return rootView;
    }
}