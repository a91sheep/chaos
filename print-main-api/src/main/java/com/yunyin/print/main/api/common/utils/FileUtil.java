package com.yunyin.print.main.api.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.store59.kylin.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/23
 * @since 1.0
 */
public class FileUtil {
    /**
     * 上传文件到阿里云OSS服务器
     *
     * @param file
     * @param aliyunOSSClient
     * @param bucketName
     * @return
     */
    public static String upload(MultipartFile file, String contentType, String name, OSSClient aliyunOSSClient, String bucketName) {
        InputStream content = null;
        try {
            // 获取指定文件的输入流
            content = file.getInputStream();

            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();

            // 必须设置ContentLength
            meta.setContentLength(file.getSize());
            meta.setContentType(contentType);

            // 生成Object key （域名＋文件哈希值＋文件后缀）
            InputStream contentCopy = file.getInputStream();
            String key = DigestUtils.md5Hex(contentCopy) + "." + name.substring(name.lastIndexOf(".") + 1);
            // 上传Object
            PutObjectResult result = aliyunOSSClient.putObject(bucketName, key, content, meta);
            String etag = result.getETag();

            if (StringUtil.isEmpty(etag) || !key.split("\\.")[0].equals(etag.toLowerCase())) {
                return null;
            } else {
                System.out.println("*****key:" + key);
                return key;
            }

        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (content != null) {
                    content.close();
                }
            } catch (IOException e) {
                return null;
            }
        }

    }

    public static OSSObject getOssObject(OSSClient aliyunOSSClient, String bucketName, String key) throws OSSException, ClientException {

        return aliyunOSSClient.getObject(new GetObjectRequest(bucketName, key));
    }
}
