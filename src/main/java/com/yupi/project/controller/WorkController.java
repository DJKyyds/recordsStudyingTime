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
