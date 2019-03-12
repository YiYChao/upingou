package top.upingou.solr;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import top.upingou.mapper.TbItemMapper;
import top.upingou.pojo.TbItem;
import top.upingou.pojo.TbItemExample;
import top.upingou.pojo.TbItemExample.Criteria;

/**
 * 实现批量导入商品数据到solr索引库
 * @author YiChao
 *
 */
@Component
public class SolrUtil {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private SolrTemplate solrTemplate;
	
	public void importItemData() {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1"); //已审核的商品
		List<TbItem> itemList = itemMapper.selectByExample(example);
		
		for(TbItem item : itemList) {
			Map specMap= JSON.parseObject(item.getSpec());	//将spec字段中的json字符串转换为map
			item.setSpecMap(specMap);	//给带注解的字段赋值{"网络":"移动4G","机身内存":"32G"}

			System.out.println(item.getTitle());
		}
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil = context.getBean("solrUtil",SolrUtil.class);
		solrUtil.importItemData();
	}
}
