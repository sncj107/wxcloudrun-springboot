#### 添加调用百度API服务
1. 鉴权认证接口
   - 百度AI开放平台使用OAuth2.0授权调用开放API，调用API时必须在URL中带上Access_token参数，Access token默认有效期为30天，获取Access_token的流程如下：
   - 请求URL数据格式
   - 向授权服务地址 https://aip.baidubce.com/oauth/2.0/token 发送请求（推荐使用POST），并在URL中带上以下参数：
     - grant_type： 必须参数，固定为client_credentials；
     - client_id： 必须参数，应用的API Key（设定在 application.yml 的环境变量里）；
     - client_secret： 必须参数，应用的Secret Key（设定在 application.yml 的环境变量里）；
   - 示例代码如下：
```
package baidu.com;

import okhttp3.*;
import org.json.JSONObject;

import java.io.*;

class Sample {

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static void main(String []args) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
            .url("https://aip.baidubce.com/oauth/2.0/token?client_id=&client_secret=&grant_type=client_credentials")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
```
2. 短文本在线合成API
    - 百度AI开放平台提供了短文本在线合成API接口，开发者可以通过调用该接口实现文本转语音功能。接口地址为：https://tsn.baidu.com/text2audio ,使用POST方法进行请求。
    - 请求参数包括：
      - tex：必填，合成的文本，使用UTF-8编码。
      - cuid：必填，用户唯一标识，用来计算UV值。建议填写能区分用户的机器 MAC 地址或 IMEI 码，长度为60字符以内。
      - per：固定为 3
      - lan：固定为 zh
      - aue：固定为 3，表示返回的音频格式为mp3
      - tok: 必填，开放平台获取到的开发者[access_token]获取 Access Token "access_token")
    - 返回说明
      需要根据 Content-Type的头部来确定是否服务端合成成功。
      - 如果合成成功，返回的Content-Type以“audio”开头
        - 返回为二进制mp3文件，具体header信息 Content-Type: audio/mp3；
      - 如果合成出现错误，则会返回json文本，具体header信息为：Content-Type: application/json。其中sn字段主要用于DEBUG追查问题，如果出现问题，可以提供sn帮助确认问题。
        - 错误示例
        - ```json
          {"err_no":500,"err_msg":"notsupport.","sn":"abcdefgh","idx":1}
        ```


