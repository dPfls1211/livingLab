package com.cookandroid.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "172.30.1.28";
    private static String TAG = "phptest";
    private static final String TAG_JSON="root";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "id_Name";
    private static final String TAG_ADDRESS ="password";
    List<String> Array = new ArrayList<String>();
    String id,password,id_Name;
    String mJsonString;
    int mile;
    int [] ccc={0,0,0,0,0,0,0};
    ArrayList<HashMap<String, String>> mArrayList;
    Boolean LoginCheck = false;
    ListView studyListV ;
    int UnivCh = 0;
    Boolean zeroCheck=false;


    private EditText mEditTextName;
    private EditText mEditTextCountry;
    private TextView mTextViewResult;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String formatDate = sdfNow.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button newText =(Button) findViewById(R.id.newText);
    }
    //-------------------------------------------------------------------------회원가입 서버
    class newMember extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id_Name = (String)params[1];
            String password = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "id_Name=" + id_Name + "&password=" + password;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();



                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();

                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    //--------------------------------------------------------------------------서버
    private class loginMember extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);
            if (result == null){
            }
            else {
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];

            String serverURL = "http://172.30.1.28/login.php";
            String postParameters = "id_Name=" + searchKeyword1 + "&password=" + searchKeyword2;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            Log.d("!!","!!");
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String getid = item.getString(TAG_ID);
                String getId_Name = item.getString(TAG_NAME);
                String ggetPassword = item.getString(TAG_ADDRESS);
                id=getid;
                id_Name=getId_Name;
                password=ggetPassword;
                LoginCheck=true;
            }
        } catch (JSONException e) {

            Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 일치하지 않음",Toast.LENGTH_SHORT).show();
        }

    }



    //-----------------------------------------------------------------------로그인 서버

    private class getMile extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);
            if (result == null){
            }
            else {
                mJsonString = result;
                showmile();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];

            String serverURL = "http://172.30.1.28/mile.php";
            String postParameters = "id=" + searchKeyword1;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showmile(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String getid = item.getString(TAG_ID);
                mile = Integer.parseInt(item.getString("mile"));

            }
        } catch (JSONException e) {

        }

    }





    //-------------------------------------------------------마일리지

    class cBoard extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String univch = (String)params[1];
            String subject = (String)params[2];
            String content=(String)params[3];
            String created=(String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "univch=" + univch + "&id_Name=" + id_Name+ "&subject=" + subject +"&content="+content+"&created="+created;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
//-----------------------------------------------------------------------게시판


    private class Getcon extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showcon();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];

            String serverURL = "http://172.30.1.28/getBoard.php";
            String postParameters = "univch=" + searchKeyword1;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showcon(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            Array.clear();
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String sub = item.getString("subject");
                Array.add(sub);
            }
        } catch (JSONException e) {
            Array.clear();
            Array.add("작성된목록이없습니다.");
            Log.d(TAG, "showResult : ", e);
        }

    }

    //----------------------------------------------------------------글 목록 띄우기
    public void onClick(View v){
        final LinearLayout BasicLayout = (LinearLayout)findViewById(R.id.basicLayout);
        LinearLayout MainLayout = (LinearLayout)findViewById(R.id.main);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView titlesName =(TextView)findViewById(R.id.titleName);
        TextView nameContext =(TextView)findViewById(R.id.nameContext);
        TextView times = (TextView)findViewById(R.id.times);
        final ArrayList<String> studyLists =new ArrayList<>();
        switch (v.getId()){
            case R.id.kangnamBus:
                BasicLayout.removeAllViews();
                inflater.inflate(R.layout.dalgumi,BasicLayout, true);
                break;
            case R.id.sky: //설정창.. 할게 없는뎀
                BasicLayout.removeAllViews();
                inflater.inflate(R.layout.setting,BasicLayout,true);
                break;
            case R.id.obj:  //마이페이지창 버튼클릭
                BasicLayout.removeAllViews();
                if(LoginCheck==true) {   // 로그인 되어있다면 나올 레이아웃
                    inflater.inflate(R.layout.mypage, BasicLayout, true);
                    TextView nickname = (TextView) findViewById(R.id.nickName); // 닉네임 변경 추가 귀찮으면 익명. 으로 라도 해두기!
                }
                else{  //로그인이 안되어있으면 로그인창 나옴.
                    final Dialog loginView = new Dialog(MainActivity.this);
                    loginView.setContentView(R.layout.login);

                    final Button sign =loginView.findViewById(R.id.sign);
                    Button loginCh = loginView.findViewById(R.id.loginCheck);
                    Button loginCan =loginView.findViewById(R.id.loginCancle);

                    final EditText loginID = loginView.findViewById(R.id.loginID);     //로그인 아이디 텍스트창
                    final EditText loginPassword = loginView.findViewById(R.id.loginPassword);     //로그인 패스워드 텍스트창
                    Display display = getWindowManager().getDefaultDisplay();
                    final Point size = new Point();
                    display.getSize(size);

                    sign.setOnClickListener(new View.OnClickListener() {      //회원가입 창
                        @Override
                        public void onClick(View v) {  //회원가입 버튼클릭
                            final Dialog signView = new Dialog(MainActivity.this);
                            signView.setContentView(R.layout.sign);

                            Button signCh = signView.findViewById(R.id.signCheck);
                            Button signCan =signView.findViewById(R.id.signCancle);

                            final EditText signID = signView.findViewById(R.id.signID);  //회원가입 아이디 텍스트창
                            final EditText signPassword = signView.findViewById(R.id.signPassword);                                                 //회원가입 패스워드 텍스트창

                            Window window = signView.getWindow();
                            int x = (int)(size.x *0.99f);
                            int y = (int)(size.y*0.5f);

                            window.setLayout(x,y);

                            signCh.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String id_Name = signID.getText().toString();
                                    String password = signPassword.getText().toString();
                                    newMember task = new newMember();
                                    task.execute("http://" + IP_ADDRESS + "/member.php", id_Name,password);
                                    signView.dismiss();
                                }
                            });
                            signCan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    signView.dismiss();
                                }
                            });
                            signView.show();
                        }
                    });
                    loginCh.setOnClickListener(new View.OnClickListener() {    // 로그인 확인 버튼로그인 성공시inflater.inflate(R.layout.mypage,BasicLayout,true); 실패시 토스트 부탁해요
                        @Override
                        public void onClick(View v) {

                            mArrayList.clear();

                            loginMember task = new loginMember();
                            task.execute(loginID.getText().toString(),loginPassword.getText().toString());
                            //getMile ma = new getMile();
                            //task.execute(id);
                            //뷰 없애는거라 로그인 성공시
                            if(LoginCheck) {
                                loginView.dismiss();
                                inflater.inflate(R.layout.mypage, BasicLayout, true);  // 로 레이아웃생성
                                TextView nickname = (TextView) findViewById(R.id.nickName);
                                nickname.setText(nickname.getText().toString()+" "+id_Name);// 닉네임 변경 추가 귀찮으면 익명. 으로 라도 해두기!
                                // 로그인 실패시 토스트..
                            }
                        }
                    });
                    mArrayList = new ArrayList<>();
                    loginCan.setOnClickListener(new View.OnClickListener() {    //취소 버튼
                        @Override
                        public void onClick(View v) {
                            loginView.dismiss();
                        }
                    });
                    loginView.show();

                    Window window = loginView.getWindow();
                    int x = (int)(size.x *0.99f);
                    int y = (int)(size.y*0.5f);

                    window.setLayout(x,y);
                }
                break;
            case R.id.myWrite: //작성 목록 보여주기
                BasicLayout.removeAllViews();
                inflater.inflate(R.layout.mypage_write,BasicLayout,true);
                String[] writeListName = {"작성목록이 없습니다."} ; //각 작성 목록에 맞는 배열 추가
                ListView writeList = findViewById(R.id.writeList);
                ArrayAdapter<String> writeadapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,writeListName);
                writeList.setAdapter(writeadapter);
                break;
            case R.id.myComent: //댓글
                BasicLayout.removeAllViews();
                inflater.inflate(R.layout.mypage_coment,BasicLayout,true);
                String[] comentListName = {"작성목록이 없습니다."} ; //각 작성 목록에 맞는 배열 추가
                ListView comentList = findViewById(R.id.comentList);
                ArrayAdapter<String> comnetadapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,comentListName);
                comentList.setAdapter(comnetadapter);
                break;
            case R.id.myReservation: //예약 목록  리스트 너무 많아서 레이아웃 자체에서도 생략함. 혹시 확인 자체 귀찮으면 VISible gone 하길 추천
                BasicLayout.removeAllViews();
                inflater.inflate(R.layout.mypage_reservation,BasicLayout,true);
                String[] ReserListName1 = {"작성목록이 없습니다."} ; //각 작성 목록에 맞는 배열 추가
                ListView reserList1 = findViewById(R.id.writenServiceList);
                ArrayAdapter<String> reserAdapter1 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,ReserListName1);
                reserList1.setAdapter(reserAdapter1);
                break;
            case R.id.logout:
                BasicLayout.removeAllViews();
                LoginCheck=false;
                break;

                //pen교육게시판dmd.. rnlcksk ggggg
            case R.id.pen:
                BasicLayout.removeAllViews();
                inflater.inflate(R.layout.studies,BasicLayout,true);
                break;
            case R.id.studyUniv:
                UnivCh=1;
                Getcon con = new Getcon();
                con.execute(Integer.toString(UnivCh));
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.studies_list,MainLayout,true);
                 ArrayList<String> studylist = new ArrayList<>(); // 리스트 추가 해야함. 사범대
                ListView sList = findViewById(R.id.studyList);
                final ArrayAdapter<String> studyAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,studylist);
                sList.setAdapter(studyAdapter);
                studylist.clear();
                if(Array.size()==0)
                    studylist.add("작성된목록이없습니다.");
                else {
                    for (int i = 0; i < Array.size(); i++) {
                        studylist.add(Array.get(i));
                    }
                }
               //서버통해서목록추가할것.지금 ArrayList인데 바꿀려면 바꿔.. 근데배열 추가생각해서 난 저게 최선이엇어..
                studyAdapter.notifyDataSetChanged();
                break;
            case R.id.welfareUniv:
                UnivCh=2;
                Getcon cons = new Getcon();
                cons.execute(Integer.toString(UnivCh));
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.studies_list,MainLayout,true);
                 ArrayList<String> wellist = new ArrayList<>();  // 리스트 추가 해야함. 복지융합대학
                ListView wList = findViewById(R.id.studyList);
                final ArrayAdapter<String> welAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,wellist);
                wList.setAdapter(welAdapter);
                wellist.clear();
                if(Array.size()==0)
                    wellist.add("작성된목록이없습니다.");
                else {
                    for (int i = 0; i < Array.size(); i++) {
                        wellist.add(Array.get(i));
                    }
                }
                welAdapter.notifyDataSetChanged();

                break;
            case R.id.globalUniv:
                UnivCh=3;
                Getcon cona = new Getcon();
                cona.execute(Integer.toString(UnivCh));
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.studies_list,MainLayout,true);
                 ArrayList<String> globallist = new ArrayList<>(); // 리스트 추가 해야함. 글로벌인재대
                ListView gList = findViewById(R.id.studyList);
                final ArrayAdapter<String> globalAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,globallist);
                gList.setAdapter(globalAdapter);
                globallist.clear();
                if(Array.size()==0)
                    globallist.add("작성된목록이없습니다.");
                else {
                    for (int i = 0; i < Array.size(); i++) {
                        globallist.add(Array.get(i));
                    }
                }
                globalAdapter.notifyDataSetChanged();

                break;
            case R.id.manageUniv:
                UnivCh=4;
                Getcon conb = new Getcon();
                conb.execute(Integer.toString(UnivCh));
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.studies_list,MainLayout,true);
                 ArrayList<String> managelist = new ArrayList<>(); // 리스트 추가 해야함. 경영대
                ListView mList = findViewById(R.id.studyList);
                final ArrayAdapter<String> manageAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,managelist);
                mList.setAdapter(manageAdapter);
                managelist.clear();
                if(Array.size()==0)
                    managelist.add("작성된목록이없습니다.");
                else {
                    for (int i = 0; i < Array.size(); i++) {
                        managelist.add(Array.get(i));
                    }
                }
                manageAdapter.notifyDataSetChanged();
                break;
            case R.id.ictUniv:
                UnivCh=5;
                Getcon conc = new Getcon();
                conc.execute(Integer.toString(UnivCh));
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.studies_list,MainLayout,true);
                 ArrayList<String> ictlist = new ArrayList<>(); // 리스트 추가 해야함. ICT건설복지융합대
                ListView iList = findViewById(R.id.studyList);
                final ArrayAdapter<String> ictAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,ictlist);
                iList.setAdapter(ictAdapter);
                ictlist.clear();
                if(Array.size()==0)
                    ictlist.add("작성된목록이없습니다.");
                else {
                    for (int i = 0; i < Array.size(); i++) {
                        ictlist.add(Array.get(i));
                    }
                }
                ictAdapter.notifyDataSetChanged();

                break;
            case R.id.cultureUniv:
                UnivCh=6;
                Getcon cond = new Getcon();
                cond.execute(Integer.toString(UnivCh));
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.studies_list,MainLayout,true);
                 ArrayList<String> cullist = new ArrayList<>(); // 리스트 추가 해야함.
                ListView cList = findViewById(R.id.studyList);
                final ArrayAdapter<String> culAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,cullist);
                cList.setAdapter(culAdapter);
                cullist.clear();
                if(Array.size()==0)
                    cullist.add("작성된목록이없습니다.");
                else {
                    for (int i = 0; i < Array.size(); i++) {
                        cullist.add(Array.get(i));
                    }
                }
                culAdapter.notifyDataSetChanged();

                break;
            case R.id.toeUniv:
                UnivCh=7;
                Getcon cone = new Getcon();
                cone.execute(Integer.toString(UnivCh));
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.studies_list,MainLayout,true);
                 ArrayList<String> toeiclist = new ArrayList<>(); // 리스트 추가 해야함.
                ListView tList = findViewById(R.id.studyList);
                final ArrayAdapter<String> toeicAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,toeiclist);
                tList.setAdapter(toeicAdapter);
                toeiclist.clear();
                if(Array.size()==0)
                    toeiclist.add("작성된목록이없습니다.");
                else {
                    for (int i = 0; i < Array.size(); i++) {
                        toeiclist.add(Array.get(i));
                    }
                }
                toeicAdapter.notifyDataSetChanged();

                break;
            case R.id.studyBack:  //뒤로가기 버튼ㅎㅎ
            case R.id.X:
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.activity_main,MainLayout,true);
                break;
            case R.id.backContext:
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.list_context,MainLayout,true);
                break;
           case R.id.newText:  //새글 추가
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.list_context,MainLayout,true);
                inflater.inflate(R.layout.studies,BasicLayout,true);
                Button uploadStudy =(Button) findViewById(R.id.upload);
                final EditText contextTitle =(EditText) findViewById(R.id.contextTitle);  // 입력받는 제목
                final EditText context = (EditText)findViewById(R.id.context);  //입력받는 내용
                final LinearLayout news= MainLayout;
                final LayoutInflater in=inflater;
                final LinearLayout ba = BasicLayout;
                uploadStudy.setOnClickListener(new View.OnClickListener() {  //업로드시. 내가 쓴 내용 보여줌.. 삭제 기능 넣지말자 ㅎ
                    @Override
                    public void onClick(View v) {
                        news.removeAllViews();
                        in.inflate(R.layout.studies_list,news,true);
                        Intent intent = new Intent(getApplicationContext(),contextActivity.class);
                        // 현재시간을 msec 으로 구한다.
                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        // nowDate 변수에 값을 저장한다.
                        String formatDate = sdfNow.format(date);
                        Toast.makeText(getApplicationContext(),formatDate,Toast.LENGTH_SHORT).show();
                        cBoard task = new cBoard();
                        task.execute("http://" + IP_ADDRESS + "/board.php",Integer.toString(UnivCh),contextTitle.getText().toString(),context.getText().toString(),formatDate);

                        intent.putExtra("time",formatDate); //업로드시 현재 시간임.
                        intent.putExtra("title",contextTitle.getText().toString());
                        intent.putExtra("contexts",context.getText().toString());
                        startActivity(intent);
                        Log.d("!!","!!!!");//내가 쓴 글 보여주는 인텐트. 저기가서댓글..하면되는데 댓글추가하는기능안함..ㅎㅎ 읽으면 갠톡 ㄱㄱ
                        //후에 리스트 클릭시 이거 위에 인텐트 사용하면됨.. putEXtra만 바꾸면됨..
                        if(UnivCh==1){ //리스트에 추가해야햄.. 서버에 넣고 위에 리스트 추가하는 곳에 그냥 쭈루룩 넣으면 될거같음...
                                        // 나눈 이유는 서버 넣는곳 다르니까.. 구분지어봤어
                            //사범대
                        }
                        if(UnivCh==2){
                            //복지융합대
                        }
                        if(UnivCh==3){
                            //글로벌
                        }
                        if(UnivCh==4){
                            //경영
                        }
                        if(UnivCh==5){
                            //ict
                        }
                        if(UnivCh==6){
                            //교양
                        }
                        if(UnivCh==7){
                            //토익
                        }
                    }
                });
                break;

            case R.id.help:
                BasicLayout.removeAllViews();
                inflater.inflate(R.layout.helperlist,BasicLayout,true);
                break;
            case R.id.shorthandService:
                Intent intent1 =new Intent(getApplicationContext(),helperActivity.class); //helperActivity들어가서 예약정보 서버에 저장.
                intent1.putExtra("first",1);
                startActivity(intent1);
                break;
            case R.id.writingService:
                Intent intent2 =new Intent(getApplicationContext(),helperActivity.class);
                intent2.putExtra("first",2);
                startActivity(intent2);
                break;
            case R.id.movingService:
                Intent intent3 =new Intent(getApplicationContext(),helperActivity.class);
                intent3.putExtra("first",3);
                startActivity(intent3);
                break;



            case R.id.smlies:
                BasicLayout.removeAllViews();
                inflater.inflate(R.layout.question,BasicLayout,true);
                final ArrayList<String> facilitylist = new ArrayList<>(); // 리스트 추가 해야함. 학교시설
                ListView fList = findViewById(R.id.facilityList);
                final ArrayAdapter<String> facilityAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,facilitylist);
                fList.setAdapter(facilityAdapter);
                facilitylist.add("작성된목록이 없습니다.");//목록추가할것.
                facilityAdapter.notifyDataSetChanged();
                final ArrayList<String> supportlist = new ArrayList<>(); // 리스트 추가 해야함. 지원서비스
                ListView supportList = findViewById(R.id.supportList);
                final ArrayAdapter<String> supportAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,supportlist);
                supportList.setAdapter(supportAdapter);
                supportlist.add("작성된목록이 없습니다.");//목록추가할것.
                supportAdapter.notifyDataSetChanged();
                break;
            case R.id.facilityQuestion:
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.list_context,MainLayout,true);
                Button uploadStudyf =(Button) findViewById(R.id.upload);
                final EditText contextTitlef =(EditText) findViewById(R.id.contextTitle);  // 입력받는 제목
                final EditText contextf = (EditText)findViewById(R.id.context);  //입력받는 내용
                final LinearLayout newsf= MainLayout;
                final LayoutInflater inf=inflater;
                uploadStudyf.setOnClickListener(new View.OnClickListener() {  //업로드시. 내가 쓴 내용 보여줌.. 삭제 기능 넣지말자 ㅎ
                    @Override
                    public void onClick(View v) {
                        newsf.removeAllViews();
                        inf.inflate(R.layout.activity_main, newsf, true);
                        Intent intent = new Intent(getApplicationContext(), contextActivity.class);
                        // 현재시간을 msec 으로 구한다.
                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        // nowDate 변수에 값을 저장한다.
                        String formatDate = sdfNow.format(date);

                        intent.putExtra("time", formatDate); //업로드시 현재 시간임.
                        intent.putExtra("title", contextTitlef.getText().toString());
                        intent.putExtra("contexts", contextf.getText().toString());
                        startActivity(intent);
                    }
                });
                inflater.inflate(R.layout.question,BasicLayout,true);
                //위랑마찬가지로리스트에 추가해야햄.. 서버에 넣고 위에 리스트 추가하는 곳에 그냥 쭈루룩 넣으면 될거같음...

                break;
            case R.id.supportQuestion:
                MainLayout.removeAllViews();
                inflater.inflate(R.layout.list_context,MainLayout,true);
                Button uploadStudysq =(Button) findViewById(R.id.upload);
                final EditText contextTitlesq =(EditText) findViewById(R.id.contextTitle);  // 입력받는 제목
                final EditText contextsq = (EditText)findViewById(R.id.context);  //입력받는 내용
                final LinearLayout newssq= MainLayout;
                final LayoutInflater insq=inflater;
                uploadStudysq.setOnClickListener(new View.OnClickListener() {  //업로드시. 내가 쓴 내용 보여줌.. 삭제 기능 넣지말자 ㅎ
                    @Override
                    public void onClick(View v) {
                        newssq.removeAllViews();
                        insq.inflate(R.layout.activity_main, newssq, true);
                        Intent intent = new Intent(getApplicationContext(), contextActivity.class);
                        // 현재시간을 msec 으로 구한다.
                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        // nowDate 변수에 값을 저장한다.
                        String formatDate = sdfNow.format(date);

                        intent.putExtra("time", formatDate); //업로드시 현재 시간임.
                        intent.putExtra("title", contextTitlesq.getText().toString());
                        intent.putExtra("contexts", contextsq.getText().toString());
                        startActivity(intent);

                        //위랑마찬가지로리스트에 추가해야햄.. 서버에 넣고 위에 리스트 추가하는 곳에 그냥 쭈루룩 넣으면 될거같음...
                    }
                });
                break;


            case R.id.explane:
                BasicLayout.removeAllViews();
                inflater.inflate(R.layout.explanation,BasicLayout,true);
                break;
            case R.id.mileage:
                Toast.makeText(MainActivity.this,"마일리지 : "+ mile+"p",Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
