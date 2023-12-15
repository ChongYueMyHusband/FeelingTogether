package com.example.feelingtogethertestone.audio;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import com.example.feelingtogethertestone.R;
import com.example.feelingtogethertestone.databinding.DialogRenameBinding;

public class RenameDia extends Dialog implements View.OnClickListener{
    private DialogRenameBinding binding;
    public interface OnEnsureListener{
        public void onEnsure(String msg);
    }
    private OnEnsureListener onEnsureListener;
    public void setOnEnsureListener(OnEnsureListener onEnsureListener){
        this.onEnsureListener = onEnsureListener;
    }
    public RenameDia(@NonNull Context context){
        super(context);
    }
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DialogRenameBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());
         binding.dialogRenameBtnCancel.setOnClickListener(this);
         binding.dialogRenameBtnEnsure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
       if(view.getId() == R.id.dialog_rename_btn_cancel){
           cancel();
       }else if(view.getId() == R.id.dialog_rename_btn_ensure){
           if(onEnsureListener != null){
             String msg = binding.dialogRenameEt.getText().toString().trim();
             onEnsureListener.onEnsure(msg);
           }
           cancel();
       }
    }
    public void setTipText(String oldText){
        binding.dialogRenameEt.setText(oldText);
    }
    public void setDialogWidth(){
    Window window = getWindow();
    WindowManager.LayoutParams wlp = window.getAttributes();
    Display display = window.getWindowManager().getDefaultDisplay();
    wlp.width = display.getWidth();
    wlp.gravity = Gravity.BOTTOM;
    window.setBackgroundDrawableResource(android.R.color.transparent);
    window.setAttributes(wlp);
    handler.sendEmptyMessageDelayed(1, 100);
}
Handler handler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(@NonNull Message message) {
        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        return false;
    }
});
}
