package com.rulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rulin.common.ResponseResult;
import com.rulin.common.FieldConstants;
import com.rulin.service.EmailService;
import com.rulin.vo.FriendLinkVO;
import com.rulin.entity.FriendLink;
import com.rulin.mapper.FriendLinkMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.service.FriendLinkService;
import com.rulin.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.rulin.enums.FriendLinkEnum.*;
import static com.rulin.enums.FriendLinkEnum.APPLY;
import static com.rulin.enums.FriendLinkEnum.UP;

/**
 * <p>
 * 友情链接表 服务实现类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    private final EmailService emailService;

    /**
     * 友链列表
     * @param name
     * @param status
     * @return
     */
    @Override
    public ResponseResult listFriendLink(String name, Integer status) {
        QueryWrapper<FriendLink> queryWrapper= new QueryWrapper<FriendLink>()
                .orderByDesc(FieldConstants.SORT).like(StringUtils.isNotBlank(name), FieldConstants.NAME,name)
                .eq(status != null, FieldConstants.STATUS,status);
        Page<FriendLink> friendLinkPage = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),queryWrapper);
        return ResponseResult.success(friendLinkPage);
    }

    /**
     * 添加友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertFriendLink(FriendLink friendLink) {
        baseMapper.insert(friendLink);
        return ResponseResult.success();
    }

    /**
     * 修改友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateFriendLink(FriendLink friendLink) {
        baseMapper.updateById(friendLink);
        //审核通过和未通过发送邮件通知
        emailService.sendFriendEmail(friendLink);
        return ResponseResult.success();
    }

    /**
     * 删除友链
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("删除友链失败");
    }

    /**
     * 置顶友链
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult top(Integer id) {
        Integer sort = baseMapper.getMaxSort();
        baseMapper.top(id,sort+1);
        return ResponseResult.success();
    }

    //    ---------web端方法开始------
    /**
     * 友链列表
     * @return
     */
    @Override
    public ResponseResult webFriendLinkList() {
        List<FriendLinkVO> list = baseMapper.selectLinkList(UP.code);
        return ResponseResult.success(list);
    }

    /**
     * 申请友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult applyFriendLink(FriendLink friendLink) {

        Assert.isTrue(StringUtils.isNotBlank(friendLink.getUrl()),"输入正确的网址!");
        friendLink.setStatus(APPLY.getCode());

        Assert.isTrue(!friendLink.getUrl().contains("rulint.com") &&
                !friendLink.getUrl().contains("baidu.com"),"不合法的网址！");

        //如果已经申请过友链 再次接入则会进行下架处理 需重新审核
        FriendLink entity = baseMapper.selectOne(new QueryWrapper<FriendLink>()
                .eq(FieldConstants.URL,friendLink.getUrl()));
        if (entity != null) {
            friendLink.setId(entity.getId());
            baseMapper.updateById(friendLink);
        }else {
            baseMapper.insert(friendLink);
        }
        //异步操作邮箱发送
        emailService.emailNoticeMe("友链接入通知","网站有新的友链接入啦("+friendLink.getUrl()+")，快去审核吧!!!");
        return ResponseResult.success();
    }
}
