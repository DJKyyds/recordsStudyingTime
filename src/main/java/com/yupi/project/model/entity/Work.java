package com.yupi.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务
 * @TableName work
 */
@TableName(value ="work")
@Data
public class Work implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 时长（秒）
     */
    private Integer duration;

    /**
     * 标签列表json
     */
    private String tags;

    /**
     * 计划时间
     */
    private Date planTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}