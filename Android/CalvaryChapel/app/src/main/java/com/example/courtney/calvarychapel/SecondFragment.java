package com.example.courtney.calvarychapel;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Courtney on 2/4/17.
 * Tutorial for XML Parsing: http://www.androidbegin.com/tutorial/android-xml-parse-images-and-texts-tutorial/
 */

public class SecondFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    TextView monthTxt;
    Spinner spinner1, spinner2;
    Button btnSubmit;
    private ImageView backArrow;
    private ImageView forwardArrow;
    private TextView previous;
    private TextView next;
    private ListView listView;
    private ListViewAdapter adapter;
    private ArrayList<HashMap<String,String>> arrayList;
    static String NAME = "event_name";
    static String DATE = "date";
    static String MONTH = "month";
    static String DAY = "day";
    static String START_TIME = "start_time";
    static String END_TIME = "end_time";
    static String LOCATION = "location";
    static String GROUP_NAME = "group_name";
    static String LEADER_NAME = "leader_name";
    static String LEADER_PHONE = "leader_phone";
    static String LEADER_EMAIL = "leader_email";
    private static String startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private static String endDate;
    private static String baseURL = "https://calvarycorvallis.ccbchurch.com/api.php?srv=public_calendar_listing&date_start=";
    private static String endDateChoice = "&date_end=";
    private static String fullURL;
    private static String chosenMonthName;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout, container, false);

        Calendar calendar = Calendar.getInstance();
        String currentMonthName = new SimpleDateFormat("MMMM").format(calendar.getTime());
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
       // monthTxt = (TextView) myView.findViewById(R.id.month);

     //   monthTxt.setText(chosenMonthName);

        endDate = getLastDayofMonth(currentMonth, currentYear);


        spinner1 = (Spinner) myView.findViewById(R.id.spinner1);
        spinner2 = (Spinner) myView.findViewById(R.id.spinner2);
        btnSubmit = (Button) myView.findViewById(R.id.btnSubmit);

        spinner1.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.month_names, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);

        CustomAddYears();
     //   addListenerOnButton();

    /*    backArrow = (ImageView) myView.findViewById(R.id.arrowBack);
        forwardArrow = (ImageView) myView.findViewById(R.id.arrowForward);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = startDate.substring(5,7);
                String year = startDate.substring(0,4);
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(year), Integer.parseInt(month), 01);
                c.add(Calendar.MONTH, -1);
                int newMonth = c.get(Calendar.MONTH);

                String monthName = String.valueOf(newMonth);
                if (monthName.length() == 1) {
                    monthName = "0" + monthName;
                }

                startDate = year + "-" + monthName + "-" + "01";
           //     endDate = getLastDayofMonth(newMonth);
            //    monthTxt.setText(getMonth(monthName));
                chosenMonthName = getMonth(monthName);

                fullURL = baseURL + startDate + endDateChoice + endDate;

                new DownloadXML().execute();

            }
        });

        forwardArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = startDate.substring(5,7);
                String year = startDate.substring(0,4);
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(year), Integer.parseInt(month), 01);
                c.add(Calendar.MONTH, 1);
                int newMonth = c.get(Calendar.MONTH);

                String monthName = String.valueOf(newMonth);
                if (monthName.length() == 1) {
                    monthName = "0" + monthName;
                }

                startDate = year + "-" + monthName + "-" + "01";
               // endDate = getLastDayofMonth(newMonth, year);
             //   monthTxt.setText(getMonth(monthName));
                chosenMonthName = getMonth(monthName);

                fullURL = baseURL + startDate + endDateChoice + endDate;

                new DownloadXML().execute();


            }
        }); */

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = String.valueOf(spinner2.getSelectedItem());
                int month = (int) spinner1.getSelectedItemId() + 1;
                startDate = year + "-" + String.valueOf(month) + "-01";
                endDate = getLastDayofMonth(month, Integer.parseInt(year));

                String url = baseURL + startDate + endDateChoice + endDate;

                System.out.println(url);

                new DownloadXML().execute(url);
            }
        });


        if (savedInstanceState != null) {
            chosenMonthName = savedInstanceState.getString("MONTH");
            fullURL = savedInstanceState.getString("FULLURL");
        //    monthTxt.setText(chosenMonthName);
        } else {
            if (fullURL != null) {
                // do nothing
            } else {
             //   monthTxt.setText(currentMonthName);
                fullURL = baseURL + startDate + endDateChoice + endDate;
            }
        }

        fullURL = baseURL + startDate + endDateChoice + endDate;


        new DownloadXML().execute(fullURL);

        return myView;
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(parent.getContext(), "OnItemSelected Listener : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void CustomAddYears() {
        spinner2 = (Spinner) myView.findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        list.add(String.valueOf(currentYear));

        for (int i = 0; i < 5; i++) {
            currentYear += 1;
            list.add(String.valueOf(currentYear));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }


    @Override
    public void onSaveInstanceState (final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("FULLURL", fullURL);
        outState.putString("MONTH", chosenMonthName);
    }

    protected class DownloadXML extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... url) {
            Log.v("AsyncTask", "doInBackground");

            arrayList = new ArrayList<HashMap<String, String>>();

            XMLParser parser = new XMLParser();
            String xml = parser.getXmlFromUrl(url[0]);
            Document doc = parser.getDomElement(xml);
            String month;
            String date;
            String day;

            try {
                NodeList nl = doc.getElementsByTagName("item");
                for (int i = 0; i < nl.getLength(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    Element e = (Element) nl.item(i);
                    map.put(NAME, parser.getValue(e, NAME));
                    date = parser.getValue(e, DATE);
                    month = date.substring(5,7);
                    day = date.substring(8,10);
                    map.put(MONTH, getMonth(month));
                    map.put(DAY, day);
                    map.put(START_TIME, parser.getValue(e, START_TIME));
                    map.put(END_TIME, parser.getValue(e, END_TIME));
                    map.put(LOCATION, parser.getValue(e, LOCATION));
                    map.put(GROUP_NAME, parser.getValue(e, GROUP_NAME));
                    map.put(LEADER_NAME, parser.getValue(e, LEADER_NAME));
                    map.put(LEADER_PHONE, parser.getValue(e, LEADER_PHONE));
                    map.put(LEADER_EMAIL, parser.getValue(e, LEADER_EMAIL));
                    arrayList.add(map);
                }

                Log.v("AsyncTask","end of doInBackground");
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            Log.v("AsyncTask","onPostExecute");
            listView = (ListView) myView.findViewById(R.id.listview);
            adapter = new ListViewAdapter(getActivity(), arrayList);
            listView.setAdapter(adapter);
        }

    }


    public static String getLastDayofMonth (int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getMonth (String month_int) {
        String month_value;
        if (month_int.equals("01")) {
            month_value = "January";
        } else if (month_int.equals("02")) {
            month_value = "February";
        } else if (month_int.equals("03")) {
            month_value = "March";
        } else if (month_int.equals("04")) {
            month_value = "April";
        } else if (month_int.equals("05")) {
            month_value = "May";
        } else if (month_int.equals("06")) {
            month_value = "June";
        } else if (month_int.equals("07")) {
            month_value = "July";
        } else if (month_int.equals("08")) {
            month_value = "August";
        } else if (month_int.equals("09")) {
            month_value = "September";
        } else if (month_int.equals("10")) {
            month_value = "October";
        } else if (month_int.equals("11")) {
            month_value = "November";
        } else if (month_int.equals("12") || month_int.equals("00")) {
            month_value = "December";
        } else {
            return null;
        }

        return month_value;
    }

}


