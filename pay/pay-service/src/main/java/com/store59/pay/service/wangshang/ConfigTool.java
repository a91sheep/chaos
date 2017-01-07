package com.store59.pay.service.wangshang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cn.topca.api.cert.CertApiException;
import cn.topca.api.cert.TCA;

public class ConfigTool {
	
    private static ConfigTool configTool = new ConfigTool();
    public ConfigTool(){}
    public static ConfigTool getInstance(){
        return configTool;
    }
    private static boolean initialized = false;
    public void init(String confYingqizhilian, String confTopESA) throws IOException, CertApiException {
        //初始化不可重复
        if (initialized)
            return;
        //在此处设置配置文件的路径
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String configFilePath = path + confTopESA;
        FileInputStream fis = new FileInputStream(new File(configFilePath));
        byte[] buf = new byte[fis.available()];
        fis.read(buf);
        fis.close();
        String json = new String(buf, "UTF-8");
        String newJson = json.replace(confYingqizhilian, path+confYingqizhilian);
        TCA.config(newJson);
        initialized = true;
    }
}