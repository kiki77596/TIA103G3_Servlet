package com.travel_order.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.ships_schedule.model.Ships_scheduleDAO_interface;
import com.ships_schedule.model.Ships_scheduleVO;

//控制中心,增刪改查的方法
public class Travel_orderDAO implements Travel_orderDAO_interface {
	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3307/CruiseShip?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	private static final String INSERT_STMT = "INSERT INTO travel_order ( memb_id, ship_id, coup_id, trav_orde_status, room_amount, trav_orde_amount)VALUES (?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT * FROM travel_order order by trav_orde_id";
	private static final String GET_ONE_STMT = "SELECT trav_orde_id, memb_id, ship_id, coup_id, trav_orde_status, room_amount, trav_orde_amount FROM travel_order where trav_orde_id = ?";
	private static final String DELETE = "DELETE FROM travel_order where trav_orde_id = ?";
	private static final String UPDATE = "UPDATE travel_order set  memb_id=?, ship_id=?, coup_id=?, trav_orde_status=? ,room_amount=? ,trav_orde_amount=? where trav_orde_id = ?";

	@Override
	public void insert(Travel_orderVO travel_orderVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);// 載入驅動
			con = DriverManager.getConnection(url, userid, passwd);// 建立連線
			System.out.println("連線成功");

			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setInt(1, travel_orderVO.getMemb_id());
			pstmt.setInt(2, travel_orderVO.getShip_id());
			pstmt.setInt(3, travel_orderVO.getCoup_id());
			pstmt.setString(4, travel_orderVO.getTrav_orde_status());
			pstmt.setInt(5, travel_orderVO.getRoom_amount());
			pstmt.setString(6, travel_orderVO.getTrav_orde_amount());
			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException | ClassNotFoundException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public void update(Travel_orderVO travel_orderVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(UPDATE);

			pstmt.setInt(1, travel_orderVO.getMemb_id());
			pstmt.setInt(2, travel_orderVO.getShip_id());
			pstmt.setInt(3, travel_orderVO.getCoup_id());
			pstmt.setString(4, travel_orderVO.getTrav_orde_status());
			pstmt.setInt(5, travel_orderVO.getRoom_amount());
			pstmt.setString(6, travel_orderVO.getTrav_orde_amount());
			pstmt.setInt(7, travel_orderVO.getId());//注意PK是放在最後!!!
			int i = pstmt.executeUpdate();
			System.out.print("更新" + i +"筆資料");
			// Handle any driver errors
		} catch (SQLException | ClassNotFoundException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public void delete(Integer id) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, id);

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException | ClassNotFoundException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public Travel_orderVO findByPrimaryKey(Integer id) {

		Travel_orderVO travel_orderVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// get("要打的跟SQL欄位同名")
				travel_orderVO = new Travel_orderVO();
				travel_orderVO.setId(rs.getInt("trav_orde_id"));
				travel_orderVO.setMemb_id(rs.getInt("memb_id"));
				travel_orderVO.setShip_id(rs.getInt("ship_id"));
				travel_orderVO.setCoup_id(rs.getInt("coup_id"));
				travel_orderVO.setTrav_orde_status(rs.getString("trav_orde_status"));
				travel_orderVO.setRoom_amount(rs.getInt("room_amount"));
				travel_orderVO.setTrav_orde_amount(rs.getString("trav_orde_amount"));
				System.out.print("查詢成功");
			}

			// Handle any driver errors
		} catch (SQLException | ClassNotFoundException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return travel_orderVO;
	}

	@Override
	public List<Travel_orderVO> getAll() {
		List<Travel_orderVO> list = new ArrayList<Travel_orderVO>();
		Travel_orderVO travel_orderVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// get("要打的跟SQL欄位同名")
				travel_orderVO = new Travel_orderVO();
				travel_orderVO.setId(rs.getInt("trav_orde_id"));
				travel_orderVO.setMemb_id(rs.getInt("memb_id"));
				travel_orderVO.setShip_id(rs.getInt("ship_id"));
				travel_orderVO.setCoup_id(rs.getInt("coup_id"));
				travel_orderVO.setTrav_orde_status(rs.getString("trav_orde_status"));
				travel_orderVO.setRoom_amount(rs.getInt("room_amount"));
				travel_orderVO.setTrav_orde_amount(rs.getString("trav_orde_amount"));
				list.add(travel_orderVO); // Store the row in the list
			}
			return list;
			// Handle any driver errors
		} catch (SQLException | ClassNotFoundException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}
		
}

	