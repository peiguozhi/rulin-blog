package com.rulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Comment;
import com.rulin.dto.CommentDTO;

import java.util.List;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface CommentService extends IService<Comment> {

    ResponseResult listComment(String keywords);

    ResponseResult deleteBatch(List<Integer> ids);



//    ------web端方法开始------
    ResponseResult comments(Long articleId);

    ResponseResult addComment(CommentDTO comment);

    ResponseResult repliesByComId(Integer commentId);

}
