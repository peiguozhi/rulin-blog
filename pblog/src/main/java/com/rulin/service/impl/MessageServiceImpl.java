package com.rulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Message;
import com.rulin.mapper.MessageMapper;
import com.rulin.service.MessageService;
import com.rulin.util.IpUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.rulin.common.ResultCode.PARAMS_ILLEGAL;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final HttpServletRequest request;

    /**
     * 留言列表
     * @param name
     * @return
     */
    @Override
    public ResponseResult listMessage(String name) {
        LambdaQueryWrapper<Message> queryWrapper = new QueryWrapper<Message>().lambda()
                .like(StringUtils.isNotBlank(name),Message::getNickname,name).orderByDesc(Message::getCreateTime);
        Page<Message> list = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),queryWrapper);
        return ResponseResult.success(list);
    }

    /**
     * 批量通过留言
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult passBatch(List<Integer> ids) {
        Assert.notEmpty(ids,PARAMS_ILLEGAL.getDesc());
        baseMapper.passBatch(ids);
        return ResponseResult.success();
    }

    /**
     * 删除留言
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteMessageById(int id) {
        baseMapper.deleteById(id);
        return ResponseResult.success();
    }

    /**
     * 批量删除留言
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("批量删除留言失败");
    }



                //    -------web端方法开始-------
    /**
     * 留言列表
     * @return
     */
    @Override
    public ResponseResult webMessage() {
        // 查询留言列表
        List<Message> messageList = baseMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar,
                        Message::getContent, Message::getTime));
        return ResponseResult.success(messageList);
    }

    /**
     * 添加留言
     * @param message
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult webAddMessage(Message message) {
        // 获取用户ip
        String ipAddress = IpUtils.getIp(request);
        String ipSource = IpUtils.getIp2region(ipAddress);
        message.setIpAddress(ipAddress);
        message.setIpSource(ipSource);
        baseMapper.insert(message);
        return ResponseResult.success("留言成功");
    }


}
