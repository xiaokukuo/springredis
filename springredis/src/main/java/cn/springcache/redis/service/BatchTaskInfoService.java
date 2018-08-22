package cn.springcache.redis.service;

import cn.springcache.redis.mapper.BatchTaskInfoMapper;
import cn.springcache.redis.model.BatchTaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BatchTaskInfoService {

    @Autowired
    private BatchTaskInfoMapper batchTaskInfoMapper;

    //#root.targetClass.simpleName
    @Cacheable(value="user",key="#root.targetClass.typeName+':'+#root.method.name")
    public List<BatchTaskInfo> findAll(){
        System.err.println("没有走缓存");
       return  batchTaskInfoMapper.selectAll();
    }

}
