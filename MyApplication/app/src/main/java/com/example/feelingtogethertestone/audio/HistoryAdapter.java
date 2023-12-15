package com.example.feelingtogethertestone.audio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.feelingtogethertestone.R;
import com.example.feelingtogethertestone.databinding.ItemAudioBinding;

import java.util.List;

public class HistoryAdapter extends BaseAdapter{
    private Context context;
    private List<AudioBean> mDates;
    public interface OnItemPlayClickListener{
        void onItemPlayClick(HistoryAdapter adapter, View converView, View playview, int position);

    }
    private OnItemPlayClickListener onItemPlayClickListener;
    public void setOnItemPlayClickListener(OnItemPlayClickListener onItemPlayClickListener){
        this.onItemPlayClickListener = onItemPlayClickListener;
    }
    public interface OnItemDeClickListener{
        void onItemDeClick(HistoryAdapter adapter, View converView, View playview, int position);
    }
    private OnItemDeClickListener onItemDeClickListener;
    public void setOnItemDeClickListener(OnItemDeClickListener onItemDeClickListener){
        this.onItemDeClickListener = onItemDeClickListener;
    }
    public interface OnItemReClickListener{
        void onItemReClick(HistoryAdapter adapter, View converView, View playview, int position);
    }
    private OnItemReClickListener onItemReClickListener;
    public void setOnItemReClickListener(OnItemReClickListener onItemReClickListener){
        this.onItemReClickListener = onItemReClickListener;
    }
    public HistoryAdapter(Context context, List<AudioBean> mDates){
        this.context = context;
        this.mDates = mDates;
    }
    @Override
    public int getCount() {
        return mDates.size();
    }

    @Override
    public Object getItem(int i) {
        return mDates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_audio, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
       AudioBean audioBean =  mDates.get(i);
        holder.ab.tvTime.setText(audioBean.getTime());
        holder.ab.tvDuration.setText(audioBean.getDuration());
        holder.ab.tvTitle.setText(audioBean.getTitle());
        if(audioBean.isPlaying()){
            holder.ab.lyControl.setVisibility(View.VISIBLE);
            holder.ab.pb.setMax(100);
            holder.ab.pb.setProgress(audioBean.getPro());
            holder.ab.ivPlay.setText("暂停");
        }else{
            holder.ab.ivPlay.setText("播放");
            holder.ab.lyControl.setVisibility(View.GONE);
        }
        View itemView = view;
        holder.ab.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemPlayClickListener != null){
                    onItemPlayClickListener.onItemPlayClick(HistoryAdapter.this, itemView, view, i);
                }
            }
        });
        holder.ab.ivDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemDeClickListener != null){
                    onItemDeClickListener.onItemDeClick(HistoryAdapter.this, itemView, view, i);
                }
            }
        });
        holder.ab.ivRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(onItemReClickListener != null){
                        onItemReClickListener.onItemReClick(HistoryAdapter.this, itemView, view, i);
                    }
            }
        });
        return view;
    }
    class ViewHolder{
        ItemAudioBinding ab;
        public ViewHolder(View v){
            ab = ItemAudioBinding.bind(v);
        }
    }
}
