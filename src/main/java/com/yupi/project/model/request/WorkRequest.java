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
