package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息 Mapper 接口
 */
@Mapper
public interface UserMapper {

    /**
     * 根据 openId 查询用户信息
     * @param openId 微信用户唯一标识
     * @return 用户信息
     */
    User findByOpenId(@Param("openId") String openId);

    /**
     * 插入用户信息
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);

    /**
     * 根据 openId 删除用户
     * @param openId 微信用户唯一标识
     * @return 影响行数
     */
    int deleteByOpenId(@Param("openId") String openId);
}
