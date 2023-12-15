package com.example.fourm;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Http.FourmGetAllAsyncTask;
import com.example.feelingtogethertestone.MainActivity;
import com.example.feelingtogethertestone.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FourmmainActivity extends AppCompatActivity implements View.OnClickListener, NotebookAdapter.OnDeleteButtonClickListener,FourmGetAllAsyncTask.OnFourmGetAllListener {

    private RecyclerView recyclerView;
    private Button btnAdd;
    public static List<NotebookBean> mNotebookList;
    private NotebookAdapter mAdapter;
    private  DBManager mDBManager;
    private int isFirstCreation=0;

    private final String TAG = "ForumMainActivity";
    public static String userName;
    public static String getUserName(){
        return  userName;
    }
    private static final int REQUEST_CODE = 1002;
    private String emo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        emo = intent.getStringExtra("emo");
        Log.i(TAG,emo);
        mDBManager = new DBManager(this);
        if (isFirstCreation%2==0) {
            mDBManager = new DBManager(this);
            FourmGetAllAsyncTask fourmGetAllAsyncTask = new FourmGetAllAsyncTask();
            fourmGetAllAsyncTask.setCallback(this);
            fourmGetAllAsyncTask.execute(emo);
            isFirstCreation +=1; // 将标志设置为 false，表示不是第一次创建
        }
        SharedPreferences sp = getSharedPreferences("user_info", 0);
        String username = sp.getString("username", "");
        Log.i("usernamet",username);
        if(username.equals("")){
            username= MainActivity.getUsername();
        }
        userName=username;
//        Log.i("tagMain",username);
//        Log.i("tagMain1",MainActivity.getUsername());
        setContentView(R.layout.activity_fourmmain_acitvity);
        checkPermission();
        initView();
        initRecyclerView();

        getNotebookList();
    }
    public static List<NotebookBean> getmNotebookList(){
        return mNotebookList;
    }
    public void addmsg(List<String>id,List<String> userAccountList,List<String> content,List<String> like,List<String> remark,List<String> created_at){

        for(int i=0;i<userAccountList.size();i++){
            NotebookBean notebookBean = new NotebookBean();
            notebookBean.setContent(content.get(i));
            notebookBean.setLikeSpecial(Integer.parseInt(like.get(i)));
            notebookBean.setCommentNum(Integer.parseInt(remark.get(i)));
            notebookBean.setPost_id(Integer.parseInt(id.get(i)));
            String temp=created_at.get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            try {
                if(!temp.equals("null")){
                // 将字符串解析为 Date 对象
                Date date = sdf.parse(temp);
                // 获取毫秒表示的时间
                long millis = date.getTime();
                notebookBean.setEditTime(millis);
                }
                else{
                    notebookBean.setEditTime(System.currentTimeMillis());
                }
            }  catch (ParseException e) {
                throw new RuntimeException(e);
            }
            NotebookBean.setNumber(userAccountList.size());
            notebookBean.setUsername("用户名: "+userAccountList.get(i));

            mDBManager.insertNotebook(notebookBean);
            setResult(RESULT_OK);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();

        isFirstCreation +=1;
        Log.i("Onresume","res: "+isFirstCreation);
        if (isFirstCreation>2) {

            // 从上一级界面返回时会执行此处的代码
            mDBManager.deleteAllNotebooks();
            FourmGetAllAsyncTask fourmGetAllAsyncTask = new FourmGetAllAsyncTask();
            fourmGetAllAsyncTask.setCallback(this);
            fourmGetAllAsyncTask.execute(emo);
            SharedPreferences sp = getSharedPreferences("user_info", 0);
            String username = sp.getString("userName", "");
            if (username.equals("")) {
                username = MainActivity.getUsername();
            }
            userName = username;


            checkPermission();
            initView();
            initRecyclerView();
            getNotebookList();
        }
    }
    private void getNotebookList() {
        mNotebookList.clear();
        mNotebookList.addAll(mDBManager.selectNotebookList());
        mAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("FourmMainAC","destroy suc");
        // 在销毁 Activity 时调用 mDBManager.deleteAllNotebooks()
        mDBManager.deleteAllNotebooks();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Log.i("rest","res");
            getNotebookList();
        }

    }
    private void initRecyclerView() {
        mNotebookList = new ArrayList<>();
        mAdapter = new NotebookAdapter(mNotebookList);
        mAdapter.setOnDeleteButtonClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add) {
            Intent intent = new Intent(FourmmainActivity.this, AddNotebookActivity.class);
            intent.putExtra("isAdd", true);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onDeleteButtonClicked(int position, int notebookId) {
        showDeleteDialog(position, notebookId);
    }
    private void showDeleteDialog(final int position, final int notebookId){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_delete_notebook, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDBManager.deleteNotebook(notebookId);
                alertDialog.dismiss();
                mNotebookList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mNotebookList.size() - position);

            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private List<String> requestPermissions;
    private  String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public boolean checkPermission() {
        boolean flag = true;
        requestPermissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(FourmmainActivity.this, permissions[0]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions.add(permissions[0]);
            flag = false;
        }
        if (ContextCompat.checkSelfPermission(FourmmainActivity.this, permissions[1]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions.add(permissions[1]);
            flag = false;
        }
        return flag;
    }

    @Override
    public void onFourmGetAllResult(List<String>id,List<String> userAccountList,List<String> content,List<String> like,List<String> remark,List<String> created_at){
        addmsg(id,userAccountList,content,like,remark,created_at);
        getNotebookList(); // 更新数据源
        mAdapter.notifyDataSetChanged(); // 通知适配器数据已更新
    }
}
