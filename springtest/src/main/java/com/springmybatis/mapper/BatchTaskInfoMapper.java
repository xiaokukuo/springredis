package com.springmybatis.mapper;

import com.springmybatis.model.BatchTaskInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchTaskInfoMapper {


    List<BatchTaskInfo> selectAll();
}
