package com.order.model;

import java.util.List;
import javax.persistence.Query;

import org.hibernate.Session;

import com.outherutil.Dao;
import com.outherutil.HibernateUtil;
public class OrderDaoImpl implements Dao{

	public void insert(OrderListVo data) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.save(data);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}

	}

	@Override
	public <T> void update(int id, T data) {
		Session session  = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			OrderListVo orgOrder = session.get(OrderListVo.class, id);
			if(orgOrder!=null) {
				OrderListVo newOrder = (OrderListVo)data;
				orgOrder.setStatus(newOrder.getStatus());
				session.update(orgOrder);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}
	public List<OrderListVo> getByMembId(int id){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		String hql = "FROM OrderListVo WHERE membId = ?0";
		 Query query = session.createQuery(hql, OrderListVo.class);
		query.setParameter(0, id);

		@SuppressWarnings("unchecked")
		List<OrderListVo> list = (List<OrderListVo>)query.getResultList();

		session.getTransaction().commit();
		return list;
	}
	public List<OrderDetailVo> getOrderDetail(int orderid,int membid){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

        String hql = "FROM OrderListVo o JOIN FETCH o.orderDetails od JOIN FETCH od.prod WHERE o.orid = :orderId AND o.membId = :membId";

        Query query = session.createQuery(hql, OrderListVo.class);
        query.setParameter("orderId", orderid);
        query.setParameter("membId", membid);

        @SuppressWarnings("unchecked")
		List<OrderListVo>orders = query.getResultList();
        session.getTransaction().commit();

        return orders.get(0).getOrderDetails();
	}
	public OrderListVo getOrderDetail(int orderid){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

        String hql = "FROM OrderListVo o JOIN FETCH o.orderDetails od JOIN FETCH od.prod WHERE o.orid = :orderId";
        Query query = session.createQuery(hql, OrderListVo.class);
        query.setParameter("orderId", orderid);
        @SuppressWarnings("unchecked")
		List<OrderListVo>orders = query.getResultList();
        session.getTransaction().commit();

        return orders.get(0);
	}
	public List<OrderListVo> getAll(){
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
		    session.beginTransaction();
		    String hql = "SELECT ol FROM OrderListVo ol LEFT JOIN FETCH ol.orderDetails od LEFT JOIN FETCH od.prod";
		    List<OrderListVo> oList = session.createQuery(hql, OrderListVo.class).getResultList();
		    session.getTransaction().commit();
		    return oList;
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;
		}
	}
}
