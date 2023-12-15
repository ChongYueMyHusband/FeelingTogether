package com.example.project_ee297.Service;

import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Object.remark;
import org.springframework.stereotype.Service;

@Service
public interface RemarkService {


     Result<remark> RemarkPost(remark remark);

     Result<remark> delete(remark remark);

}
