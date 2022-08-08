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
