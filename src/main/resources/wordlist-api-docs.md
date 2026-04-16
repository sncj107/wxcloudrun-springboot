# 词汇列表 API 文档

## 基础信息

- **基础路径**: `/api/wordlists`
- **认证方式**: 通过请求头 `X-WX-OPENID` 传递用户 openId

## 接口说明

### 1. 创建词汇列表

**接口**: `POST /api/wordlists`

**描述**: 创建一个新的词汇列表

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 词汇列表名称 |
| words | string | 否 | 初始词汇，多个词汇用逗号分隔 |

**请求头**:
```
X-WX-OPENID: user_openid_here
```

**请求体示例**:
```json
{
  "name": "我的生词本",
  "words": "apple,banana,orange"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "我的生词本",
    "words": "apple,banana,orange",
    "openId": "user_openid_here",
    "createdTime": "2024-01-01T00:00:00",
    "updatedTime": "2024-01-01T00:00:00"
  }
}
```

---

### 2. 删除词汇列表

**接口**: `DELETE /api/wordlists/{id}`

**描述**: 删除指定的词汇列表

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | integer | 是 | 词汇列表ID |

**请求头**:
```
X-WX-OPENID: user_openid_here
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 3. 更新词汇列表

**接口**: `PUT /api/wordlists/{id}`

**描述**: 更新指定的词汇列表信息

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | integer | 是 | 词汇列表ID |

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 词汇列表名称 |
| words | string | 是 | 词汇内容，多个词汇用逗号分隔 |

**请求头**:
```
X-WX-OPENID: user_openid_here
```

**请求体示例**:
```json
{
  "name": "更新后的生词本",
  "words": "apple,banana,orange,grape"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "更新后的生词本",
    "words": "apple,banana,orange,grape",
    "openId": "user_openid_here",
    "createdTime": "2024-01-01T00:00:00",
    "updatedTime": "2024-01-01T01:00:00"
  }
}
```

---

### 4. 查询词汇列表详情

**接口**: `GET /api/wordlists/{id}`

**描述**: 获取指定词汇列表的详细信息

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | integer | 是 | 词汇列表ID |

**请求头**:
```
X-WX-OPENID: user_openid_here
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "我的生词本",
    "words": "apple,banana,orange",
    "openId": "user_openid_here",
    "createdTime": "2024-01-01T00:00:00",
    "updatedTime": "2024-01-01T00:00:00"
  }
}
```

---

### 5. 查询用户的所有词汇列表

**接口**: `GET /api/wordlists/list`

**描述**: 获取当前用户的所有词汇列表

**请求头**:
```
X-WX-OPENID: user_openid_here
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "我的生词本",
      "words": "apple,banana,orange",
      "openId": "user_openid_here",
      "createdTime": "2024-01-01T00:00:00",
      "updatedTime": "2024-01-01T00:00:00"
    },
    {
      "id": 2,
      "name": "高级词汇",
      "words": "sophisticated,comprehensive,articulate",
      "openId": "user_openid_here",
      "createdTime": "2024-01-02T00:00:00",
      "updatedTime": "2024-01-02T00:00:00"
    }
  ]
}
```

---

### 6. 添加词汇到列表

**接口**: `POST /api/wordlists/{id}/words`

**描述**: 向指定词汇列表中添加一个新词汇

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | integer | 是 | 词汇列表ID |

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| word | string | 是 | 要添加的词汇 |

**请求头**:
```
X-WX-OPENID: user_openid_here
```

**请求示例**:
```
POST /api/wordlists/1/words?word=pear
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "我的生词本",
    "words": "apple,banana,orange,pear",
    "openId": "user_openid_here",
    "createdTime": "2024-01-01T00:00:00",
    "updatedTime": "2024-01-01T02:00:00"
  }
}
```

---

### 7. 从列表中删除词汇

**接口**: `DELETE /api/wordlists/{id}/words`

**描述**: 从指定词汇列表中删除一个词汇

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | integer | 是 | 词汇列表ID |

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| word | string | 是 | 要删除的词汇 |

**请求头**:
```
X-WX-OPENID: user_openid_here
```

**请求示例**:
```
DELETE /api/wordlists/1/words?word=banana
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "我的生词本",
    "words": "apple,orange,pear",
    "openId": "user_openid_here",
    "createdTime": "2024-01-01T00:00:00",
    "updatedTime": "2024-01-01T03:00:00"
  }
}
```

## 错误响应

**错误响应格式**:
```json
{
  "code": 500,
  "message": "错误信息",
  "data": null
}
```

**常见错误**:
- `未找到用户 openId`: 请求头中缺少 `X-WX-OPENID`
- `词汇列表不存在或无权操作`: 词汇列表不存在或用户无权限操作

## 数据结构

### WordList 对象

| 字段 | 类型 | 说明 |
|------|------|------|
| id | integer | 词汇列表唯一标识 |
| name | string | 词汇列表名称 |
| words | string | 词汇内容，多个词汇用逗号分隔 |
| openId | string | 用户 openId |
| createdTime | string | 创建时间 |
| updatedTime | string | 更新时间 |
