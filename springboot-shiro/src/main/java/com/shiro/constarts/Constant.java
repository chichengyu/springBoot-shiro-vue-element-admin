package com.shiro.constarts;

/**
 * @ClassName: Constant
 */
public interface Constant {


    /**
     * 用户名称 key（用于生成token与只能一个账号同时存在）
     */
    String JWT_USER_NAME="jwt-user-name-key";

    /**
     * 权限key
     */
    String JWT_PERMISSIONS_KEY="jwt-permissions-key_";

    /**
     * 角色key
     */
    String JWT_ROLES_KEY="jwt-roles-key_";

    /**
     * 标记用户需要重新登录，场景 比如修改了用户的角色/权限/菜单
     */
    String JWT_USER_LOGIN_BLACKLIST="jwt-access-token-blacklist_";

    /**
     * 正常token
     */
    String ACCESS_TOKEN="authorization";

    /**
     * 标记用户是否已经被锁定
     */
    String ACCOUNT_LOCK_KEY="account-lock-key_";

    /**
     * 标记用户是否已经删除
     */
    String DELETED_USER_KEY="deleted-user-key_";


    /**
     * 用户权鉴缓存 key
     */
    String IDENTIFY_CACHE_KEY="shiro-cache:com.xh.lesson.shiro.CustomRealm.authorizationCache:";

}
