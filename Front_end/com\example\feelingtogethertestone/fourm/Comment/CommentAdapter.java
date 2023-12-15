package com.example.fourm.Comment;


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

        import com.example.feelingtogethertestone.R;
import com.example.fourm.Util;

import java.util.ArrayList;
        import java.util.List;

// 用于RecyclerView的适配器 NotebookAdapter，它负责将数据绑定到RecyclerView的视图项中。
// 这个适配器是为RecyclerView提供数据的桥梁，其中 ViewHolder 负责管理视图项的布局和内容。通过接口 OnItemChildClickListener，可以实现对不同视图项的点击事件监听。

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {


    private List<CommentBean> mNotebookList;
    private OnDeleteButtonClickListener mDeleteButtonClickListener;

    public CommentAdapter(List<CommentBean> notebookList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CommentBean item = mNotebookList.get(position);
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

        private  FourmLikeAsyncTask fourmLikeAsyncTask;
        private  CommentBean notebookBean1;

        public ViewHolder(View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.tv_content1);
            timeTextView = itemView.findViewById(R.id.tv_time1);
            dataTextView = itemView.findViewById(R.id.tv_data);

            usernameTextView=itemView.findViewById(R.id.tv_username1);
            Log.i("adapter",msg+"a");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TAG", "layout_notebook");
                }
            });


        }

        public void bind(CommentBean item) {
            contentTextView.setText(item.getContent());
            long time = item.getEditTime();
            timeTextView.setText(TimeUtil.getTime(item.getEditTime()));
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
                dataTextView.setText(TimeUtil.data(0, notebookBean1.getLikeSpecial()));
            }
            else if(msg.equals("取消点赞成功")){
                notebookBean1.setLikeSpecial(notebookBean1.getLikeSpecial()-1);
                dataTextView.setText(TimeUtil.data(0, notebookBean1.getLikeSpecial()));
            }
            Log.i("adapter",msg);
        }
    }
}
