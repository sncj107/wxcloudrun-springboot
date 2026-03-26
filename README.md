# wxcloudrun-springboot
[![GitHub license](https://img.shields.io/github/license/WeixinCloud/wxcloudrun-express)](https://github.com/WeixinCloud/wxcloudrun-express)
![GitHub package.json dependency version (prod)](https://img.shields.io/badge/maven-3.6.0-green)
![GitHub package.json dependency version (prod)](https://img.shields.io/badge/jdk-11-green)

微信云托管 Java Springboot 框架模版，实现简单的计数器读写接口，使用云托管 MySQL 读写、记录计数值。

**新增功能**：
- ✅ 微信小程序登录功能
- ✅ 百度AI 文本转语音服务
- ✅ 用户信息管理

![](https://qcloudimg.tencent-cloud.cn/raw/be22992d297d1b9a1a5365e606276781.png)


## 快速开始
前往 [微信云托管快速开始页面](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/basic/guide.html)，选择相应语言的模板，根据引导完成部署。

## 本地调试
下载代码在本地调试，请参考[微信云托管本地调试指南](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/guide/debug/)。

## 实时开发
代码变动时，不需要重新构建和启动容器，即可查看变动后的效果。请参考[微信云托管实时开发指南](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/guide/debug/dev.html)

## Dockerfile最佳实践
请参考[如何提高项目构建效率](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/scene/build/speed.html)

## 目录结构说明
~~~
.
├── Dockerfile                      Dockerfile 文件
├── LICENSE                         LICENSE 文件
├── README.md                       README 文件
├── container.config.json           模板部署「服务设置」初始化配置（二开请忽略）
├── mvnw                            mvnw 文件，处理mevan版本兼容问题
├── mvnw.cmd                        mvnw.cmd 文件，处理mevan版本兼容问题
├── pom.xml                         pom.xml文件
├── settings.xml                    maven 配置文件
├── springboot-cloudbaserun.iml     项目配置文件
└── src                             源码目录
    └── main                        源码主目录
        ├── java                    业务逻辑目录
        └── resources               资源文件目录
~~~


## 服务 API 文档

### 微信小程序登录 API

#### `POST /api/wx/login`

微信小程序登录接口

**请求参数**：
```json
{
  "code": "微信登录凭证 code",
  "nickname": "用户昵称（可选）",
  "avatarUrl": "头像 URL（可选）",
  "gender": 1,  // 性别 0-未知 1-男 2-女（可选）
  "language": "zh_CN",  // 语言（可选）
  "city": "深圳",  // 城市（可选）
  "province": "广东",  // 省份（可选）
  "country": "中国"  // 国家（可选）
}
```

**响应结果**：
```json
{
  "code": 0,
  "data": {
    "openId": "oXXXX-XXXXXXXXXXXXXXXXX",
    "sessionKey": "XXXXXXXXXXXXXXXX",
    "unionId": "oXXXX-XXXXXXXXXXXXXXXXX",
    "userInfo": {
      "nickname": "张三",
      "avatarUrl": "https://...",
      "gender": 1,
      "language": "zh_CN",
      "city": "深圳",
      "province": "广东",
      "country": "中国"
    }
  }
}
```

**调用示例**：
```bash
curl -X POST -H 'content-type: application/json' \
  -d '{"code":"071xxx","nickname":"测试用户"}' \
  https://<云托管服务域名>/api/wx/login
```

#### `GET /api/wx/test`

测试微信服务是否正常

#### `GET /api/wx/token`

获取微信 access_token（用于调用其他微信 API）

### 百度AI 文本转语音 API

#### `POST /api/baidu/text2audio`

将文本转换为语音

**请求参数**：
```json
{
  "text": "你好世界",
  "cuid": "user_unique_id"
}
```

**响应**：MP3 音频文件

#### `GET /api/baidu/test`

测试百度AI 服务

### 计数器 API

### `GET /api/count`

获取当前计数

#### 请求参数

无

#### 响应结果

- `code`：错误码
- `data`：当前计数值

##### 响应结果示例

```json
{
  "code": 0,
  "data": 42
}
```

#### 调用示例

```
curl https://<云托管服务域名>/api/count
```



### `POST /api/count`

更新计数，自增或者清零

#### 请求参数

- `action`：`string` 类型，枚举值
  - 等于 `"inc"` 时，表示计数加一
  - 等于 `"clear"` 时，表示计数重置（清零）

##### 请求参数示例

```
{
  "action": "inc"
}
```

#### 响应结果

- `code`：错误码
- `data`：当前计数值

##### 响应结果示例

```json
{
  "code": 0,
  "data": 42
}
```

#### 调用示例

```
curl -X POST -H 'content-type: application/json' -d '{"action": "inc"}' https://<云托管服务域名>/api/count
```

## 使用注意
如果不是通过微信云托管控制台部署模板代码，而是自行复制/下载模板代码后，手动新建一个服务并部署，需要在「服务设置」中补全以下环境变量，才可正常使用，否则会引发无法连接数据库，进而导致部署失败。

### 数据库配置（必填）
- `MYSQL_ADDRESS`
- `MYSQL_PASSWORD`
- `MYSQL_USERNAME`

以上三个变量的值请按实际情况填写。如果使用云托管内 MySQL，可以在控制台 MySQL 页面获取相关信息。

### 微信小程序配置（如使用登录功能）
- `WX_MINIAPP_APPID`: 小程序 AppID
- `WX_MINIAPP_SECRET`: 小程序 AppSecret

在 [微信公众平台](https://mp.weixin.qq.com/) -> 开发 -> 基本配置 中获取。

### 百度AI 配置（如使用文本转语音功能）
- `BAIDU_API_KEY`: 百度AI API Key
- `BAIDU_SECRET_KEY`: 百度AI Secret Key

在 [百度AI 开放平台](https://ai.baidu.com/) 创建应用后获取。


## License

[MIT](./LICENSE)
