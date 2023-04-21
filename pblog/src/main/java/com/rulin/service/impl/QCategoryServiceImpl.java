package com.rulin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.common.FieldConstants;
import com.rulin.common.ResponseResult;
import com.rulin.entity.QCategory;
import com.rulin.exception.BusinessException;
import com.rulin.mapper.QCategoryMapper;
import com.rulin.service.QCategoryService;
import com.rulin.util.DateUtils;
import com.rulin.util.PageUtils;
import com.rulin.vo.QCategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static com.rulin.common.FieldConstants.LIMIT_ONE;
import static com.rulin.common.ResultCode.CATEGORY_IS_EXIST;
import static com.rulin.common.ResultCode.CATEGORY_IS_TOP;

/**
 * <p>
 * 博客面试题分类表 服务实现类
 * </p>
 *
 * @author 程序儒
 * @date 2023年4月13日 14点58分
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QCategoryServiceImpl extends ServiceImpl<QCategoryMapper, QCategory> implements QCategoryService {

    /**
     * 面试题分类列表
     *
     * @param name 面试题分类名
     * @return
     */
    @Override
    public ResponseResult listQCategory(String name) {
        Page<QCategory> qCategoryPage = baseMapper.selectQCategory(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), name);
        return ResponseResult.success(qCategoryPage);
    }

    /**
     * 面试题分类详情
     *
     * @param id 面试题分类id
     * @return
     */
    @Override
    public ResponseResult getQCategoryById(Long id) {
        QCategory qCategory = baseMapper.selectById(id);
        return ResponseResult.success(qCategory);
    }

    /**
     * 添加面试题分类
     *
     * @param qCategory 面试题分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertQCategory(QCategory qCategory) {
        QCategory vo = baseMapper.selectOne(new QueryWrapper<QCategory>().eq(FieldConstants.NAME, qCategory.getName()));

        if (ObjectUtil.isNotNull(vo)) {
            throw new BusinessException("该面试题分类名称已存在!");
        }
        int rows = baseMapper.insert(qCategory);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("添加面试题分类失败");
    }

    /**
     * 修改面试题分类
     *
     * @param qCategory 面试题分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateQCategory(QCategory qCategory) {
        QCategory vo = baseMapper.selectOne(new QueryWrapper<QCategory>().eq(FieldConstants.NAME, qCategory.getName()));
        Assert.isTrue(!(vo != null && !vo.getId().equals(qCategory.getId())), CATEGORY_IS_EXIST.getDesc());

        int rows = baseMapper.updateById(qCategory);

        return rows > 0 ? ResponseResult.success() : ResponseResult.error("修改面试题分类失败");
    }

    /**
     * 删除面试题分类
     *
     * @param id 面试题分类id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteQCategory(Long id) {
        int rows = baseMapper.deleteById(id);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("删除面试题分类失败");
    }

    /**
     * 批量删除面试题分类
     *
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<QCategory> list) {
        List<Long> ids = list.stream().map(QCategory::getId).collect(Collectors.toList());
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("批量删除面试题分类失败");
    }

    /**
     * 置顶面试题分类
     *
     * @return 置顶面试题分类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult top(Long id) {
        QCategory qCategory = baseMapper.selectOne(new QueryWrapper<QCategory>().orderByDesc(FieldConstants.SORT).last(LIMIT_ONE));
        Assert.isTrue(!qCategory.getId().equals(id), CATEGORY_IS_TOP.getDesc());

        QCategory vo = QCategory.builder()
                .sort(qCategory.getSort() + 1).updateTime(DateUtils.getNowDate()).id(id).build();
        int rows = baseMapper.updateById(vo);

        return rows > 0 ? ResponseResult.success() : ResponseResult.error("置顶失败");
    }

    //-----------------web端方法开始-------------

    /**
     * 首页面试题分类列表
     *
     * @return
     */
    @Override
    public ResponseResult webList() {
        List<QCategoryVO> categories = baseMapper.selectAll();
        return ResponseResult.success(categories);
    }
}
