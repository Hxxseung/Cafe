package cafe;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

public class CafeGUI extends JFrame implements ActionListener {
	private Container con;
	private BorderLayout bl = new BorderLayout();
	
	//초기화면 구성
	private ImageIcon icon;
	private JScrollPane scrollPane;
	private JButton bt_start = new JButton("주문하기");
	
	//클릭시 해당 메뉴판으로 이동하기 위한 버튼
	private JPanel south_p = new JPanel();	//메뉴 이동 버튼 패널
	private RoundedButton coffee = new RoundedButton("COFFEE");
	private RoundedButton noncof = new RoundedButton("NONCOFFEE");

	//메뉴 버튼
	private JPanel center_p = new JPanel();
	private GridLayout center_p_gl = new GridLayout(2, 3);
	private JPanel[] pn_menu = new JPanel[12];
	private JLabel[] lb_coffee = new JLabel[6];	//커피메뉴 이름, 가격 라벨
	private JLabel[] lb_noncof = new JLabel[6];	//논커피메뉴 이름, 가격 라벨
	private ItemDTO[] bt_coffee = new ItemDTO[6];
	private ItemDTO[] bt_noncof = new ItemDTO[6];
	
	//결제창
	private JPanel east_p = new JPanel();
	private BorderLayout east_p_bl = new BorderLayout();
	private JLabel lb_title = new JLabel("주문 내역서", JLabel.CENTER);
	JPanel in_p = new JPanel();
	private JPanel in_p2 = new JPanel();
	private JPanel[] pn ;
	private JLabel[] lb ;	//주문별 라벨
	private JButton[] bt ;	//주문별 취소버튼
	 JTextArea ta = new JTextArea();	//총금액 
	private JButton order_bt = new JButton("주문하기");
	
	private JPanel panel;
	
	private MenuDTO menu = new MenuDTO();
	private ItemDAO itemDao = new ItemDAO();
	
	//옵션 다이얼로그
	private CoffeeDialog dlg;
	private NoncoffeeDialog dlg_v2;
	
	ArrayList<ItemDTO> list = new ArrayList<>();
	
	private InetAddress ia;
	private Socket soc;
	private ObjectOutputStream oos;

	
	//coffee 메뉴 버튼 메소드
	public void coffee(boolean visible) {
	    center_p.removeAll();
	    center_p.revalidate();
	    center_p.repaint();
	    
	    
	    for (int i = 0; i < 6; ++i) {
	    	lb_coffee[i] = new JLabel("", JLabel.CENTER);
	    	pn_menu[i] = new JPanel();
	        Image img = Toolkit.getDefaultToolkit().getImage("src/teamproject/img/image" + (i + 1) + ".jpg");
	        bt_coffee[i] = new ItemDTO(list.getNo(), list.getMenu(), list.getPrice(), img);
	        center_p.add(pn_menu[i]);
	        pn_menu[i].setLayout(new BorderLayout());
	        pn_menu[i].add("Center", bt_coffee[i]);
	        lb_coffee[i].setText(cof[i].name()+" : "+cof[i].getPrice()+"원");
	        lb_coffee[i].setFont(new Font("Plain", Font.BOLD , 15));
	        pn_menu[i].add("South", lb_coffee[i]);
	        bt_coffee[i].addActionListener(this);
	    }
	    this.setVisible(visible);
	}
	
	//noncoffee 메뉴 버튼 메소드
	public void noncoffee(boolean visible) {
	    center_p.removeAll();
	    center_p.revalidate();
	    center_p.repaint();
	    
	    Menu[] noncof = Menu.values();
	    for (int i = 6; i < 12; ++i) {
	    	lb_noncof[i-6] = new JLabel("", JLabel.CENTER);
	    	pn_menu[i] = new JPanel();
	        Image img = Toolkit.getDefaultToolkit().getImage("src/teamproject/img/image" + (i+1) + ".jpg");
	        bt_noncof[i-6] = new CafeButton(noncof[i].name(), noncof[i].getPrice(), noncof[i].getSort(), img);
	        center_p.add(pn_menu[i]);
	        pn_menu[i].setLayout(new BorderLayout());
	        pn_menu[i].add("Center", bt_noncof[i-6]);
	        lb_noncof[i-6].setText(noncof[i].name()+" : "+noncof[i].getPrice()+"원");
	        lb_noncof[i-6].setFont(new Font("Plain", Font.BOLD , 15));
	        pn_menu[i].add("South", lb_noncof[i-6]);
	        bt_noncof[i-6].addActionListener(this);
	    }
	    this.setVisible(visible);
	}
	
	  //주문내역서에 메뉴 넣는 메소드
		public void setTextArea() {
		    int tot = 0;
		    ta.setText("TOTAL: "); // JTextArea를 "TOTAL: "로 초기화하여 총 가격을 다시 표시하기 전에 초기화
		    if (list.size() == 0) {
		        ta.append("0원"); // 리스트에 아무것도 없으면 총 가격을 0원으로 표시
		        return;
		    }
		    //ArrayList에 저장된 값을 꺼내서 출력하기
		    for(CafeButton mb : list) {
		        tot += mb.getPrice();
		    }
		    ta.append(tot + "원");
		}
		 /*
		//주문내역서에 메뉴 넣는 메소드
		public void setTextArea(int num) {
			int tot = 0;
			//기본 출력
			if (num == 0) {
			    ta.setText("TOTAL: "); // JTextArea를 "TOTAL: "로 초기화하여 총 가격을 다시 표시하기 전에 초기화
			    if (list.size() == 0) {
			        ta.append("0원"); // 리스트에 아무것도 없으면 총 가격을 0원으로 표시
			        return;
			    }
			//버튼을 눌렀을 때 출력
		    }else {
			    for(CafeButton mb : list) {
			    	
			    	if (num<0) tot += mb.getPrice()*(-num);	//X버튼 클릭시
			    	else tot += mb.getPrice()*num;			//메뉴 버튼 클릭시
			    }
			    ta.setText("TOTAL: ");
			    ta.append(tot + "원");
		    }
		}
	*/
	//서버 전송 메소드
	public void serverSend() {
		Hashtable<String, Integer> ht = new Hashtable<>();//key - 음식이름, value - 주문갯수
		
		//for문은 음식 주문 내역을 항목별로 정리
		for(CafeButton cb : list) {
			if (ht.containsKey(cb.getName())) {	//동일한 이름이 있다면..
				int co = ht.get(cb.getName());
				co++;
				ht.put(cb.getName(), co);		//키가 값으면 value값이 수정
			}else {
				ht.put(cb.getName(), 1);
			}
		}
		try {
			oos = new ObjectOutputStream(soc.getOutputStream());
			oos.writeObject(ht);
			oos.flush();
		}catch(Exception e) {}
		
	}
	


	@Override
	public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == order_bt) {
	        serverSend();
	        list.clear();
	        setTextArea();
	        ta.setText("");
	        // in_p2 패널 초기화
	        in_p.removeAll();
	        in_p.revalidate(); 
	        in_p.repaint();
	    } else if (e.getSource() == coffee) {
	        this.noncoffee(false);
	        this.coffee(true);
	    } else if (e.getSource() == noncof) {
	        this.coffee(false);
	        this.noncoffee(true);
	    }else {
	        for (int i = 0; i < 12; ++i) {
	            if (i < 6 && e.getSource() == bt_coffee[i]) {
	            	final CafeButton selectedCoffee = bt_coffee[i];
	            	dlg = new CoffeeDialog(this, selectedCoffee, true);
	            	dlg.bt_order.addActionListener(dlg);
	            	dlg.setVisible(true);
	            	System.out.println(list);

	            }  else if (i >= 6 && e.getSource() == bt_noncof[i - 6]) {
	            	final CafeButton selectedNoncoffee = bt_noncof[i-6];
	            	dlg_v2 = new NoncoffeeDialog(this, selectedNoncoffee, true);
	            	dlg_v2.bt_order.addActionListener(dlg_v2);
	            	dlg_v2.setView(true);
	            	
	            	System.out.println(list);
	        }
	    }
	        
	    }
	}

	public void init() {
		con = this.getContentPane();
		con.setLayout(bl);
		con.add("North", south_p);
		south_p.setLayout(new FlowLayout());
		south_p.add(coffee);	south_p.add(noncof);
		coffee.addActionListener(this); noncof.addActionListener(this);
		con.add("Center", center_p);
		center_p.setLayout(center_p_gl);
		this.coffee(false);
		east_p.setPreferredSize(new Dimension(150, 400));
		con.add("East", east_p);
		east_p.setLayout(east_p_bl);
		east_p.add("North", lb_title);	
		lb_title.setBackground(Color.ORANGE); lb_title.setOpaque(true);	lb_title.setFont(new Font("Plain", Font.BOLD, 20));
		east_p.add("Center", in_p);
		east_p.add("South", in_p2);
		in_p2.setLayout(new GridLayout(2,1));
		in_p2.add(ta);	ta.setFont(new Font("Plain", Font.BOLD, 15));
		in_p2.add(order_bt);	order_bt.addActionListener(this);
		this.setTextArea();

		
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public CafeGUI(String title) {
		super(title);
		
		this.init();
		
		this.setSize(800, 550);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth()/2 - this.getWidth()/2);
		int ypos = (int)(screen.getHeight()/2 - this.getHeight()/2);
		this.setLocation(xpos, ypos);
		this.setResizable(false);
		this.setVisible(true);
		try {
			ia = InetAddress.getByName("localhost");
			soc = new Socket(ia, 12345);
		}catch(Exception e) {}
	}
	
	public static void main(String[] args) {
		new CafeGUI("커피시스템");
	}
}