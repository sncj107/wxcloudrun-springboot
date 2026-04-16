package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 创建词汇列表请求
 */
@Data
public class CreateWordListRequest {

    /**
     * 词汇列表名称
     */
    private String name;

    /**
     * 词汇列表，使用逗号分隔的字符串
     */
    private String words;
}
