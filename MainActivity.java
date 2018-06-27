package com.jose.saneesh.jsonparsing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

  public static Button btnDisplay;
   public static TextView textView;

    public static ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    public  static List<DataFields>list3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDisplay = findViewById(R.id.btnDisplay);

        swipeRefreshLayout =findViewById(R.id.swipeRefresh);


        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParsingData parsedData = new ParsingData();
                parsedData.execute();
            }
        });


      // addItemLongClick();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        listView = findViewById(R.id.list_view);
       registerForContextMenu(listView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.list_view){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.layout_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.repoUrl:
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
               int position = menuInfo.position;
                DataFields dataFields = list3.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(dataFields.getUrl()));

                startActivity(intent);
                return true;


            case R.id.ownerUrl:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://github.com/square"));
                startActivity(intent1);
                return true;

               default:
                   return super.onContextItemSelected(item);



        }


    }
    @Override
    public void onRefresh() {

    }

    public class ParsingData extends AsyncTask<String,String,String>  {



        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            try {
                url = new URL("https://api.github.com/users/square/repos");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line ;
                StringBuilder data =new StringBuilder();
                while((line = bufferedReader.readLine())!= null){

                    data.append(line);
                }

             return (data.toString());

            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            List<DataFields> dataList = new ArrayList<>();


            try {
                JSONArray jsonArray= new JSONArray(result);

                for(int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    DataFields dataFields = new DataFields();
                    dataFields.name =jsonObject.getString("full_name");
                    dataFields.description = jsonObject.getString("description");
                    dataFields.url = jsonObject.getString("html_url");
                    dataList.add(dataFields);
                }

                list3 = dataList;
                listView = findViewById(R.id.list_view);
                listView.setAdapter(new MyListAdapter(MainActivity.this,dataList));
                swipeRefreshLayout.setRefreshing(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }


    private void addItemLongClick(){
        ListView listView = findViewById(R.id.list_view);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DataFields dataFields = list3.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(dataFields.getUrl()));
                startActivity(intent);
                return false;
            }
        });
    }

    public void refresh(){
        ParsingData parsingData = new ParsingData();
        parsingData.execute();
    }

}

