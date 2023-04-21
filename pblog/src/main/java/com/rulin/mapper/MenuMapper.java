package com.rulin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rulin.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统管理-权限资源表  Mapper 接口
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> getMenuByUserId(Object loginId);
}
