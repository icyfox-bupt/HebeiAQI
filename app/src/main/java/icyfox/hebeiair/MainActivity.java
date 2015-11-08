package icyfox.hebeiair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.icyfox.hebeiair.R;
import com.umeng.update.UmengUpdateAgent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	private TextView tv_time;
	private List<CityData> cdList;
	private List<Node> nodeList;
	private GridView gv;
	private View ll,pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv_time = (TextView)findViewById(R.id.tv_time);
		gv = (GridView)findViewById(R.id.gv);
		ll = findViewById(R.id.ll);
		pb = findViewById(R.id.pb1);
		cdList = new ArrayList<>();
		nodeList = new ArrayList<>();
		new GetSource().execute();

        UmengUpdateAgent.update(this);
	}

	class GetSource extends AsyncTask<String, Integer, String>{

		@Override
		protected void onPreExecute() {
			pb.setVisibility(View.VISIBLE);
			ll.setVisibility(View.GONE);
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
				return Util.HttpGet("http://121.28.49.85:8080/datas/hour/130000.xml");
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			//建立一个解析器
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
				InputStream is = new ByteArrayInputStream(result.getBytes());
				Document document = builder.parse(is);
				Element element = document.getDocumentElement();
				//获得所有的Citys节点数据
				NodeList cityList = element.getElementsByTagName("Citys");
				
				//获得mapstitle数据，并分解为两部分
				NodeList title = element.getElementsByTagName("MapsTitle");
				String text1 = title.item(0).getTextContent();
				String t[] = text1.split("\\(");
				text1 = t[0];
				t = t[1].split(",");
				tv_time.setText(text1+ "\n" +t[0]);
				
				Element citys = (Element)cityList.item(0);
				NodeList city = citys.getChildNodes();
				
				for (int i=0;i < city.getLength();i++){
					//此时的city节点的item上，有的是一个城市的所有数据
					Node node = city.item(i);
					if (node.getNodeName().equalsIgnoreCase("city")) { //这是一个有效的节点
						CityData cd = new CityData(node);
						nodeList.add(node);
						cdList.add(cd);
					}
				}


			} catch (Exception e) {
				e.printStackTrace();
			}

			gv.setAdapter(new gvAdapter(cdList));
			gv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					Intent it = new Intent(MainActivity.this, CityActivity.class);
					it.putExtra("node", cdList.get(position));
					startActivity(it);
				}
			});
			pb.setVisibility(View.GONE);
			ll.setVisibility(View.VISIBLE);
		}
		
	}
	
	class gvAdapter extends BaseAdapter{

		//Adapter的数据源，根据这个数据来填充网格列表
		List<CityData> cdList;
		
		public gvAdapter(List<CityData> cdList) {
			super();
			this.cdList = cdList;
		}

		//根据数据的多少返回相应的数值
		@Override
		public int getCount() {
			return cdList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//解析之前写好的每个网格的布局
			convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.gv, null);
			//找到布局中的元素，和布局的背景
			TextView tv_city = (TextView)convertView.findViewById(R.id.tv_city);
			TextView tv_aqi = (TextView)convertView.findViewById(R.id.tv_aqi);
			View bg = convertView.findViewById(R.id.bg);
			//根据数据填充每个格子的内容和背景色
			tv_city.setText(cdList.get(position).name);
			tv_aqi.setText(cdList.get(position).aqi);
			bg.setBackgroundColor(Color.parseColor(cdList.get(position).color));
			return convertView;
		}
		
	}
}
