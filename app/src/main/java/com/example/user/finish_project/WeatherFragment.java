package com.example.user.finish_project;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherFragment extends android.support.v4.app.Fragment {
    TextView contentView;               // отвечает за градусы на сегодняшний день
    ImageView img, img1, img2, img3;
    String contentText = null;
    String contentAdd = null;
    TextView contentTAdd;               // отвечает за доп. информацию на сегодняшний день
    TextView date;                      // отвечает за текущую дату
    String jsonhandler;
    Spinner spinner1;
    Spinner spinner2;
    Week w;
    TextView t1, t2, t3;
    TextView[] t = new TextView[4];
    ImageView[] imgArr = new ImageView[4];
    int city1 = 501175;                     // Ростов-на-Дону
    String city = Integer.toString(city1);
    String[] laender = {"-Выберите Федеративную землю-", "Baden-Württemberg", "Freistaat Bayern", "Rheinland-Pfalz", "Nordrhein-Westfalen", "Freistaat Sachsen"};
    String[] citiesBW = {"-Выберите город-", "Stuttgart", "Karlsruhe", "Freiburg", "Heidelberg", "Mannheim"};
    String[] citiesFB = {"-Выберите город-", "München", "Nürnberg", "Passau"};
    String[] citiesRP = {"-Выберите город-", "Koblenz", "Trier", "Mainz", "Neustadt an der Weinstraße"};
    String[] citiesNW = {"-Выберите город-", "Köln", "Düsseldorf", "Dortmund", "Essen", "Münster", "Wuppertal", "Bonn"};
    String[] citiesFS = {"-Выберите город-", "Dresden", "Leipzig", "Torgau", "Freiberg"};

    //Логическая переменная для статуса соединения
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    Map<String,Integer> citiesId = new HashMap<>();

    class Week {
        public String date, temp_min, temp_max, description;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        //Создаем пример класса connection detector:
        cd = new ConnectionDetector(getActivity());

        citiesId.put("Stuttgart", 2825297);
        citiesId.put("Karlsruhe", 2892794);
        citiesId.put("Freiburg", 2925177);
        citiesId.put("Heidelberg", 2907911);
        citiesId.put("Mannheim", 2873891);
        citiesId.put("München", 6940463);
        citiesId.put("Nürnberg", 2861650);
        citiesId.put("Passau", 2855328);
        citiesId.put("Koblenz", 2886946);
        citiesId.put("Trier", 2821164);
        citiesId.put("Mainz", 2874225);
        citiesId.put("Neustadt an der Weinstraße", 2864054);
        citiesId.put("Köln", 6691073);
        citiesId.put("Düsseldorf", 2934246);
        citiesId.put("Dortmund", 2935517);
        citiesId.put("Essen", 2928810);
        citiesId.put("Münster", 2867543);
        citiesId.put("Wuppertal", 2805753);
        citiesId.put("Bonn", 2946447);
        citiesId.put("Dresden", 2935022);
        citiesId.put("Leipzig", 2879139);
        citiesId.put("Torgau", 2821807);
        citiesId.put("Freiberg", 2925177);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        contentView = (TextView) view.findViewById(R.id.contentMainTemp);
        contentTAdd = (TextView) view.findViewById(R.id.textView2);
        img = (ImageView) view.findViewById(R.id.imageView);
        date = (TextView) view.findViewById(R.id.contentDate);
        Calendar c = Calendar.getInstance();
        date.setText(c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.YEAR) + "\n");
        t1 = (TextView) view.findViewById(R.id.txt1);
        t2 = (TextView) view.findViewById(R.id.txt2);
        t3 = (TextView) view.findViewById(R.id.txt3);
        img1 = (ImageView) view.findViewById(R.id.img1);
        img2 = (ImageView) view.findViewById(R.id.img2);
        img3 = (ImageView) view.findViewById(R.id.img3);

        //СПИННЕРЫ
        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, laender);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        setOnClickListeners();

        if (contentText != null)
            contentView.setText(contentText);
        if (contentAdd != null)
            contentTAdd.setText(contentAdd);

        t[0] = t1;
        t[1] = t2;
        t[2] = t3;

        imgArr[0] = img1;
        imgArr[1] = img2;
        imgArr[2] = img3;

        for (int i = 0; i < 3; i++)
            t[i].setText(null);
        return view;
    }

    private void setOnClickListeners() {

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapter2;
                ProgressTask();
                switch (position) {
                    case 1:
                        // Получаем статус Интернет
                        isInternetPresent = cd.ConnectingToInternet();
                        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, citiesBW);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        //Проверяем Интернет статус:
                        if (isInternetPresent) {
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        int cit_id = citiesId.get(spinner2.getAdapter().getItem(position));
                                        updateWeather(cit_id);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        showAlertDialog(getActivity(), "Интернет соединение отсутствует",
                                                "У вас нет Интернет соединения", false);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                        break;

                    case 2:
                        isInternetPresent = cd.ConnectingToInternet();
                        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, citiesFB);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        if (isInternetPresent) {
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        int cit_id = citiesId.get(spinner2.getAdapter().getItem(position));
                                        updateWeather(cit_id);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        showAlertDialog(getActivity(), "Интернет соединение отсутствует",
                                                "У вас нет Интернет соединения", false);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                        break;
                    case 3:
                        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, citiesRP);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        if (isInternetPresent) {
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        int cit_id = citiesId.get(spinner2.getAdapter().getItem(position));
                                        updateWeather(cit_id);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        showAlertDialog(getActivity(), "Интернет соединение отсутствует",
                                                "У вас нет Интернет соединения", false);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                        break;
                    case 4:
                        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, citiesNW);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        if (isInternetPresent) {
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        int cit_id = citiesId.get(spinner2.getAdapter().getItem(position));
                                        updateWeather(cit_id);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        showAlertDialog(getActivity(), "Интернет соединение отсутствует",
                                                "У вас нет Интернет соединения", false);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                        break;
                    case 5:
                        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, citiesFS);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        if (isInternetPresent) {
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        int cit_id = citiesId.get(spinner2.getAdapter().getItem(position));
                                        updateWeather(cit_id);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        showAlertDialog(getActivity(), "Интернет соединение отсутствует",
                                                "У вас нет Интернет соединения", false);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateWeather(int cit_id) {
        Toast.makeText(getActivity(), "Данные загружены", Toast.LENGTH_SHORT)
                .show();
        city = Integer.toString(cit_id);


        // запрос на обновление погоды
            ProgressTask();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //if (contentText == null)
        //  ProgressTask();
    }

    void ProgressTask() {
        new AsyncTask<String, Void, API.ApiResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected API.ApiResponse doInBackground(String... x) {
                ArrayList<String> params = new ArrayList<String>();
                params.add("id");
                //params.add("501183");         //id Ростова-на-Дону
                params.add(city);
                params.add("APPID");
                params.add("0408201c1a78c86fd9433b7efbc51b4f");
                params.add("lang");
                params.add("ru");
                params.add("units");
                params.add("metric");
                //params.add("cnt");
                //params.add("16");
                return API.execute(API.ApiMethod.GET_WEATHER.format(), API.HttpMethod.GET, params.toArray(new String[params.size()]));
            }

            protected void onPostExecute(API.ApiResponse apiResponse) {
                super.onPostExecute(apiResponse);
                try {
                    if (apiResponse.isSuccess()) {
                        String[] s;
                        s = hangleProcessTask(apiResponse);
                        contentText = s[1];
                        contentAdd = s[0] + "\n" + s[3] + "\nВлажность: " + s[2] + "%\nВетер: " + s[4] + " м/с";
                        jsonhandler = apiResponse.getJson().toString();
                        getWeekCharacteristics(apiResponse, s[0]);
                        contentView.setText(contentText + " °C");
                        contentTAdd.setText(contentAdd);
                        //Toast.makeText(getActivity(), "Данные загружены", Toast.LENGTH_SHORT)
                        //        .show();
                    }
                } catch (Exception e) {
                    Log.e("Weather", "ALERT! ALERT! Exception!", e);
                } finally {

                }
            }
        }.execute();
    }

    private void getWeekCharacteristics(API.ApiResponse apiResponse, String city) throws JSONException {
        JSONObject[] a = new JSONObject[4];
        a[0] = apiResponse.getJson().getJSONArray("list").optJSONObject(8); //6 утра завтра
        a[1] = apiResponse.getJson().getJSONArray("list").optJSONObject(16);
        a[2] = apiResponse.getJson().getJSONArray("list").optJSONObject(24);
        a[3] = apiResponse.getJson().getJSONArray("list").optJSONObject(32);

        w = new Week();
        for (int i = 0; i < 3; i++) {
            w.date = a[i].getString("dt_txt").toString();
            String[] dt = w.date.split(" ");
            w.temp_min = a[i].getJSONObject("main").getString("temp_min");
            w.temp_max = a[i].getJSONObject("main").getString("temp_max");
            w.description = a[i].getJSONArray("weather").optJSONObject(0).getString("description");
            t[i].setText(dt[0] + "\n" + dt[1] + "\nMax t: " + w.temp_max + " °C\nMin t: " + w.temp_min + " °C\n" + w.description);
            changePic(imgArr[i], w.description, 12);
        }
    }

    private String[] hangleProcessTask(API.ApiResponse apiResponse) throws JSONException {
        String[] answer = new String[10];
        String cityName = apiResponse.getJson().getJSONObject("city").getString("name");
        JSONObject a = apiResponse.getJson().getJSONArray("list").optJSONObject(0);

        String other = a.getJSONObject("main").getString("temp");
        String humidity = a.getJSONObject("main").getString("humidity");
        String description = a.getJSONArray("weather").optJSONObject(0).getString("description");
        String pressure = a.getJSONObject("main").getString("pressure");
        String wind = a.getJSONObject("wind").getString("speed");
        //String snow=a.getJSONObject("snow").getString("3h");
        String clouds = a.getJSONObject("clouds").getString("all");
        changePic(img, description, Integer.parseInt(a.getString("dt_txt").substring(11, 13)));

        answer[0] = cityName;
        answer[1] = other;
        answer[2] = humidity;
        answer[3] = description;
        answer[4] = wind;
        //answer[5]=snow;
        answer[5] = clouds;
        answer[6] = pressure;
        return answer;
    }

    private void changePic(ImageView img, String description, int hour) {
        switch (description) {
            case "ясно":
                if (hour < 18 && hour > 6)
                    img.setImageResource(R.drawable.s2);
                else
                    img.setImageResource(R.drawable.s3);
                break;
            case "пасмурно":
                img.setImageResource(R.drawable.s6);
                break;
            case "легкий дождь":
                img.setImageResource(R.drawable.s8);
                break;
            case "дождь":
                img.setImageResource(R.drawable.s7);
                break;
            case "небольшой снегопад":
            case "снег":
                img.setImageResource(R.drawable.s1);
                break;
            case "облачно":
                if (hour < 18 && hour > 6)
                    img.setImageResource(R.drawable.s4);
                else
                    img.setImageResource(R.drawable.s5);
                break;
        }
    }

   public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        //Настраиваем название Alert Dialog:
        alertDialog.setTitle(title);

        //Настраиваем сообщение:
        alertDialog.setMessage(message);

        //Настраиваем кнопку OK
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //Отображаем сообщение диалога:
        alertDialog.show();
    }
}
