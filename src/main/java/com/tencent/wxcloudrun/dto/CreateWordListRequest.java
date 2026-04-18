package com.tencent.wxcloudrun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建词汇列表请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
