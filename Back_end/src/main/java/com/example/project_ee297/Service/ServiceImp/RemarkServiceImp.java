package com.example.project_ee297.Service.ServiceImp;

import com.example.project_ee297.DAO.remarkDAO;
import com.example.project_ee297.Object.Post;
import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Object.remark;
import com.example.project_ee297.Service.RemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RemarkServiceImp implements RemarkService {

    @Autowired
    private remarkDAO remarkDAO;


    @Override
    public List<remark> getAllRemark(Post post) {
        return remarkDAO.getAllRemark(post.getId());
    }


    @Override
    public Result<remark> RemarkPost(remark remark) {
        Result<remark> result=new Result<>();
        remarkDAO.addRemark(remark);
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
