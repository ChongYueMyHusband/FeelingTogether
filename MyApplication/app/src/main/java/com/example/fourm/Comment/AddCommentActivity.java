package com.example.fourm.Comment;

import static com.example.fourm.FourmmainActivity.mNotebookList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.Http.FourmCommentAsyncTask;
import com.example.feelingtogethertestone.R;


import android.content.SharedPreferences;
        import android.os.Bundle;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import com.example.Http.FourmContentAsyncTask;
        import com.example.feelingtogethertestone.MainActivity;
        import com.example.feelingtogethertestone.R;
import com.example.fourm.NotebookBean;

import android.text.TextUtils;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;



public class AddCommentActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int IMAGE_BACK_ID = R.id.image_back;
    public static final int IMAGE_DELETE_ID = R.id.image_delete;
    public static final int BTN_SAVE_ID = R.id.btn_save1;
    private SharedPreferences sp;
    private ImageView imageBack;
    private ImageView imageDelete;
    private EditText editContent;
    private Button btnSave;
    private TextView tvTitle;

    private boolean bIsAdd;

    private DBCommentManager mDBManager;
    private int mNotebookId;
    private CommentBean mNotebookBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        initView();
        mDBManager = new DBCommentManager(this);
        bIsAdd = getIntent().getBooleanExtra("isAdd", true);
        if (bIsAdd) {
            imageDelete.setVisibility(View.GONE);
        } else {
            mNotebookBean = getIntent().getParcelableExtra("notebook");
            imageDelete.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            editContent.setText(mNotebookBean.getContent());
        }
    }

    private void initView() {
        imageBack = findViewById(R.id.image_back);
        imageDelete = findViewById(R.id.image_delete);
        editContent = findViewById(R.id.edit_comment);
        btnSave = findViewById(R.id.btn_save1);
        tvTitle = findViewById(R.id.tv_title_comment);

        imageBack.setOnClickListener(this);
        imageDelete.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        editContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && !bIsAdd){
                    btnSave.setVisibility(View.VISIBLE);
                    imageDelete.setVisibility(View.GONE);
                    tvTitle.setText("编辑记事本");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int temp=view.getId();
        if(temp==IMAGE_BACK_ID){
            finish();
        }
        else if(temp==IMAGE_DELETE_ID ){
            showDeleteDialog();
        }
        else if(temp==BTN_SAVE_ID){
            save(editContent.getText().toString().trim());
        }
    }

    private void save(String content) {
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(AddCommentActivity.this, "您还未输入内容", Toast.LENGTH_SHORT).show();
        } else {
            FourmCommentAsyncTask fourmCommentAsyncTask=new FourmCommentAsyncTask();
            sp = getSharedPreferences("user_info", 0);
            String username = sp.getString("userName", "");
            Log.i("Usernames",username);
            if(username.equals("")){
                username= MainActivity.getUsername();
            }
            CommentBean notebookBean = new CommentBean();
            notebookBean.setContent(content);
            notebookBean.setEditTime(System.currentTimeMillis());
            notebookBean.setUsername("用户名: "+username);
            notebookBean.setLikeSpecial(0);
            fourmCommentAsyncTask.execute(username,CommentActivity.position,content);


            mDBManager.insertNotebook(notebookBean);
            setResult(RESULT_OK);finish();
        }
    }

    private void showDeleteDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_delete_notebook, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDBManager.deleteNotebook(mNotebookBean.getNotebookId());
                setResult(RESULT_OK);
                finish();
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }
}
