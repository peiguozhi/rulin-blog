package com.rulin.mapper;

import com.rulin.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Repository
public interface MessageMapper extends BaseMapper<Message> {

    void passBatch(@Param("ids") List<Integer> ids);
}
