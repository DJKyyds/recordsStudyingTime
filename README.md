# 开发记录学习时长的工具

## 需求

1. 个人可以记录自己每日的工作、工作时长、工作打标签（工作进度、目标）
2. 可以根据日历查看自己的工作（视图）
3. 可以对一定周期内的工作进行分析（饼图、折线图、柱状图）

## 技术选型

1、前端：Vue3+ Vite2 +Ant Design Vue (桌面端)

- 数据可视化：AntV G2
- 日期处理库：Day.js

2、后端：SpringBoot + redis分布式登录

### 准备工作

利用vite快速开发,这里我们使用yarn

```
yarn create vite
```

安装需要的组件

Vue Router

```
yarn add vue-router@4
```

下载antdesignvue

```
yarn add ant-design-vue
```

使用Antv

```
yarn add @antv/g2
```

## 前端整合

1. Vite 初始化
2. Vue Router 整合，添加路由功能
3. Ant Design Vue 整合
4. 整合 AntV G2，支持实例图表

## 前端开发

1. 新增记录：表单页
2. 展示记录：列表页
3. 分析

## 后端开发

1. 拉取通用模板，已经整合好了常用框架、用户登录用例等

```
后端模板
https://github.com/DJKyyds/springbootinit
```

2.设计库表

### 数据库设计

#### 工作表

```
name: string;
description: string;
duration: number;
tags: string[];
planTime?: Date;
createTime: Date;
updateTime: Date;
```

```
id bigint

任务名称 varchar

描述 varchar

时长 duration int

tags varchar

planTime 计划时间 datetime

创建时间 datetime

更新时间 datetime

逻辑删除 tinyint 0 / 1
```

#### SQL

```sql
# 任务
create table work
(
    id          bigint auto_increment comment 'id' primary key,
    name        varchar(256)       null comment '任务名称',
    description text               null comment '描述',
    duration    int                null comment '时长（秒）',
    tags        varchar(512)       null comment '标签列表json',
    planTime    datetime comment '计划时间',
    createTime  datetime default CURRENT_TIMESTAMP comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    isDelete    tinyint  default 0 not null comment '是否删除'
) comment '任务';
```

### 接口设计

#### 创建任务

1. 校验参数
2. 插入数据到数据库中

查询任务列表

直接用接口文档在线测试

- 关键点:为了方便修改，我们创建一个PageRequest

  ![](file://C:\Users\a\AppData\Roaming\marktext\images\2022-08-08-16-12-48-image.png?msec=1659946368093)


```java
package com.yupi.project.common;

import lombok.Data;

import java.io.Serializable;


/**
 * 通用分页请求类
 */
@Data
public class PageRequest implements Serializable {


    private static final long serialVersionUID = -7351549463165389434L;
    /**
     * 页面大小
     */
    private long size = 5;

    /**
     * 当前页面
     */
    private long current = 1;
}

```

![](file://C:\Users\a\AppData\Roaming\marktext\images\2022-08-08-16-13-10-image.png?msec=1659946390187)

```java
package com.yupi.project.model.request;

import com.yupi.project.common.PageRequest;
import com.yupi.project.model.entity.Work;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author yupi
 */
@Data
public class WorkRequest implements Serializable{

    private static final long serialVersionUID = 604678205224377007L;

    private Work work;

    private PageRequest pageRequest;
}

```

Controller

```java
package com.yupi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.project.common.BaseResponse;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.common.PageRequest;
import com.yupi.project.common.ResultUtils;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.model.entity.Work;
import com.yupi.project.model.request.WorkRequest;
import com.yupi.project.service.WorkService;
import org.apache.catalina.LifecycleState;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/work")
@CrossOrigin
public class WorkController {

    @Resource
    private WorkService workService;

    @PostMapping("/create")
    public BaseResponse<Long> createWork(@RequestBody Work work) {
        if (work == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isAnyBlank(work.getName(), work.getDescription())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = workService.save(work);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(work.getId());
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateWork(@RequestBody Work work) {
        if (work == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 权限校验
        // 参数校验
        if (work.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = work.getName();
        final int MAX_NAME_LENGTH = 100;
        if (StringUtils.isNotBlank(name) && name.length() > MAX_NAME_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = workService.updateById(work);
        return ResultUtils.success(b);
    }

    /**
     * 简单查询
     */
    @GetMapping("/page")
    public BaseResponse<List<Work>> listWorkAll(Work work){
        QueryWrapper<Work> queryWrapper = new QueryWrapper<>();
        List<Work> listWork = workService.list(queryWrapper);
        return ResultUtils.success(listWork);
    }

    /**
     * 分页查询
     */
    @PostMapping("/list")
    public BaseResponse<Page<Work>> listWork(@RequestBody WorkRequest workRequest) {
        if (workRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Work work = workRequest.getWork();
        PageRequest pageRequest = workRequest.getPageRequest();
        QueryWrapper<Work> queryWrapper = new QueryWrapper<>();
        if (work != null) {
            String name = work.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }
        }
        Page<Work> pageData = workService.page(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return ResultUtils.success(pageData);
    }
}

```

参数校验全在模板里面

## 前后端联调

### 分页实现

1. 前端分页（一次请求全量数据，前端去计算每一页展示什么）
2. 后端分页（一次请求一页的数据，前端页面时重新发请求去加载）

## 源码

```url
https://github.com/DJKyyds/recordsStudyingTime.githttps://github.com/DJKyyds/recordsStudyingTime.git
```