package com.ike.o2o.until;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    // 需要加密的字段数组
    private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};

    /**
     * 对关键的属性进行转换
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            // 对已加密的字段进行解密工作
            return DESUtil.getDecryptString(propertyValue);
        } else {
            return propertyValue;
        }
    }

    /**
     * 该属性是否已加密
     *
     * @param propertyName  属性名称
     * @return boolean
     */
    private boolean isEncryptProp(String propertyName) {
        // 若等于需要加密的field，则进行加密
        for (String encryptpropertyName : encryptPropNames) {
            if (encryptpropertyName.equals(propertyName))
                return true;
        }
        return false;
    }
}
