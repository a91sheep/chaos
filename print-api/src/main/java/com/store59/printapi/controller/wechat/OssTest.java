package com.store59.printapi.controller.wechat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.AccessControlList;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.ListBucketsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.store59.kylin.utils.JsonUtil;

public class OssTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
		//new
		String bucketName="print-identification-private";
		String accessKeyId = "dPL2EYrJZggLCN7B";
		String accessKeySecret = "7EzhfiGJqc5ha5Vf5P9xn3HrR7wIRy";
		//old
//		String accessKeyId = "vxHYcjeAQgjiUa1H";
//		String accessKeySecret = "I2TIn74UydTjOM7XMgsyqktsOXn7ke";

		// 创建ClientConfiguration实例
//		ClientConfiguration conf = new ClientConfiguration();

		// 设置OSSClient使用的最大连接数，默认1024
//		conf.setMaxConnections(200);
		// 设置请求超时时间，默认50秒
//		conf.setSocketTimeout(10000);
		// 设置失败请求重试次数，默认3次
//		conf.setMaxErrorRetry(5);
		
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
//		listBucketsRequest.setPrefix("print");
		List<Bucket> buckets = ossClient.listBuckets(listBucketsRequest).getBucketList();
		
		for (Bucket bucket : buckets) {
		    System.out.println(" - " + bucket.getName());
		}
		
		AccessControlList acl = ossClient.getBucketAcl(bucketName);
		// bucket权限
		System.out.println(acl.toString());
		//文件列表
		ObjectListing objectList=ossClient.listObjects(bucketName);
		System.out.println(objectList.getObjectSummaries().size());
		System.out.println(objectList.getCommonPrefixes());
		// 使用访问OSS
		 // 获取指定文件的输入流
		File file=new File("/Users/haowt/59store/0e8282518292a73e3d0d199b9cc33293.jpeg");
        InputStream content = new FileInputStream(file);

        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();

        // 必须设置ContentLength
        meta.setContentLength(file.length());
        meta.setContentType("image/png");

        // 生成Object key （域名＋文件哈希值＋文件后缀）
        InputStream contentCopy = new FileInputStream(file);
        String key = DigestUtils.md5Hex(contentCopy) + ".png";
        // 上传Object
        PutObjectResult result = ossClient.putObject("print-identification-private", key, content, meta);
        System.out.println(JsonUtil.getJsonFromObject(result));
        System.out.println(key);
        
        // 设置URL过期时间为1小时
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);

        // 生成临时URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        System.out.println(url.toString());
        
        //获取object
        OSSObject ossObject=ossClient.getObject(bucketName, key);
        
        InputStream objStream = ossObject.getObjectContent();
        File objFile=new File("/Users/haowt/59store/test.png");
        if(!objFile.exists())objFile.createNewFile();
        inputstreamtofile(objStream,objFile);
        objStream.close();
	}
	
	public static void inputstreamtofile(InputStream ins,File file) throws Exception{
		   OutputStream os = new FileOutputStream(file);
		   int bytesRead = 0;
		   byte[] buffer = new byte[8192];
		   while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
		      os.write(buffer, 0, bytesRead);
		   }
		   os.close();
		   ins.close();
		}

}
