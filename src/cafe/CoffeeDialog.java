package cafe;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class CoffeeDialog extends JDialog implements ActionListener {
	private Hashtable<Integer,String> table = new Hashtable<>();
	private MenuDTO menu = new MenuDTO();
	//매장 또는 포장
	private JPanel pn = new JPanel();
	private JLabel lb = new JLabel("매장 or 포장 ______________________________");
	private JPanel pn_ = new JPanel();
	private JButton bt_here = new JButton("매장");
	private JButton bt_take = new JButton("포장");
	private JLabel lb_blank2 = new JLabel("        ");	// 버튼 사이 공백
	//HOT or ICED 선택
	private JPanel pn_hoticed = new JPanel();
	private JLabel lb_hoticed = new JLabel("HOT or ICED ______________________________");
	private JPanel pn_1 = new JPanel();
	private JLabel lb_blank = new JLabel("        ");	// 버튼 사이 공백
	private JButton bt_hot = new JButton("HOT");
	private JButton bt_iced = new JButton("ICED");
	//샷 선택
	private JPanel pn_add = new JPanel();
	private JLabel lb_add = new JLabel("샷 선택 __________________________________");
	private JPanel pn_2 = new JPanel();
	private JButton bt_more = new JButton("진하게");
	private JButton bt_less = new JButton("연하게");
	private JButton bt_standard = new JButton("기본");
	//수량 선택
	private JPanel pn_num = new JPanel();
	private JLabel lb_num = new JLabel("수량 선택 ________________________________");
	private JPanel pn_3 = new JPanel();
	private JButton bt_minus = new JButton("-");
	private JButton bt_plus = new JButton("+");
	 JTextField tf_num = new JTextField("1");	//기본값 1
	 int count = 1;
	//주문 및 취소
	private JPanel pn_bt = new JPanel();
	JButton bt_order = new JButton("장바구니");
	private JButton bt_cancel = new JButton("취소");
	private JLabel lb_blank1 = new JLabel("        ");	// 버튼 사이 공백
	
	private CafeGUI CG;
	private CafeButton cf;
	public void init() {
		this.setLayout(new GridLayout(5,1));
		this.add(pn); this.add(pn_hoticed); this.add(pn_add); this.add(pn_num); this.add(pn_bt);
		
		pn.setLayout(new BorderLayout());
		pn.add("North", lb);
		pn.add("Center", pn_);
		pn_.setLayout(new FlowLayout());
		pn_.add(bt_here); pn_.add(lb_blank2); pn_.add(bt_take);
		bt_here.addActionListener(this); bt_take.addActionListener(this);
		bt_here.setPreferredSize(new Dimension(70,50));	bt_take.setPreferredSize(new Dimension(70,50));
		
		pn_hoticed.setLayout(new BorderLayout());
		pn_hoticed.add("North", lb_hoticed);
		pn_hoticed.add("Center", pn_1);
		pn_1.setLayout(new FlowLayout());
		pn_1.add(bt_hot); pn_1.add(lb_blank);pn_1.add(bt_iced); 
		bt_hot.addActionListener(this); bt_iced.addActionListener(this);
		bt_hot.setPreferredSize(new Dimension(70,50)); bt_iced.setPreferredSize(new Dimension(70,50));
		
		pn_add.setLayout(new BorderLayout());
		pn_add.add("North", lb_add);
		pn_add.add("Center", pn_2);
		pn_2.setLayout(new FlowLayout());
		pn_2.add(bt_less); pn_2.add(bt_standard); pn_2.add(bt_more); 
		bt_less.setPreferredSize(new Dimension(70, 50)); bt_more.setPreferredSize(new Dimension(70, 50)); bt_standard.setPreferredSize(new Dimension(70, 50));
		bt_less.addActionListener(this); bt_more.addActionListener(this); bt_standard.addActionListener(this);
		
		pn_num.setLayout(new BorderLayout());
		pn_num.add("North", lb_num);
		pn_num.add("Center", pn_3);
		pn_3.add(bt_minus); pn_3.add(tf_num); pn_3.add(bt_plus); 
		bt_minus.setPreferredSize(new Dimension(50,40)); bt_plus.setPreferredSize(new Dimension(50,40)); tf_num.setPreferredSize(new Dimension(30,20));
		bt_minus.addActionListener(this); bt_plus.addActionListener(this); 
		tf_num.setEditable(false); 
		
		pn_bt.setLayout(new FlowLayout());
		pn_bt.add(bt_order); pn_bt.add(lb_blank1); pn_bt.add(bt_cancel);
		bt_order.setPreferredSize(new Dimension(90, 70));	bt_cancel.setPreferredSize(new Dimension(90, 70));
		bt_cancel.addActionListener(this);
		bt_minus.setEnabled(false);
		getTable().put(4, Integer.toString(1));
	}
	
	public CoffeeDialog (CafeGUI owner, CafeButton selectedCoffee, boolean modal){
		super(owner, selectedCoffee.getName(), modal);
		this.CG = owner;
		this.cf = selectedCoffee;
		this.init();
		menu.setMenu(selectedCoffee.getName());
		this.setSize(300, 450);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth()/2 - this.getWidth()/2);
		int ypos = (int)(screen.getHeight()/2 - this.getHeight()/2);
		this.setLocation(xpos, ypos);
		this.setResizable(false);
		this.setVisible(false);
		
	}
	
	public void setView(boolean isView) {
		this.setVisible(isView);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//장바구니 담기 버튼 클릭시, 장바구니에 담겼다는 메세지와 함께 Dialog 종료
		 if (e.getSource() == bt_cancel) {
			table.clear();
			CG.list.remove(cf);
			count = 1;
			tf_num.setText(Integer.toString(count));
			dispose();
		//+버튼 클릭시, textfield에 ++count
		}else if (e.getSource() == bt_plus) {
			setCount(count + 1);
			tf_num.setText(Integer.toString(count));
			bt_minus.setEnabled(true);
			getTable().put(4, Integer.toString(count));
		//-버튼 클릭시, textfield에 --count / 수량이 1보다 작을 경우 버튼 비활성화
		}else if (e.getSource() == bt_minus) {
			if (count>1) {
				bt_minus.setEnabled(true);
				setCount(count - 1);
				tf_num.setText(Integer.toString(count));
			}else {
				bt_minus.setEnabled(false);
			}
			getTable().put(4, Integer.toString(count));
		}else if(e.getSource() == bt_order) {
        	if (getTable().get(1) == null || getTable().get(2) == null || getTable().get(3) == null) {
	            JOptionPane.showMessageDialog(this, "옵션을 모두 선택해주세요");
	        } else {
	        	
                JPanel panel = new JPanel();
				JLabel label = new JLabel("<html>"+cf.getName()
						+"["+getTable().get(1)+"]" + "<br>"
                		+ "ㄴ" + getTable().get(2)+"+"+getTable().get(3)+ "<br>"
                		+ "->"+getTable().get(4)+ "개" + "<br>"
                		+ cf.getPrice()*count + "원"+"</html>");
                JButton cancelButton = new JButton("X");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 취소 버튼이 속한 패널 삭제
                        CG.in_p.remove(panel);
                        CG.in_p.revalidate();
                        CG.in_p.repaint();
                        // 리스트에서 해당 주문 제거
                        CG.list.remove(cf);
                        // 다시 주문 내역 갱신
                        CG.setTextArea();
                    }
                });
                panel.add(label);
                panel.add(cancelButton);
                CG.in_p.add(panel);
                cf.setPrice(cf.getPrice()*count);
                CG.list.add(cf);//바뀐 cf를 넣어야되는데...
	        	JOptionPane.showMessageDialog(this, "ADD CART!");
	        	count=1;
	            tf_num.setText(Integer.toString(count));
	            
	            dispose();
        }
        System.out.println(table);
    
        } else {
	        // 버튼에 따라 메뉴 정보 설정
	        if (e.getSource() == bt_here) {
	        	getTable().put(1, bt_here.getText());
	        } else if (e.getSource() == bt_take) {
	        	getTable().put(1, bt_take.getText());
	        } else if (e.getSource() == bt_hot) {
	        	getTable().put(2, bt_hot.getText());
	        } else if (e.getSource() == bt_iced) {
	        	getTable().put(2, bt_iced.getText());
	        } else if (e.getSource() == bt_less) {
	        	getTable().put(3, bt_less.getText());
	        } else if (e.getSource() == bt_standard) {
	        	getTable().put(3, bt_standard.getText());
	        } else if (e.getSource() == bt_more) {
	        	getTable().put(3, bt_more.getText());
	        }
		
		}
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}


	public Hashtable<Integer,String> getTable() {
		return table;
	}

	public void setTable(Hashtable<Integer,String> table) {
		this.table = table;
	}


	
	
}
