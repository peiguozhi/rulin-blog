package com.rulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Menu;
import java.util.List;

/**
 * @author CodeScholar
 * @description:
 * @date 2023年4月9日
 */
public interface MenuService extends IService<Menu>{

    List<Menu> listMenuTree(List<Menu> list);

    ResponseResult listMenuApi(Integer id);

    ResponseResult insertMenu(Menu menu);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenuById(Integer id);
}
