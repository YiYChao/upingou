package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果类
 * @author YiChao
 *
 */
public class PageResult<E> implements Serializable {

	private long total;	// 总记录数
	private List<E> rows;	// 当前页记录
	
	public PageResult(long total, List<E> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	
	public long getTotal() {
		return total;
	}
	
	public void setTotal(long total) {
		this.total = total;
	}
	
	public List<E> getRows() {
		return rows;
	}
	
	public void setRows(List<E> rows) {
		this.rows = rows;
	}
}
