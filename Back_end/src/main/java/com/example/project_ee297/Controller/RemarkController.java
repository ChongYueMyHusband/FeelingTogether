package com.example.project_ee297.Controller;

import com.example.project_ee297.Object.Post;
import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Object.remark;
import com.example.project_ee297.Service.RemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.hibernate.bytecode.BytecodeLogging.LOGGER;


@RestController
public class RemarkController {
    @Autowired
    private RemarkService remarkService;
    @PostMapping("/Remark")
    public Result<remark> Remark(@RequestBody remark remark ) throws Exception{
        LOGGER.info("收到一条对"+remark.getPost_id()+"的评论");
        Result<remark> result = new Result<>();
        result = remarkService.RemarkPost(remark);
        return  result;

    }
    @PostMapping("getRemarkInfo")
    public List<remark> getAllRemarks(@RequestBody Post  post) {
        return  remarkService.getAllRemark(post);

    }

}

