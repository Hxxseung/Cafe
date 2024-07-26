package cafe;

import java.sql.*;
import java.util.*;

public class MenuDAO {
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	
	private String url, user, pass;
	
	public MenuDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이버 검색 성공!!");
		}catch(ClassNotFoundException e) {
			System.err.println("드라이버 검색 실패!!");
		}
		url = "jdbc:oracle:thin:@localhost:1521:xe";
		user = "fin02";
		pass = "fin02";
	}
	
	//메뉴이름과 메뉴 가격넣기
	public MenuDTO getMenu(String menu) {
		try {
			con = DriverManager.getConnection(url, user, pass);
			String sql = "select menu,price from menujava where menu = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, menu);
			rs = ps.executeQuery();
			if (rs.next()) {
				MenuDTO dto = new MenuDTO();
				dto.setMenu(rs.getString("menu"));
				dto.setPrice(rs.getInt("price"));
				return dto;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//주문현황에 나타내기 위한 모든 값을 받아오는 배열
	public ArrayList<MenuDTO> MenuAll() {
		ArrayList<MenuDTO> list = new ArrayList<>();
		try {
			con = DriverManager.getConnection(url, user, pass);
			String sql = "select * from menujava";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				dto.setMenu(rs.getString("menu"));
				dto.setCof(rs.getInt("cof"));
				dto.setNum(rs.getInt("num"));
				dto.setPrice(rs.getInt("price"));
				dto.setTemp(rs.getString("temp"));
				dto.setDensity(rs.getString("density"));
				dto.setTake(rs.getString("take"));
				dto.setIce(rs.getString("ice"));
				list.add(dto);
			}
			return list;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//주문삭제
	public int deleteMenu(String name) {
		try {
			con = DriverManager.getConnection(url, user, pass);
			String sql = "delete from menujava where name = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			int res = ps.executeUpdate();
			return res;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//핫,아이스 
	public int updateTemp(MenuDTO dto) {
		try {
			con = DriverManager.getConnection(url, user, pass);
			String sql = "update Menujava set temp=? where name=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, dto.getTemp());
			ps.setString(2, dto.getMenu());
			int res = ps.executeUpdate();
			return res;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}	
	
	//연하게 기본 진하게 변경
	public int updateDensity(MenuDTO dto) {
		try {
			con = DriverManager.getConnection(url, user, pass);
			String sql = "update Menujava set density=? where name=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, dto.getDensity());
			ps.setString(2, dto.getMenu());
			int res = ps.executeUpdate();
			return res;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//얼음 적게,기본,많이 변경
	public int updateIce(MenuDTO dto) {
		try {
			con = DriverManager.getConnection(url, user, pass);
			String sql = "update Menujava set ice=? where name=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, dto.getIce());
			ps.setString(2, dto.getMenu());
			int res = ps.executeUpdate();
			return res;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	

	//수량 변경
	public int updateNum(MenuDTO dto) {
		try {
			con = DriverManager.getConnection(url, user, pass);
			String sql = "update Menujava set num=? where name=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, dto.getNum());
			ps.setString(2, dto.getMenu());
			int res = ps.executeUpdate();
			return res;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
}
