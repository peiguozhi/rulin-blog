package com.rulin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rulin.system.model.po.Dictionary;

import java.util.List;


/**
 * 数据字典 服务类
 *
 * @author 程序儒
 * @date 2023-08-14 13:27:11
 */
public interface DictionaryService extends IService<Dictionary> {

    /**
     * 查询所有数据字典内容
     * @return
     */
    List<Dictionary> queryAll();

    /**
     * 根据code查询数据字典
     * @param code -- String 数据字典Code
     * @return
     */
    Dictionary getByCode(String code);
}
