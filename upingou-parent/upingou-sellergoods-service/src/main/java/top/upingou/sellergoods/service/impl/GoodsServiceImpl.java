package top.upingou.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import top.upingou.mapper.TbBrandMapper;
import top.upingou.mapper.TbGoodsDescMapper;
import top.upingou.mapper.TbGoodsMapper;
import top.upingou.mapper.TbItemCatMapper;
import top.upingou.mapper.TbItemMapper;
import top.upingou.mapper.TbSellerMapper;
import top.upingou.pojo.TbBrand;
import top.upingou.pojo.TbGoods;
import top.upingou.pojo.TbGoodsDesc;
import top.upingou.pojo.TbGoodsExample;
import top.upingou.pojo.TbGoodsExample.Criteria;
import top.upingou.pojo.TbItem;
import top.upingou.pojo.TbItemCat;
import top.upingou.pojo.TbItemExample;
import top.upingou.pojo.TbSeller;
import top.upingou.pojogroup.Goods;
import top.upingou.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbBrandMapper brandMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbSellerMapper sellerMapper;
	
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult<TbGoods> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult<TbGoods>(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		goods.getGoods().setAuditStatus("0");	// 设置成为申请状态
		goodsMapper.insert(goods.getGoods());
		
		goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());	// 设置Id
		goodsDescMapper.insert(goods.getGoodsDesc());	// 新增商品扩展描述记录
		
		saveItemList(goods);	// 插入商品SKU列表数据
	}
	/**
	 * <p>Title: setItemValus</p>
	 * <p>Description: 设置商品的属性</p>
	 * <p>CreateDate:2019年3月6日 下午8:09:10</p>
	 * @param item	商品实体
	 * @param goods	封装的组合实体
	 */
	private void setItemValus(TbItem item, Goods goods) {
		item.setGoodsId(goods.getGoods().getId());//商品SPU编号
		item.setSellerId(goods.getGoods().getSellerId());//商家编号
		item.setCategoryid(goods.getGoods().getCategory3Id());//商品分类编号（3级）
		item.setCreateTime(new Date());//创建日期
		item.setUpdateTime(new Date());//修改日期 
		
		//品牌名称
		TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
		item.setBrand(brand.getName());
		//分类名称
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
		item.setCategory(itemCat.getName());
		
		//商家名称
		TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
		item.setSeller(seller.getNickName());
		
		//图片地址（取spu的第一个图片）
		List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class) ;
		if(imageList.size()>0){
			item.setImage ( (String)imageList.get(0).get("url"));
		}
	}
	
	/**
	 * <p>Title: saveItemList</p>
	 * <p>Description: 将数据插入SKU列表</p>
	 * <p>CreateDate:2019年3月7日 下午4:28:44</p>
	 * @param goods 组合商品类
	 */
	private void saveItemList(Goods goods) {
		if("1".equals(goods.getGoods().getIsEnableSpec())) {
			for(TbItem item : goods.getItemList()) {
				//设置标题
				String title = goods.getGoods().getGoodsName();
				Map<String,Object> specMap = JSON.parseObject(item.getSpec());
				for(String key : specMap.keySet()) {
					title += " " + specMap.get(key);
				}
				item.setTitle(title);
				setItemValus(item,goods);
				itemMapper.insert(item);
			}
		}else {
			TbItem item=new TbItem();
			item.setTitle(goods.getGoods().getGoodsName());//商品KPU+规格描述串作为SKU名称
			item.setPrice( goods.getGoods().getPrice() );//价格			
			item.setStatus("1");//状态
			item.setIsDefault("1");//是否默认			
			item.setNum(99999);//库存数量
			item.setSpec("{}");			
			setItemValus(item,goods);					
			itemMapper.insert(item);
		}
	}
	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		goods.getGoods().setAuditStatus("0");	// 设置未审核状态
		goodsMapper.updateByPrimaryKey(goods.getGoods());	// 保存商品表
		
		goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());	// 保存商品扩展表信息
		
		// 删除原有SKU列表数据
		TbItemExample example = new TbItemExample();
		top.upingou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getGoods().getId());
		itemMapper.deleteByExample(example);
		
		// 添加新的SKU列表数据
		saveItemList(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
		// 设置商品基本信息
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setGoods(tbGoods);
		// 设置商品详情
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(tbGoodsDesc);
		// 查询SKU列表
		TbItemExample example = new TbItemExample();
		top.upingou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);	// 设置查询条件，商品id
		List<TbItem> itemList = itemMapper.selectByExample(example);
		goods.setItemList(itemList);
		return goods;
	}

	/**
	 * 批量进行逻辑删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setIsDelete("1");	// 逻辑删除
			goodsMapper.updateByPrimaryKey(tbGoods);
		}		
	}
	
	
	@Override
	public PageResult<TbGoods> findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		criteria.andIsDeleteIsNull();	// 非删除状态的商品
		
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
//				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult<TbGoods>(page.getTotal(), page.getResult());
	}

	@Override
	public void updateStatus(Long[] ids, String status) {
		for(Long id : ids) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
		
	}
	
}
