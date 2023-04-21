package com.rulin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.common.ResponseResult;
import com.rulin.config.satoken.MySaTokenListener;
import com.rulin.config.satoken.OnlineUser;
import com.rulin.vo.SystemUserVO;
import com.rulin.vo.UserVO;
import com.rulin.entity.Menu;
import com.rulin.entity.User;
import com.rulin.enums.UserStatusEnum;
import com.rulin.mapper.UserAuthMapper;
import com.rulin.mapper.UserMapper;
import com.rulin.service.MenuService;
import com.rulin.service.UserService;
import com.rulin.util.PageUtils;
import com.rulin.util.AesEncryptUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

import static com.rulin.common.ResultCode.ERROR_USER_NOT_EXIST;

/**
 * @author CodeScholar
 * @description:
 * @date 2023年4月9日
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final MenuService menuService;

    private final UserAuthMapper userAuthMapper;


    /**
     * 用户列表
     * @param username
     * @param loginType
     * @return
     */
    @Override
    public ResponseResult listUser(String username, Integer loginType) {
        Page<UserVO> page = baseMapper.selectPageRecord(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),username,loginType);
        return ResponseResult.success(page);
    }

    /**
     * 用户详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult getUserById(Integer id) {
        SystemUserVO user = baseMapper.getById(id);
        return ResponseResult.success(user);
    }

    /**
     *  添加用户
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertUser(User user) {
        user.setPassword(AesEncryptUtils.aesEncrypt(user.getPassword()));
        user.setStatus(UserStatusEnum.normal.code);
        baseMapper.insert(user);
       // roleMapper.insertToUserId(user.getId(),user.getRoleId());
        return ResponseResult.success(user);
    }

    /**
     *  修改用户
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUser(User user) {
        baseMapper.updateById(user);
        //roleMapper.updateByUserId(user.getId(),user.getRoleId());
        return ResponseResult.success();
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Integer> ids) {
        userAuthMapper.deleteByUserIds(ids);
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0? ResponseResult.success(): ResponseResult.error("删除失败");
    }

    /**
     * 获取当前登录用户详情
     * @return
     */
    @Override
    public ResponseResult getCurrentUserInfo() {
        return ResponseResult.success("获取当前登录用户信息成功", baseMapper.getById(StpUtil.getLoginIdAsInt()));
    }

    /**
     * 获取当前登录用户所拥有的菜单权限
     * @return
     */
    @Override
    public ResponseResult getCurrentUserMenu() {
        List<Integer> menuIds = baseMapper.getMenuId(StpUtil.getLoginIdAsInt());
        List<Menu> menus = menuService.listByIds(menuIds);
        List<Menu> menuTree = menuService.listMenuTree(menus);
        return ResponseResult.success(menuTree);
    }

    /**
     * 修改密码
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updatePassword(Map<String, String> map) {

        User user = baseMapper.selectById(StpUtil.getLoginIdAsInt());
        Assert.notNull(user,ERROR_USER_NOT_EXIST.getDesc());

        boolean isValid = AesEncryptUtils.validate(user.getPassword(),map.get("oldPassword"));
        Assert.isTrue(isValid,"旧密码校验不通过!");

        String newPassword = AesEncryptUtils.aesEncrypt(map.get("newPassword"));
        user.setPassword(newPassword);
        baseMapper.updateById(user);
        return ResponseResult.success("修改成功");
    }

    /**
     * 在线用户
     * @param keywords
     * @return
     */
    @Override
    public ResponseResult listOnlineUsers(String keywords) {
        int pageNo = PageUtils.getPageNo().intValue();
        int pageSize = PageUtils.getPageSize().intValue();

        List<OnlineUser> onlineUsers = MySaTokenListener.ONLINE_USERS;
        //根据关键词过滤
        if (StringUtils.isNotBlank(keywords)) {
            onlineUsers = MySaTokenListener.ONLINE_USERS.stream().filter(item -> item.getNickname().contains(keywords)).collect(Collectors.toList());
        }
        //排序
        onlineUsers.sort((o1, o2) -> DateUtil.compare(o2.getLoginTime(), o1.getLoginTime()));
        int fromIndex = (pageNo-1) * pageSize;
        int toIndex = onlineUsers.size() - fromIndex > pageSize ? fromIndex + pageSize : onlineUsers.size();
        List<OnlineUser> userOnlineList = onlineUsers.subList(fromIndex, toIndex);
        logger.info("memory用户数：{}", userOnlineList.size());

        Map<String,Object> map = new HashMap<>();
        map.put("total",onlineUsers.size());
        map.put("records",userOnlineList);
        return ResponseResult.success(map);
    }

    /**
     * 踢人下线
     * @param token
     * @return
     */
    @Override
    public ResponseResult kick(String token) {
        logger.info("当前踢下线的用户token为:{}",token);
        StpUtil.kickoutByTokenValue(token);
        return ResponseResult.success();
    }
}
