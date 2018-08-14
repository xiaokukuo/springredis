package cn.springcache.redis.service;

import cn.springcache.redis.mapper.BatchTaskInfoMapper;
import cn.springcache.redis.model.BatchTaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BatchTaskInfoService {

    @Autowired
    private BatchTaskInfoMapper BatchTaskInfoMapper;

    public List<BatchTaskInfo> findAll(){
       return  BatchTaskInfoMapper.selectAll();
    }

}
