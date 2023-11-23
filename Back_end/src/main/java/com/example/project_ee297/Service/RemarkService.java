package com.example.project_ee297.Service;

import com.example.project_ee297.Object.Post;
import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Object.remark;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RemarkService {
     List<remark> getAllRemark(Post post);

     Result<remark> RemarkPost(remark remark);

     Result<remark> delete(remark remark);

}
