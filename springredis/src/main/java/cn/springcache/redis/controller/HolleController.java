package cn.springcache.redis.controller;

import cn.springcache.redis.service.BatchTaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HolleController {

    @Autowired
    private BatchTaskInfoService batchTaskInfoService;

    @RequestMapping("hello")
    public String hello(){
        System.err.println("hello");
        return "index";

    }

    @RequestMapping("hello/ftl")
    public String hello1(){
        System.err.println("helloftl");
        return "index";

    }

    @RequestMapping("data")
    @ResponseBody
    public Object data(){

        return batchTaskInfoService.findAll();

    }

}
