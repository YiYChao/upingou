package top.upingou.pojogroup;

import java.io.Serializable;
import java.util.List;

import top.upingou.pojo.TbSpecification;
import top.upingou.pojo.TbSpecificationOption;

/**
* @author YiChao
* @version 创建时间：2019年3月2日 上午10:24:23
* 说明: 组合规格实体类
*/
public class Specification implements Serializable {

	private TbSpecification specification;	//规格名称
	
	private List<TbSpecificationOption> specificationOptionList;	// 规格选项

	public TbSpecification getSpecification() {
		return specification;
	}

	public void setSpecification(TbSpecification specification) {
		this.specification = specification;
	}

	public List<TbSpecificationOption> getSpecificationOptionList() {
		return specificationOptionList;
	}

	public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
		this.specificationOptionList = specificationOptionList;
	}
	
}
