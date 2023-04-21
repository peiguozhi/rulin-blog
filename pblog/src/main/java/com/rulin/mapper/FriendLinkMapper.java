package com.rulin.mapper;

import com.rulin.vo.FriendLinkVO;
import com.rulin.entity.FriendLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 友情链接表 Mapper 接口
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Repository
public interface FriendLinkMapper extends BaseMapper<FriendLink> {

    Integer getMaxSort();

    void top(@Param("id") Integer id, @Param("sort") int sort);

    List<FriendLinkVO> selectLinkList(@Param("status") Integer status);

}
