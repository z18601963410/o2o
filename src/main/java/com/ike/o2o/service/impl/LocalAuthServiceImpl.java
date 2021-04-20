package com.ike.o2o.service.impl;

import com.ike.o2o.dao.LocalAuthDao;
import com.ike.o2o.dto.LocalAuthExecution;
import com.ike.o2o.entity.LocalAuth;
import com.ike.o2o.enums.LocalAuthStateEnum;
import com.ike.o2o.exception.LocalAuthOperationException;
import com.ike.o2o.service.LocalAuthService;
import com.ike.o2o.until.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
    @Autowired
    private LocalAuthDao localAuthDao;

    /**
     * 通过用户和密码查询本地用户对象
     *
     * @param userName 用户名
     * @param password 密码
     * @return 返回查询后的对象
     */
    @Override
    public LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password) {
        return localAuthDao.queryLocalByUserNameAndPwd(userName, MD5.getMd5(password));
    }

    /**
     * 通过用户ID查询本地用户对象
     *
     * @param userId 用户ID(personInfo id)
     * @return 本地用户对象
     */
    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    /**
     * 本地用户对象与用户信息对象绑定
     *
     * @param localAuth 本地用户对象:该对象包含本地用户对象信息和需要关联的用户信息对象
     * @return 结果
     * @throws LocalAuthOperationException 异常保证事务回滚
     */
    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        //非空判断
        if (localAuth == null || localAuth.getUsername() == null || localAuth.getPassword() == null ||
                localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
            throw new LocalAuthOperationException
                    ("localAuth对象不合法: localAuth or username or password or personInfo or personInfoId 为NULL");
        }
        //判断本地对象是否绑定了personInfo对象
        Long personInfoId = localAuth.getPersonInfo().getUserId();
        if (localAuthDao.queryLocalByUserId(personInfoId) == null) {
            //初始化参数
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            //执行插入
            int affect = localAuthDao.insertLocalAuth(localAuth);
            if (affect == 1) {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
            } else {
                throw new LocalAuthOperationException("绑定失败,未成功执行localAuth对象的数据插入");
            }
        } else {
            //已绑定对象
            return new LocalAuthExecution(LocalAuthStateEnum.IS_BINDING, localAuth);
        }
    }

    /**
     * 修改本地用户对象信息
     *
     * @param userId      personInfo id
     * @param username    用户名
     * @param password    密码
     * @param newPassword 新密码
     * @return 处理结果
     * @throws LocalAuthOperationException 触发异常保证事务回滚
     */
    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException {
        //非空判断
        if (userId == null || username == null || password == null || password.equals(newPassword)) {
            throw new LocalAuthOperationException("userId or username or password is null or password == newPassword");
        } else {
            //提交修改
            int affect = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password), MD5.getMd5(newPassword), new Date());
            if (affect == 1) {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, new LocalAuth());
            } else {
                throw new LocalAuthOperationException("更新失败: modifyLocalAuth方法执行结果不等于1");
            }
        }

    }
}
