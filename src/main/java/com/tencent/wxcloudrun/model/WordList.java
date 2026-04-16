package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 词汇列表实体类
 */
@Data
public class WordList implements Serializable {

    private Integer id;

    /**
     * 词汇列表名称
     */
    private String name;

    /**
     * 词汇列表，使用逗号分隔的字符串存储
     */
    private String words;

    /**
     * 所属用户的微信唯一标识
     */
    private String openId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
