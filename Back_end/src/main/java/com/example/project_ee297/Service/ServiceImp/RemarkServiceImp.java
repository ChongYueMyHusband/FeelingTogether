package com.example.project_ee297.Service.ServiceImp;

import com.example.project_ee297.DAO.ForumsDAO;
import com.example.project_ee297.DAO.remarkDAO;
import com.example.project_ee297.Object.Post;
import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Object.remark;
import com.example.project_ee297.Service.RemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class RemarkServiceImp implements RemarkService {

    @Autowired
    private remarkDAO remarkDAO;
    @Autowired
    private ForumsDAO forumsDAO;


    @Override
    public Result<remark> RemarkPost(remark remark) {
        Result<remark> result=new Result<>();
        Timestamp time=new Timestamp(System.currentTimeMillis());
        remark.setTimestamp(time);
        System.out.println(remark.getTimestamp());
        remarkDAO.addRemark(remark);
        Post GetPost=forumsDAO.getById(Integer.valueOf(remark.getPost_id()));
        GetPost.setRemark(GetPost.getRemark()+1);
        forumsDAO.remark(GetPost);
        result.setResultSuccess("评论成功");
        return result;
    }

    @Override
    public Result<remark> delete(remark remark) {
        Result<remark> result=new Result<>();
        remarkDAO.deleteRemarkByIdAndUserAccount(remark.getId(),remark.getUser_account());
        return result;
    }

}
