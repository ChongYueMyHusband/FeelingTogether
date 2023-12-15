package com.example.fourm.Comment;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.Http.FourmCommentAsyncTask;
import com.example.Http.FourmGetCommentAsyncTask;
import com.example.feelingtogethertestone.R;
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
import com.example.fourm.NotebookBean;

import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Date;
        import java.util.List;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener, CommentAdapter.OnDeleteButtonClickListener,FourmGetCommentAsyncTask.OnFourmGetCommentListener {

    public static String position;
    public static String id;

    private RecyclerView recyclerView;
    private Button btnAdd;
    public static List<CommentBean> mNotebookList;
    private CommentAdapter mAdapter;
    private  DBCommentManager mDBManager;

    private final String TAG = "ForumMainActivity";
    public static String userName;
    public static String getUserName(){
        return  userName;
    }
    private static final int REQUEST_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBManager = new DBCommentManager(this);
        FourmGetCommentAsyncTask fourmGetCommentAsyncTask=new FourmGetCommentAsyncTask();
        fourmGetCommentAsyncTask.setCallback(this);
        Intent intent = getIntent();
        position = intent.getStringExtra("positions");
        id = intent.getStringExtra("id");

        fourmGetCommentAsyncTask.execute(position);
        SharedPreferences sp = getSharedPreferences("user_info", 0);
        String username = sp.getString("userName", "");
        if(username.equals("")){
            username= MainActivity.getUsername();
        }
        userName=username;
        Log.i("tagMain",username);
        Log.i("tagMain1",MainActivity.getUsername());
        setContentView(R.layout.activity_comment);
        checkPermission();
        initView();
        initRecyclerView();

        getNotebookList();
    }
    public static List<CommentBean> getmNotebookList(){
        return mNotebookList;
    }
    public void addmsg(List<String> userAccountList, List<String> post_id, List<String> contentList,List<String> timestamp){
        for(int i=0;i<userAccountList.size();i++) {
            CommentBean commentBean=new CommentBean();
            commentBean.setContent(contentList.get(i));
            commentBean.setUsername(userAccountList.get(i));
            CommentBean.setNumber(userAccountList.size());
            commentBean.setUsername("用户名: "+userAccountList.get(i));
            String temp=timestamp.get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            try {
                if(!temp.equals("null")){
                    // 将字符串解析为 Date 对象
                    Date date = sdf.parse(temp);
                    // 获取毫秒表示的时间
                    long millis = date.getTime();
                    commentBean.setEditTime(millis);
                }
                else{
                    commentBean.setEditTime(System.currentTimeMillis());
                }
            }  catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Log.i("CommentActivity",contentList.get(i));
            mDBManager.insertNotebook(commentBean);
            setResult(RESULT_OK);
        }


    }

    public void getNotebookList() {
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
        mAdapter = new CommentAdapter(mNotebookList);
        mAdapter.setOnDeleteButtonClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add) {
            Intent intent = new Intent(CommentActivity.this, AddCommentActivity.class);
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
                CommentBean.removeItem(position);
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private void Likes(final int position, final int notebookId){

    }
    private List<String> requestPermissions;
    private  String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public boolean checkPermission() {
        boolean flag = true;
        requestPermissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(CommentActivity.this, permissions[0]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions.add(permissions[0]);
            flag = false;
        }
        if (ContextCompat.checkSelfPermission(CommentActivity.this, permissions[1]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions.add(permissions[1]);
            flag = false;
        }
        return flag;
    }



    @Override
    public void onFourmGetCommentResult(List<String> userAccountList, List<String> post_id, List<String> contentList,List<String> timestamp) {
        addmsg(userAccountList,post_id,contentList,timestamp);
        getNotebookList(); // 更新数据源
        mAdapter.notifyDataSetChanged(); // 通知适配器数据已更新
    }
}
