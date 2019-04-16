package me.zohar.lottery.statisticalanalysis.vo;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.statisticalanalysis.domain.MonthStatistical;
import me.zohar.lottery.statisticalanalysis.domain.TodayStatistical;
import me.zohar.lottery.statisticalanalysis.domain.TotalStatistical;

@Data
public class IndexStatisticalVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double tradeAmount;

	private Long paidOrderNum;

	private Long orderNum;
	
	public static IndexStatisticalVO convertForTotal(TotalStatistical statistical) {
		if (statistical == null) {
			return null;
		}
		IndexStatisticalVO vo = new IndexStatisticalVO();
		BeanUtils.copyProperties(statistical, vo);
		return vo;
	}
	
	public static IndexStatisticalVO convertForMonth(MonthStatistical statistical) {
		if (statistical == null) {
			return null;
		}
		IndexStatisticalVO vo = new IndexStatisticalVO();
		BeanUtils.copyProperties(statistical, vo);
		return vo;
	}
	
	public static IndexStatisticalVO convertForToday(TodayStatistical statistical) {
		if (statistical == null) {
			return null;
		}
		IndexStatisticalVO vo = new IndexStatisticalVO();
		BeanUtils.copyProperties(statistical, vo);
		return vo;
	}

}
