package cn.springcache.redis.mapper;

import cn.springcache.redis.model.BatchTaskInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchTaskInfoMapper {


    List<BatchTaskInfo> selectAll();
}
