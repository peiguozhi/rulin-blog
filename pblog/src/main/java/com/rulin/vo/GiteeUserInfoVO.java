package com.rulin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiteeUserInfoVO {

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar_url;

    /**
     * id
     */
    private String id;

}
