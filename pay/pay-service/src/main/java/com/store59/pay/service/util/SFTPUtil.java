/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.store59.pay.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.store59.kylin.utils.JsonUtil;
import com.store59.kylin.utils.StringUtil;
import com.store59.pay.service.config.WangshangConfig;

/**
 * 
 * @author beangou
 *
 */

public class SFTPUtil {
	
	private WangshangConfig wangshangConfig;
	
    public static Logger LOGGER = LoggerFactory.getLogger(SFTPUtil.class);

    private Session     sshSession;

    private ChannelSftp sftpClient = null;

    public SFTPUtil(WangshangConfig wangshangConfig) {
    	this.wangshangConfig = wangshangConfig;
    }

    public ChannelSftp connectFtpServer() throws SocketException, IOException {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(wangshangConfig.getSftpUserName(), wangshangConfig.getSftpIp(), wangshangConfig.getSftpPort());
            sshSession = jsch.getSession(wangshangConfig.getSftpUserName(), wangshangConfig.getSftpIp(), wangshangConfig.getSftpPort());
            sshSession.setPassword(wangshangConfig.getSftpPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect(5000);
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return null;
        }
        this.sftpClient = sftp;
        try {
            sftpClient.setFilenameEncoding("UTF-8");
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return sftp;
    }

    /**
     * 断开与对方FTP Server的连接
     * 
     * @throws IOException
     */
    public void disconnectFtpServer() throws IOException {
        sftpClient.disconnect();
        sshSession.disconnect();
    }

    public boolean changeWorkingDirectory(String fileDir) throws IOException {
        try {
            if (fileDir.trim().length() > 0) {
                sftpClient.cd(fileDir);
            } else {
                sftpClient.cd(sftpClient.getHome());
            }
            return true;
        } catch (SftpException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    public InputStream downloadResultFile(String batchTransNo, String dateStr) throws Exception {
    	InputStream in = null;
        if(dateStr == null) {
        	// 留个外部调用入口
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        	dateStr = sdf.format(new Date());
        }
        String path = wangshangConfig.getDownloadPath()+wangshangConfig.getAccountId()+"/"+dateStr;// 进入FTP服务器指定目录
        String resultFileName = "";
        try {
        	connectFtpServer(); // 连接FTP服务器
            changeWorkingDirectory(path); // 进入FTP服务器指定目录
			@SuppressWarnings("rawtypes")
			Vector fileVec = sftpClient.ls("./");
        	for (Object fileName : fileVec) {
        		LsEntry lsEntry = (LsEntry)fileName;
				if(lsEntry.getFilename().indexOf(batchTransNo) > -1) {
					resultFileName = lsEntry.getFilename();
					break;
				}
			}
        	LOGGER.info("resultFileName = {}" + resultFileName);
        	if(StringUtil.isEmpty(resultFileName)) {
        		LOGGER.info("没找到结果文件,batchTransNo={}", batchTransNo);
        		return null;
        	}
        	String localPath = this.getClass().getClassLoader().getResource("").getPath();
            sftpClient.get(resultFileName, localPath+"files/download/" + resultFileName);
            in = new FileInputStream(new File(localPath+"files/download/" + resultFileName));
        } finally{
        	disconnectFtpServer();
        }
        return in;
    }

    public String upLoadFile(InputStream in, String bizNo) throws Exception {
        String result = "";
        try{
        	connectFtpServer(); // 连接FTP服务器
        	// 进入FTP服务器指定目录
          	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
          	String dateStr = sdf.format(new Date());
          	String uploadPath = wangshangConfig.getUploadPath()+wangshangConfig.getAccountId();
          	String targetDir = uploadPath+"/"+dateStr;
          	boolean changeResult = changeWorkingDirectory(targetDir); // 进入FTP服务器指定目录
          	if(!changeResult) {
          		// 如果进入指定目录失败，应该是该目录不存在，需要重新新建一个目录
          		if (!changeWorkingDirectory(uploadPath)) {
          			LOGGER.info("{}不存在，需要新建目录！", uploadPath);
          			// 如果uploadpath(/upload/H2H/batchPay/)也不存在
          			String[] uploadDirs = uploadPath.split("/");
          			if(!changeWorkingDirectory(uploadDirs[1])) {
          				// 先进uploadpath目录
          				sftpClient.mkdir(uploadDirs[1]);
          				changeWorkingDirectory(uploadDirs[1]);
          			}
          			if(!changeWorkingDirectory(uploadDirs[2])) {
          				// 进H2H目录
          				sftpClient.mkdir(uploadDirs[2]);
          				changeWorkingDirectory(uploadDirs[2]);
          			}
          			if(!changeWorkingDirectory(uploadDirs[3])) {
          				// 进batchPay目录
          				sftpClient.mkdir(uploadDirs[3]);
          				changeWorkingDirectory(uploadDirs[3]);
          			}
          			if(!changeWorkingDirectory(uploadDirs[4])) {
          				// 进AccountId目录
          				sftpClient.mkdir(uploadDirs[4]);
          				changeWorkingDirectory(uploadDirs[4]);
          			}
          		}
          		// 进日期目录
          		sftpClient.mkdir(dateStr);
      			changeWorkingDirectory(dateStr);
          	}
          	String dest = "h2h_batchPay_"+wangshangConfig.getAccountId()+"_" + bizNo + ".xls";
          	sftpClient.put(in, dest);
          	LOGGER.info("put file=>h2h_batchPay_"+wangshangConfig.getAccountId()+"_{}.xls", bizNo);
        }finally {
        	disconnectFtpServer();
        }
        return result;
    }
    
}