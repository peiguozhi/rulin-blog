package com.rulin.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rulin.content.mapper.CourseCategoryMapper;
import com.rulin.content.model.dto.CourseCategoryTreeDto;
import com.rulin.content.model.po.CourseCategory;
import com.rulin.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    /**
     * 课程分类树形结构查询  (使用hutool工具类的TreeUtil实现)
     *
     * @param id id
     * @return {@code List<Tree<String>>}
     */
    @Override
    public List<Tree<String>> queryTreeNodes(String id) {

        //1.获取所有资料分类
        List<CourseCategoryTreeDto> courseCategoryList = courseCategoryMapper
                .selectList(new LambdaQueryWrapper<CourseCategory>().eq(CourseCategory::getIsShow, 1))
                .stream()
                .map(item -> BeanUtil.copyProperties(item, CourseCategoryTreeDto.class))// 实体类转换
                .collect(Collectors.toList());

        //2.配置
        TreeNodeConfig config = new TreeNodeConfig();
        config.setDeep(3);      //最大递归深度
        config.setParentIdKey("parentid");  // 父id 别名
        config.setChildrenKey("childrenTreeNodes");  // 子节点别名
        config.setWeightKey("orderby"); // 排序字段

        //3.转树          id 为从哪一级开始  1 为 去掉根节点，从二级节点开始查询
        List<Tree<String>> treeList = TreeUtil.build(courseCategoryList, id, config, (object, treeNode) -> {
            treeNode.setId(object.getId());
            treeNode.setParentId(object.getParentid());
            treeNode.setName(object.getName());
            treeNode.setWeight(object.getOrderby());
            // 额外的值
            treeNode.put("label", object.getLabel());
            treeNode.put("isShow", object.getIsShow());
            treeNode.put("isLeaf", object.getIsLeaf());
        });

        return treeList;
    }

}
