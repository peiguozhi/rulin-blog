package com.rulin.system.controller;

import com.rulin.system.model.po.Dictionary;
import com.rulin.system.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 数据字典 前端控制器
 *
 * @author 程序儒
 * @date 2023-08-14 13:25:36
 */
@Slf4j
@RestController
public class DictionaryController  {

    @Autowired
    private DictionaryService  dictionaryService;

    @GetMapping("/dictionary/all")
    public List<Dictionary> queryAll() {
        return dictionaryService.queryAll();
    }

    @GetMapping("/dictionary/code/{code}")
    public Dictionary getByCode(@PathVariable String code) {
        return dictionaryService.getByCode(code);
    }
}
