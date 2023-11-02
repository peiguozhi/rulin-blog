package com.rulin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.system.mapper.DictionaryMapper;
import com.rulin.system.model.po.Dictionary;
import com.rulin.system.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 数据字典 服务实现类
 *
 * @author 程序儒
 * @date 2023-08-14 13:27:20
 */
@Slf4j
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Override
    public List<Dictionary> queryAll() {

        List<Dictionary> list = this.list();


        return list;
    }

    @Override
    public Dictionary getByCode(String code) {


        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getCode, code);

        Dictionary dictionary = this.getOne(queryWrapper);


        return dictionary;
    }
}
