package com.order.model;

import java.util.List;
import com.google.gson.annotations.Expose;

import kotlin.Pair;

public class WebOrderDetail {

	@Expose
	List<Pair<Integer,OrderDetailVo>> prods;

	@Expose
	OrderListVo order;

	public WebOrderDetail() {}

	public WebOrderDetail(List<Pair<Integer,OrderDetailVo>> prods, OrderListVo order) {
		super();
		this.prods = prods;
		this.order = order;
	}
	public List<Pair<Integer,OrderDetailVo>> getProds() {
		return prods;
	}
	public OrderListVo getOrder() {
		return order;
	}
	public void setProds(List<Pair<Integer,OrderDetailVo>> prods) {
		this.prods = prods;
	}
	public void setOrder(OrderListVo order) {
		this.order = order;
	}
}
