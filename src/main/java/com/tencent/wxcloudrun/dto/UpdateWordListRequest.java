package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 更新词汇列表请求
 */
@Data
public class UpdateWordListRequest {

    /**
     * 词汇列表名称
     */
    private String name;

    /**
     * 词汇列表 ID
     */
    private Integer id;

    /**
     * 词汇列表，使用逗号分隔的字符串
     */
    private String words;
}
