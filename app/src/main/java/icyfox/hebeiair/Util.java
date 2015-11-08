package icyfox.hebeiair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Util {

	public static String HttpGet(String url) throws ClientProtocolException, IOException {
		//新建一个默认的连接
		DefaultHttpClient client = new DefaultHttpClient();
		//新建一个Get方法
		HttpGet get = new HttpGet(url);
		//得到网络的回应
		HttpResponse response = client.execute(get);
		//获得的网页源代码（xml）
		String content = null;

		//如果服务器响应的是OK的话！
		if (response.getStatusLine().getStatusCode() == 200) {
			//以下是把网络数据分段读取下来的过程
			InputStream in = response.getEntity().getContent();
			byte[] data = new byte[1024];
			int length = 0;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			while ((length = in.read(data)) != -1) {
				bout.write(data, 0, length);
			}
			//最后把字节流转为字符串 转换的编码为utf-8.
			content = new String(bout.toByteArray(), "utf-8");
		}
		//返回得到的字符串 也就是网页源代码
		return content;
	}
}
