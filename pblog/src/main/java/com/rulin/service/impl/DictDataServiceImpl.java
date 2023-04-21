package com.rulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.common.FieldConstants;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Dict;
import com.rulin.entity.DictData;
import com.rulin.mapper.DictDataMapper;
import com.rulin.service.DictDataService;
import com.rulin.service.DictService;
import com.rulin.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rulin.common.Constants.*;
import static com.rulin.common.FieldConstants.LIMIT_ONE;
import static com.rulin.common.ResultCode.DATA_TAG_IS_EXIST;
import static com.rulin.enums.PublishEnum.PUBLISH;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    private final DictService dictService;

    /**
     * 获取字典数据列表
     *
     * @param dictId
     * @param isPublish
     * @return
     */
    @Override
    public ResponseResult listDictData(Integer dictId, Integer isPublish) {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<DictData>()
                .eq(FieldConstants.DICT_TYPE_ID, dictId).eq(isPublish != null, FieldConstants.IS_PUBLISH, isPublish);
        Page<DictData> data = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), queryWrapper);
        data.getRecords().forEach(item -> {
            Dict dict = dictService.getById(item.getDictId());
            item.setDict(dict);
        });
        return ResponseResult.success(data);
    }

    /**
     * 添加字典数据
     *
     * @param dictData
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertDictData(DictData dictData) {
        // 判断添加的字典数据是否存在
        isExist(dictData);
        baseMapper.insert(dictData);
        return ResponseResult.success();
    }

    /**
     * 修改字典数据
     *
     * @param sysDictData
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateDictData(DictData sysDictData) {

        DictData dictData = baseMapper.selectOne(new QueryWrapper<DictData>().eq(FieldConstants.DICT_LABEL, sysDictData.getLabel()));
        if (dictData != null && !dictData.getId().equals(sysDictData.getId())) return ResponseResult.error("该标签已存在!");

        baseMapper.updateById(sysDictData);
        return ResponseResult.success();
    }

    /**
     * 批量删除字典数据
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteDictData(Long id) {
        baseMapper.deleteById(id);
        return ResponseResult.success();
    }

    /**
     * 根据字典类型获取字典数据
     *
     * @param types
     * @return
     */
    @Override
    public ResponseResult getDataByDictType(List<String> types) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(FieldConstants.TYPE, types).eq(FieldConstants.IS_PUBLISH, PUBLISH.getCode());
        List<Dict> dictList = dictService.list(queryWrapper);
        dictList.forEach(item -> {
            QueryWrapper<DictData> sysDictDataQueryWrapper = new QueryWrapper<>();
            sysDictDataQueryWrapper.eq(FieldConstants.IS_PUBLISH, PUBLISH.getCode());
            sysDictDataQueryWrapper.eq(FieldConstants.DICT_TYPE_ID, item.getId());
            sysDictDataQueryWrapper.orderByAsc(FieldConstants.SORT);
            List<DictData> dataList = baseMapper.selectList(sysDictDataQueryWrapper);
            String defaultValue = null;
            for (DictData dictData : dataList) {
                //选取默认值
                if (dictData.getIsDefault().equals(ONE)) {
                    defaultValue = dictData.getValue();
                    break;

                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put(DEFAULT_VALUE, defaultValue);
            result.put(LIST, dataList);
            map.put(item.getType(), result);
        });
        return ResponseResult.success(map);
    }

    //-------------自定义方法开始-----------
    public void isExist(DictData dictData) {
        DictData temp = baseMapper.selectOne(new QueryWrapper<DictData>()
                .eq(FieldConstants.DICT_LABEL, dictData.getLabel())
                .eq(FieldConstants.DICT_TYPE_ID, dictData.getDictId())
                .last(LIMIT_ONE));
        Assert.notNull(temp, DATA_TAG_IS_EXIST.getDesc());
    }
}
