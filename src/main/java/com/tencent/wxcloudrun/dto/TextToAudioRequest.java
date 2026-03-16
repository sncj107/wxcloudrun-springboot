package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 文本转语音请求 DTO
 */
@Data
public class TextToAudioRequest {
    /**
     * 合成的文本 (必填)
     */
    private String text;
    
    /**
     * 用户唯一标识，用来计算 UV 值 (必填)
     * 建议填写能区分用户的机器 MAC 地址或 IMEI 码，长度为 60 字符以内
     */
    private String cuid;
}
