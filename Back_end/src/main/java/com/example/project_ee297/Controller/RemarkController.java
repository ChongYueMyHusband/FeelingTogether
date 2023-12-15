package com.example.project_ee297.Controller;

import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Object.remark;
import com.example.project_ee297.Service.RemarkService;
import org.hibernate.metamodel.mapping.MappingModelCreationLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.hibernate.bytecode.BytecodeLogging.LOGGER;


@RestController
public class RemarkController {

    @Autowired
    private RemarkService remarkService;

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public  RemarkController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/Remark")
    public Result<remark> Remark(@RequestBody remark remark ) throws Exception{
        LOGGER.info("收到一条对"+remark.getPost_id()+"的评论");
        Result<remark> result = new Result<>();
        result = remarkService.RemarkPost(remark);
        return  result;

    }

    @PostMapping ("/getRemarkInfo")
    public String getHistory(@RequestBody remark remark) {
        MappingModelCreationLogger.LOGGER.info("发送评论信息");
        String post_id=remark.getPost_id();
        List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT user_account, post_id, content,`timestamp` FROM remark WHERE post_id = ? ORDER BY id DESC", post_id);

        return results.toString();
    }

}

