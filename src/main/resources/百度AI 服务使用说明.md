# 百度AI 服务集成说明

## 功能概述

本项目已集成百度AI 开放平台的文本转语音 (Text-to-Speech) 服务，可以将文字转换为语音输出。

## API 接口

### 1. 文本转语音
```
POST /api/tts/text2audio
Content-Type: application/json

{
  "text": "你好，这是一个测试",
  "cuid": "test_user_001"
}
```

请求参数:
- `text`: 要转换的文本内容 (必填)
- `cuid`: 用户唯一标识，用于区分用户 (必填)

响应:
- 成功：返回 MP3 格式的音频文件 (Content-Type: audio/mpeg)
- 失败：返回错误信息

### 2. 测试接口
```
GET /api/baidu/test
```

## 配置说明

### 1. 环境变量配置

在运行前需要设置以下环境变量:

```bash
export BAIDU_API_KEY="your_api_key"
export BAIDU_SECRET_KEY="your_secret_key"
```

或者在 `application.yml` 中直接配置:

```yaml
baidu:
  ai:
    api-key: your_api_key
    secret-key: your_secret_key
```

### 2. 获取百度API 密钥

1. 访问 [百度AI 开放平台](https://cloud.baidu.com/)
2. 注册并登录账号
3. 创建应用，选择需要的服务 (语音技术)
4. 获取 API Key 和 Secret Key

## 使用示例

### 方式一：使用提供的 HTML 页面

访问 `/baidu-tts.html` 页面，在页面上输入文本和用户标识，点击"转换为语音"按钮即可。

### 方式二：通过 API 调用

```javascript
// 示例代码
fetch('/api/baidu/text2audio', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    text: '你好，世界',
    cuid: 'user_123'
  })
})
.then(response => response.blob())
.then(blob => {
  // blob 是音频数据
  const audioUrl = URL.createObjectURL(blob);
  const audio = new Audio(audioUrl);
  audio.play();
})
.catch(error => console.error('Error:', error));
```

### 方式三：使用 cURL

```bash

# 文本转语音并保存为文件
curl -X POST http://localhost/api/baidu/text2audio \
  -H "Content-Type: application/json" \
  -d '{"text":"你好世界","cuid":"test001"}' \
  --output audio.mp3
```

## 技术实现

### 核心组件

1. **BaiduAIService** - 服务接口
   - `getAccessToken()`: 获取百度AI 访问令牌
   - `textToAudio()`: 文本转语音

2. **BaiduAIServiceImpl** - 服务实现
   - 使用 OkHttp 进行 HTTP 请求
   - 自动缓存 access_token (有效期 30 天)
   - 提前 5 分钟刷新 token 避免过期

3. **BaiduAIController** - REST 控制器
   - 提供 RESTful API 接口
   - 处理请求和响应

4. **DTO 类**:
   - `BaiduAuthResponse`: 鉴权响应
   - `TextToAudioRequest`: 文本转语音请求

### API 调用流程

1. 首次调用时，自动向百度 OAuth2.0 授权服务请求 access_token
2. 缓存 access_token，后续调用直接使用
3. 调用文本转语音 API 时，将 access_token 作为参数传入
4. 根据响应 Content-Type 判断是否成功
   - 以 "audio" 开头：返回 MP3 文件
   - 否则：返回错误 JSON

## 注意事项

1. **access_token 有效期**: 默认为 30 天，系统会自动缓存和刷新
2. **CUID 要求**: 长度不超过 60 字符，建议使用 MAC 地址或 IMEI 码
3. **文本长度**: 短文本合成，建议不超过 1024 个字节
4. **并发限制**: 注意百度API 的 QPS 限制
5. **错误处理**: 如果返回 JSON 错误，检查 err_no 和 err_msg 字段

## 常见错误码

- 500: 不支持的操作
- 501: 参数错误
- 502: APP ID 不存在
- 503: 权限不足
- 600: 访问频率超限

详细错误码请参考 [百度AI 官方文档](https://ai.baidu.com/ai-doc/TTS/Syk8zbvfz)

## 依赖项

- OkHttp 4.9.0: HTTP 客户端
- FastJSON 2.0.4: JSON 解析
- Spring Boot 2.5.5
- Lombok (可选)

## 项目结构

```
src/main/java/com/tencent/wxcloudrun/
├── controller/
│   └── BaiduAIController.java          # REST 控制器
├── dto/
│   ├── BaiduAuthResponse.java          # 鉴权响应 DTO
│   └── TextToAudioRequest.java         # 请求 DTO
├── service/
│   ├── BaiduAIService.java             # 服务接口
│   └── impl/
│       └── BaiduAIServiceImpl.java     # 服务实现
```

## 测试

启动应用后:

```bash
# 1. 测试服务是否正常
curl http://localhost/api/baidu/test

# 2. 获取 access_token
curl http://localhost/api/baidu/token

# 3. 文本转语音
curl -X POST http://localhost/api/baidu/text2audio \
  -H "Content-Type: application/json" \
  -d '{"text":"你好","cuid":"test"}' \
  --output test.mp3
```

## 参考资料

- [百度AI 开放平台 - 语音技术](https://cloud.baidu.com/speech)
- [短文本在线合成 API 文档](https://ai.baidu.com/ai-doc/TTS/Sykgds88r)
- [OAuth2.0 授权说明](https://ai.baidu.com/ai-doc/REFERENCE/Ckizdw4r9)
