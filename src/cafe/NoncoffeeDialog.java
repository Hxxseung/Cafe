package cafe;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class NoncoffeeDialog extends JDialog implements ActionListener {
	private MenuDTO menu = new MenuDTO();
	private Hashtable<Integer,String> table = new Hashtable<>();
	
	
	//매장 또는 포장
	private JPanel pn = new JPanel();
	private JLabel lb = new JLabel("매장 or 포장 ______________________________");
	private JPanel pn_ = new JPanel();
	private JButton[] bt_take = new JButton[]{new JButton("매장"), new JButton("포장")};
	private JLabel lb_blank2 = new JLabel("        ");	// 버튼 사이 공백
	//얼음량 선택
	private JPanel pn_add = new JPanel();
	private JLabel lb_add = new JLabel("얼음 선택 ________________________________");
	private JPanel pn_1 = new JPanel();
	private JButton[] bt_iOr = new JButton[]{new JButton("많이"), new JButton("적게"),new JButton("기본")};

	//수량 선택
	private JPanel pn_num = new JPanel();
	private JLabel lb_num = new JLabel("수량 선택 ________________________________");
	private JPanel pn_2 = new JPanel();
	private JButton bt_minus = new JButton("-");
	private JButton bt_plus = new JButton("+");
	private JTextField tf_num = new JTextField("1"); //기본 수량 1
	int count = 1;
	//주문 및 취소
	private JPanel pn_bt = new JPanel();
	JButton bt_order = new JButton("주문");
	private JButton bt_cancel = new JButton("취소");
	private JLabel lb_blank1 = new JLabel("        ");	// 버튼
	
	private CafeGUI CG;
	private CafeButton cf=null;
	
	public void init() {
		this.setLayout(new GridLayout(4,1));
		this.add(pn); this.add(pn_add); this.add(pn_num); this.add(pn_bt);
		
		pn.setLayout(new BorderLayout());
		pn.add("North", lb);
		pn.add("Center", pn_);
		pn_.setLayout(new FlowLayout());
		pn_.add(bt_take[0]);
		pn_.add(lb_blank2);
		pn_.add(bt_take[1]);
		for (JButton jButton : bt_take) {
			jButton.setPreferredSize(new Dimension(70, 50));
			jButton.addActionListener(this);
		}
		
		pn_add.setLayout(new BorderLayout());
		pn_add.add("North", lb_add);
		pn_add.add("Center", pn_1);
		pn_1.setLayout(new FlowLayout());
		for (JButton jButton : bt_iOr) {
			pn_1.add(jButton);
			jButton.setPreferredSize(new Dimension(70, 50));
			jButton.addActionListener(this);
		}
		
		pn_num.setLayout(new BorderLayout());
		pn_num.add("North", lb_num);
		pn_num.add("Center", pn_2);
		pn_2.add(bt_minus); pn_2.add(tf_num); pn_2.add(bt_plus); 
		bt_minus.setPreferredSize(new Dimension(50,40)); bt_plus.setPreferredSize(new Dimension(50,40)); tf_num.setPreferredSize(new Dimension(30,20));
		bt_minus.addActionListener(this); bt_plus.addActionListener(this); 
		tf_num.setEditable(false);
		
		pn_bt.setLayout(new FlowLayout());
		pn_bt.add(bt_order); pn_bt.add(lb_blank1); pn_bt.add(bt_cancel);
		bt_order.setPreferredSize(new Dimension(70, 70));	bt_cancel.setPreferredSize(new Dimension(70, 70));
		bt_cancel.addActionListener(this);
		
		bt_minus.setEnabled(false);
		table.put(3, Integer.toString(1));
	}
	
	public NoncoffeeDialog (CafeGUI owner, CafeButton selectedNoncoffee, boolean modal){
		super(owner, selectedNoncoffee.getName(), modal);
		this.init();
		this.CG = owner;
		this.cf = selectedNoncoffee;
		menu.setMenu(selectedNoncoffee.getName());
		this.setSize(300, 400);
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
	 if(e.getSource() == bt_order) {
		 
        	if (table.get(1) == null || table.get(2) == null) {
	            JOptionPane.showMessageDialog(this, "옵션을 모두 선택해주세요");
	        } else {
                JPanel panel = new JPanel();
				JLabel label = new JLabel("<html>"+cf.getName()
						+"["+getTable().get(1)+"]" + "<br>"
                		+ "ㄴ" + getTable().get(2)+ "<br>"
                		+ "->"+ cf.getPrice()*count + "원"+"</html>");
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
                CG.list.add(cf);//바뀐 cf를 넣어야되는데...
	        	
                
                JOptionPane.showMessageDialog(this, "ADD CART!");	
	            tf_num.setText(Integer.toString(count));
	            dispose();
	            CG.setTextArea();
	            
	            count=1;
	        }
        System.out.println(table);
    
        }else if (e.getSource() == bt_cancel) {
			table.clear();
			CG.list.remove(cf);
			count = 1;
			tf_num.setText(Integer.toString(count));
			dispose();
		}else if (e.getSource() == bt_plus) {
			++count;
			tf_num.setText(Integer.toString(count));
			bt_minus.setEnabled(true);
			table.put(4, Integer.toString(count));
		//-버튼 클릭시, textfield에 --count / 수량이 1보다 작을 경우 버튼 비활성화
		}else if (e.getSource() == bt_minus) {
			if (count>1) {
				bt_minus.setEnabled(true);
				--count;
				tf_num.setText(Integer.toString(count));
			}else {
				bt_minus.setEnabled(false);
			}
			table.put(4, Integer.toString(count));
		}else { // 옵션 버튼 클릭 시
			MenuDTO menu = new MenuDTO();
			for (JButton jButton : bt_take) {
				if (e.getSource() == jButton) {
					table.put(1, jButton.getText());
				}
			}
			for (JButton jButton : bt_iOr) {
				if (e.getSource() == jButton) {
					table.put(2, jButton.getText());
				}
			}
		}
	}

	public Hashtable<Integer,String> getTable() {
		return table;
	}

	public void setTable(Hashtable<Integer,String> table) {
		this.table = table;
	}

}

