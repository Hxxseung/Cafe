package cafe;


import java.sql.*;
import java.util.*;

public class ItemDAO {
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	
	private String url, user, pass;
	
	public ItemDAO() {
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
	
	
	public MenuDTO getMenu(String menu) {
		try {
			con = DriverManager.getConnection(url, user, pass);
			String sql = "select no,menu,price from menujava where menu = ?";
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
}
