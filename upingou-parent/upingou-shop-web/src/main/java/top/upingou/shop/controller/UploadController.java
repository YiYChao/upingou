package top.upingou.shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.UResult;
import top.upingou.util.FastDFSClient;

/**
* @author YiChao
* @version 创建时间：2019年3月5日 上午10:33:19
* 说明: 图片上传控制器
*/
@RestController
public class UploadController {
	
	@Value("${FILE_SERVER_URL}")
	private String file_server_url;	// 文件服务器IP地址

	@RequestMapping("/upload")
	public UResult uploadImage(MultipartFile file) {
		// 1、获取文件扩展名
		String originalFilename = file.getOriginalFilename();
		String extName = originalFilename.substring(originalFilename.lastIndexOf('.')+1);
		try {
			// 2、创建一个FastDFS客户端
			FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
			// 3、执行图片上传，获得路径
			String path = client.uploadFile(file.getBytes(), extName);
			// 4、凭借ip地址和路径，获得完整访问路径
			String url = file_server_url + path;
			return new UResult(true, url);
		} catch (Exception e) {
			e.printStackTrace();
			return new UResult(false,"上传失败");
		}
	}
}
