package com.yupi.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yupi.project.mapper.WorkMapper;
import com.yupi.project.model.entity.Work;
import com.yupi.project.service.WorkService;
import org.springframework.stereotype.Service;

/**
* @author a
* @description 针对表【work(任务)】的数据库操作Service实现
* @createDate 2022-08-08 09:54:24
*/
@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work>
    implements WorkService {

}




