package com.rulin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.exception.BusinessException;
import com.rulin.vo.ReplyCountVO;
import com.rulin.vo.ReplyVO;
import com.rulin.common.ResponseResult;
import com.rulin.common.FieldConstants;
import com.rulin.vo.SystemCommentVO;
import com.rulin.entity.Comment;
import com.rulin.entity.UserAuth;
import com.rulin.util.PageUtils;
import com.rulin.dto.CommentDTO;
import com.rulin.mapper.CommentMapper;
import com.rulin.mapper.UserAuthMapper;
import com.rulin.service.CommentService;
import com.rulin.util.DateUtils;
import com.rulin.util.HTMLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final UserAuthMapper userAuthMapper;

    /**
     * 评论列表
     *
     * @param keywords
     * @return
     */
    @Override
    public ResponseResult listComment(String keywords) {
        Page<SystemCommentVO> dtoPage = baseMapper.selectPageList(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), keywords);
        return ResponseResult.success(dtoPage);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseResult deleteBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }


    //-----------------------web端方法开始-------------
    @Override
    public ResponseResult comments(Long articleId) {
        // 查询文章评论量
        Integer commentCount = baseMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.nonNull(articleId), Comment::getArticleId, articleId)
                .isNull(Objects.isNull(articleId), Comment::getArticleId)
                .isNull(Comment::getParentId));
        if (commentCount == 0) {
            return ResponseResult.success();
        }
        Page<Comment> pages = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),
                new QueryWrapper<Comment>().eq(FieldConstants.ARTICLE_ID, articleId).isNull(FieldConstants.PARENT_ID)
                        .orderByDesc(FieldConstants.ID));
        // 分页查询评论集合
        List<Comment> comments = pages.getRecords();
        if (CollectionUtils.isEmpty(comments)) {
            return ResponseResult.success();
        }
        List<com.rulin.vo.CommentVO> commentVOList = new ArrayList<>();
        List<ReplyVO> replyVOList;
        for (Comment comment : comments) {
            UserAuth userAuth = userAuthMapper.getByUserId(comment.getUserId());
            // 根据评论id集合查询回复数据
            replyVOList = baseMapper.listReplies(comment.getId());
            ReplyCountVO replyCountVO = baseMapper.listReplyCountByCommentId(comment.getId());
            com.rulin.vo.CommentVO dto = new com.rulin.vo.CommentVO();
            dto.setId(comment.getId());
            dto.setUserId(comment.getUserId());
            dto.setCommentContent(comment.getContent());
            dto.setCreateTime(comment.getCreateTime());
            dto.setAvatar(userAuth.getAvatar());
            dto.setNickname(userAuth.getNickname());
            dto.setReplyVOList(replyVOList);
            dto.setReplyCount(replyCountVO == null ? 0 : replyCountVO.getReplyCount());
            commentVOList.add(dto);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("commentCount", commentCount);
        map.put("commentDTOList", commentVOList);
        return ResponseResult.success(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addComment(CommentDTO commentDTO) {
        if (commentDTO.getUserId() != null) {
            throw new BusinessException("非法请求评论!");
        }
        // 过滤标签
        commentDTO.setCommentContent(HTMLUtils.deleteTag(commentDTO.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(StpUtil.getLoginIdAsLong())
                .replyUserId(commentDTO.getReplyUserId())
                .articleId(commentDTO.getArticleId())
                .content(commentDTO.getCommentContent())
                .parentId(commentDTO.getParentId()).createTime(DateUtils.getNowDate())
                .build();
        int rows = baseMapper.insert(comment);
       /* // 判断是否开启邮箱通知,通知用户
        if (websiteConfig.getIsEmailNotice().equals(TRUE)) {
            notice(comment);
        }*/
        return rows > 0 ? ResponseResult.success(comment) : ResponseResult.error("评论失败");
    }

    @Override
    public ResponseResult repliesByComId(Integer commentId) {
        Page<Comment> page = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), new QueryWrapper<Comment>().eq(FieldConstants.PARENT_ID, commentId));
        List<ReplyVO> result = new ArrayList<>();
        for (Comment comment : page.getRecords()) {
            UserAuth userAuth = userAuthMapper.getByUserId(comment.getUserId());
            UserAuth replyUser = userAuthMapper.getByUserId(comment.getReplyUserId());
            ReplyVO dto = new ReplyVO();
            dto.setId(comment.getId());
            dto.setAvatar(userAuth.getAvatar());
            dto.setNickname(userAuth.getNickname());
            dto.setContent(comment.getContent());
            dto.setCreateTime(comment.getCreateTime());
            dto.setParentId(commentId);
            dto.setReplyNickname(replyUser.getNickname());
            result.add(dto);
        }
        return ResponseResult.success(result);
    }
}
