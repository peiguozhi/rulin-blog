package com.rulin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rulin.entity.UserRole;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 系统管理 - 用户角色关联表  Mapper 接口
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

    // List<Menu> selectMenuByUserId(Long id);
}
