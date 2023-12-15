package com.example.fourm;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Http.FourmLikeAsyncTask;
import com.example.feelingtogethertestone.EmotionDiaryActivity;
import com.example.feelingtogethertestone.HomeActivity;
import com.example.feelingtogethertestone.MainActivity;
import com.example.feelingtogethertestone.PersonalInfoActivity;
import com.example.feelingtogethertestone.R;
import com.example.fourm.Comment.CommentActivity;

import java.util.ArrayList;
import java.util.List;

// 用于RecyclerView的适配器 NotebookAdapter，它负责将数据绑定到RecyclerView的视图项中。
// 这个适配器是为RecyclerView提供数据的桥梁，其中 ViewHolder 负责管理视图项的布局和内容。通过接口 OnItemChildClickListener，可以实现对不同视图项的点击事件监听。

public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.ViewHolder> {


    private List<NotebookBean> mNotebookList;
    private OnDeleteButtonClickListener mDeleteButtonClickListener;

    public NotebookAdapter(List<NotebookBean> notebookList) {
        mNotebookList = notebookList;
    }



    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClicked(int position, int notebookId);
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        mDeleteButtonClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notebook, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        NotebookBean item = mNotebookList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mNotebookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements FourmLikeAsyncTask.OnFourmLikeListener {
        private TextView contentTextView;
        public  String msg;
        private int excute;
        private TextView timeTextView;
        private TextView usernameTextView;
        private TextView dataTextView;

        private Button btnComment;
        private Button btnLike;
        private  FourmLikeAsyncTask fourmLikeAsyncTask;
        private  NotebookBean notebookBean1;

        public ViewHolder(View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.tv_content);
            timeTextView = itemView.findViewById(R.id.tv_time);
            dataTextView = itemView.findViewById(R.id.tv_data);

            btnComment = itemView.findViewById(R.id.btn_comment);
            btnLike = itemView.findViewById(R.id.btn_like);
            usernameTextView=itemView.findViewById(R.id.tv_username);
            Log.i("adapter",msg+"a");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TAG", "layout_notebook");
                }
            });



            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    position=mNotebookList.get(position).getPost_id();

                    int number=NotebookBean.getNumber();
                    String positions=(position)+"";
                    Intent intent = new Intent(view.getContext(), CommentActivity.class);
                    intent.putExtra("positions", positions);
                    intent.putExtra("id", position);

                    view.getContext().startActivity(intent);
                    Log.i("TAG", "btnComment" + position);
                }
            });
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                     notebookBean1=FourmmainActivity.getmNotebookList().get(position);
                    initial();
                    position=mNotebookList.get(position).getPost_id();
                     int number=NotebookBean.getNumber();
                     String positions=(position)+"";
                     Log.i("position",positions);
                     String name=FourmmainActivity.getUserName();
                    Log.i("nameAda",MainActivity.getUsername());
                    fourmLikeAsyncTask.execute(MainActivity.getUsername(),positions);


                }
            });
        }

        public void bind(NotebookBean item) {
            contentTextView.setText(item.getContent());
            long time = item.getEditTime();
            timeTextView.setText(Util.getTime(time));
            dataTextView.setText(Util.data(item.getCommentNum(), item.getLikeSpecial()));
            Log.i("temp","number:"+item.getContent());
            usernameTextView.setText(item.getUsername());
        }
        public void initial(){
            fourmLikeAsyncTask=new FourmLikeAsyncTask();
            fourmLikeAsyncTask.setCallback(this);
        }

        @Override
        public void OnFourmLikeListener(String isSuccess) {
            this.msg=isSuccess;
            if(msg.equals("点赞成功")){
                notebookBean1.setLikeSpecial(notebookBean1.getLikeSpecial()+1);
                dataTextView.setText(Util.data(notebookBean1.getCommentNum(), notebookBean1.getLikeSpecial()));
            }
            else if(msg.equals("取消点赞成功")){
                notebookBean1.setLikeSpecial(notebookBean1.getLikeSpecial()-1);
                dataTextView.setText(Util.data(notebookBean1.getCommentNum(), notebookBean1.getLikeSpecial()));
            }
            Log.i("adapter",msg);
        }
    }
}
