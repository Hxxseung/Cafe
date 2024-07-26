package cafe;

import java.util.*;
import java.awt.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

public class CafeServer extends JFrame implements ActionListener, Runnable{
	private Container con;
	
	private JMenuBar mb = new JMenuBar();
	private JMenu pay = new JMenu("주문현황");
	private JButton item = new JButton("매출현황");
	
	private JMenuItem p_01 = new JMenuItem("대기중");
	private JMenuItem p_02 = new JMenuItem("주문완료");

	
	
	private ArrayList<Hashtable<String, Integer>> list = new ArrayList<>();	//미결재
	private ArrayList<Hashtable<String, Integer>> list2 = new ArrayList<>();//결재

	
	private JPanel jp = new JPanel();
	private JTextArea jta;
	private JLabel[] order_list;
	private JButton[] pay_jb;
	private JPanel[] p;
	private JLabel[] lb;
	
	private ServerSocket ss;
	private Socket soc;
	private ObjectInputStream ois;
	
	private Menu[] menu = Menu.values();

	private Hashtable<String, Integer> item1 = new Hashtable<>();	//전체
	
	
	private JTabbedPane jtp = new JTabbedPane();
	private JPanel sort_p = new JPanel();
	
	private WaitDialog dlg = new WaitDialog(null,"",false);
	
	public void init() {
		this.setJMenuBar(mb);
		mb.add(pay);
		pay.add(p_01);			p_01.addActionListener(this);
		pay.add(p_02);			p_02.addActionListener(this);
		mb.add(item);			item.addActionListener(this);
		
		
		
		con = this.getContentPane();
		con.setLayout(new BorderLayout());
		con.add("Center", jp);
		
		for(int i=0; i<menu.length; ++i) {
			if (menu[i].getSort().equals("coffee")||menu[i].getSort().equals("noncoffee")) {
				item1.put(menu[i].name(), 0);
			}
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public String getOrder(Hashtable<String, Integer> ht) {
		
		String order = "";
		Set<String> set = ht.keySet();
		String[] key = new String[set.size()];
		set.toArray(key);
		for(int j=0; j<key.length-1; ++j) {
			order += key[j] + " : " + ht.get(key[j]) + ", ";
		}
		order += key[key.length-1] + " : " + ht.get(key[key.length-1] );
		return order;
	}
	
	public void payList() {
		con.remove(jp);
		jp = new JPanel();		//주문내역서가 보일 전체 Panel
		int size = list.size();	//미결제 주문서의 갯수
		jp.setLayout(new GridLayout(size, 1));//미결제 주문서갯수만큼 칸을 나눈다
		p = new JPanel[size];		//각 칸의 Panel
		order_list = new JLabel[size];	//각자의 주문내역서
		pay_jb = new JButton[size];//각자 주문내역서의 결제버튼	

		for(int i=0; i<size; ++i) {
			p[i] = new JPanel();
			p[i].setLayout(new BorderLayout());
			order_list[i] = new JLabel(getOrder(list.get(i)) , JLabel.LEFT);
			pay_jb[i] = new JButton("주문 완료");
			p[i].add("Center", order_list[i]);
			p[i].add("East", pay_jb[i]);
			pay_jb[i].addActionListener(this);
			jp.add(p[i]);
		}
		con.add("Center", jp);
		con.validate();
		
	}
	
	public void payList2() {
		con.remove(jp);
		jp = new JPanel();		//주문내역서가 보일 전체 Panel
		int size = list2.size();	//미결제 주문서의 갯수
		jp.setLayout(new GridLayout(size, 1));//미결제 주문서갯수만큼 칸을 나눈다
		p = new JPanel[size];		//각 칸의 Panel
		order_list = new JLabel[size];	//각자의 주문내역서
		lb = new JLabel[size];//각자 주문완료	
		
		for(int i=0; i<size; ++i) {
			p[i] = new JPanel();
		
			p[i].setLayout(new BorderLayout());

			order_list[i] = new JLabel(getOrder(list2.get(i)) , JLabel.LEFT);
			lb[i] = new JLabel("주문 완료",JLabel.LEFT);
		
			p[i].add("Center", order_list[i]);
			p[i].add("East", lb[i]);
			jp.add(p[i]);
		}
		con.add("Center", jp);
		con.validate();
		
	}

	
	
	public CafeServer(String title) {
		super(title);
		this.init();
		this.setSize(600, 400);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth()/2 - this.getWidth()/2);
		int ypos = (int)(screen.getHeight()/2 - this.getHeight()/2);
		this.setLocation(xpos, ypos);
		this.setResizable(false);
		this.setVisible(true);
		dlg.setView(true);
		try {
			ss = new ServerSocket(12345);
			soc = ss.accept();
			new Thread(this).start();
		}catch(Exception e) {}
	}
	
	public static void main(String[] args) {
		new CafeServer("주문내역-서버");
	}
	
	public void setTextArea(int i) {
		
		ArrayList<Hashtable<String, Integer>> textList = null;
		jta = new JTextArea("");
		switch(i) {
		case 1 :
			textList = list;
			break;
		case 2 :
			textList = list2;
			break;
		case 3 :
			
		}
		for(int j=0; j<textList.size(); ++j) {
			jta.append(getOrder(textList.get(j)) + "\n");
		}
		con.remove(jp);
		jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(jta);
		con.add("Center", jp);
		con.validate();
	}
	
	public void clearItem(Hashtable<String, Integer> item) {
		Enumeration<String> clear = item.keys();
		while(clear.hasMoreElements()) {
			String key = clear.nextElement();
			item.put(key, 0);
		}	
	}
	
	public void itemList() {
	    int tot = 0;
	    String name = null;
	    int price = 0;
	 
	    clearItem(item1);
	    
	    Hashtable<String, Integer> itemList = null;
	    jta = new JTextArea("");

	    
	            itemList = item1;
	            jta.append("전체 현황\n");
	            jta.append("메뉴\t갯수\t금액\n");
				jta.append("---------------------------------------------------------------------------\n");
	            for (Hashtable<String, Integer> ht : list2) {
	                Set<String> set = ht.keySet();
	                Iterator<String> it = set.iterator();
	                while (it.hasNext()) {
	                    String key = it.next();
	                    int value = ht.get(key);
	                    if (itemList.containsKey(key)) {
	                        itemList.put(key, itemList.get(key) + value);		
	                    } else {
	                        itemList.put(key, value);
	                    }
	                }
	            }
	            for (int i = 0; i < menu.length; i++) {
                    price = menu[i].getPrice();
	            }
	            // 각 메뉴별 주문 갯수만 표시
	            for (String key : itemList.keySet()) {
	                jta.append(key + "\t" + itemList.get(key)+"\t" +itemList.get(key)*price+ "\n");
	            }
	            
	            // 판매 누계
	            jta.append("\n판매 누계:\n");
	            for (Hashtable<String, Integer> ht : list2) {
	                Set<String> set = ht.keySet();
	                Iterator<String> it = set.iterator();
	                while (it.hasNext()) {
	                    String key = it.next();
	                    int value = ht.get(key);
	                    for (int j = 0; j < menu.length; j++) {
	                        name = menu[j].name();
	                        price = menu[j].getPrice();
	                        if (key.equals(menu[j].name())) {
	                            tot += value * price;
	                            jta.setEditable(false);
	                        }
	                    }
	                }
	            }
	            // 총 판매 금액만 표시
	            jta.append("총 판매 금액 : " + tot + "원\n");
	            jta.setEditable(false);
	            
	    con.remove(jp);
	    jp = new JPanel();
	    jp.setLayout(new BorderLayout());
	     
	    jp.add(jta);
	    con.add("Center", jp);
	    con.validate();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == p_01) {
			payList();
		}else if (e.getSource() == p_02){
			//setTextArea(2);
			payList2();
		}else if (e.getSource() == item){
			itemList();
			dlg.setView(true);
		}else {
			for(int i=0; i<list.size(); ++i) {
				if (e.getSource() == pay_jb[i]) {
					//dlg.clearText(list.get(i));
					list2.add(list.get(i));
					dlg.clearData(list.get(i));
					list.remove(i);
					//dlg.list(dlg.remove(list.get(i)));
					dlg.clearNumber();
					dlg.waitNumber();
					break;
				}
			}
			payList();
		}
	}

	@Override
	public void run() {
		// 클라이언트로부터 주문내역서를 받아서 ArrayList에 저장한다.
		try {
			while(true) {
				ois = new ObjectInputStream(soc.getInputStream());
				Object obj = ois.readObject();
				Hashtable<String, Integer> ht = (Hashtable)obj;
				list.add(ht);
				dlg.payData(ht);
				dlg.waitNumber();
				payList();	//미결재 주문 리스트 작성하는 곳
				//dlg.setView(true);
			}
		}catch(Exception e) {}
		
	}
}