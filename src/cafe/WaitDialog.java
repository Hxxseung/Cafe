package cafe;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.util.*;
import java.util.List;


public class WaitDialog extends JDialog {
	
	private JPanel dlg_p = new JPanel();
	
	
	
	private JLabel lb_w = new JLabel("음료를 준비중 입니다.",JLabel.CENTER);
	private JLabel lb_c = new JLabel("주문하신 음료가 나왔습니다.",JLabel.CENTER);
	
	private JPanel jp_c = new JPanel();
	private JPanel jp_w = new JPanel();
	private JPanel jp_cp = new JPanel();
	private JPanel jp_wp = new JPanel();
	
	private JPanel[] p ;
	
	private JLabel[] waitLb;
	private JLabel[] clearLb;
	
	private ArrayList<Hashtable<String, Integer>> list = new ArrayList<>();	//미결재
	private ArrayList<Hashtable<String, Integer>> list2 = new ArrayList<>();//결재
	
	
	public void waitNumber() {
		jp_w.remove(jp_wp);
		jp_wp = new JPanel();
		int size = list.size();
		jp_wp.setLayout(new GridLayout(size,1)); //갯수만큼 칸을 나눔
		p = new JPanel[size];		//각 칸의 패널
		waitLb = new JLabel[size];
		for(int i =0;i<size;i++) {
			p[i]= new JPanel();
			p[i].setLayout(new BorderLayout());
			waitLb[i] = new JLabel(waitText(list.get(i)),JLabel.CENTER);
			p[i].add("Center",waitLb[i]);
			jp_wp.add(p[i]);
			
		}
		jp_w.add("Center",jp_wp);
		dlg_p.validate();
		
	
		
	}
		
	
	public void clearNumber() {
		jp_c.remove(jp_cp);
		jp_cp = new JPanel();
		int size = list2.size();
		jp_cp.setLayout(new GridLayout(size,1)); //갯수 칸
		p = new JPanel[size];
		clearLb = new JLabel[size];
		
		for(int i =0;i<size;i++) {
			for(int j = 0;j<list.size();j++) {
				if(list2.get(i)==list.get(j)) {
					list.remove(j);
				}
			}
			p[i]= new JPanel();
			p[i].setLayout(new BorderLayout());
			clearLb[i] = new JLabel(waitText(list2.get(i)),JLabel.CENTER);;
			p[i].add("Center",clearLb[i]);
			jp_cp.add(p[i]);
		}
		jp_c.add(jp_cp);
		dlg_p.validate();
		
	}
	
	
	public void setView(boolean isView) {
		this.setVisible(isView);
	}	
	
	public String waitText(Hashtable<String, Integer> ht) {
	    String order = "";
	    Set<Map.Entry<String, Integer>> entrySet = ht.entrySet();
	    Iterator<Map.Entry<String, Integer>> iterator = entrySet.iterator();
	    while (iterator.hasNext()) {
	        Map.Entry<String, Integer> entry = iterator.next();
	        order += entry.getKey() + " : " + entry.getValue();
	        if (iterator.hasNext()) {
	            order += ", ";
	        }
	    }
	    return order;
	}

	
	
	public String clearText(Hashtable<String, Integer> ht) {
		String order = "";
		Set<String> set = ht.keySet();
		String[] key = new String[set.size()];
		set.toArray(key);
		for(int j=0; j<key.length-1; ++j) {
			order += key[j] + " : " + ht.get(key[j]) + ", ";
		}
		order += key[key.length-1] + " : " + ht.get(key[key.length-1] );
		JLabel label = new JLabel(order, JLabel.CENTER);
		jp_cp.add(label);
		dlg_p.validate();
		
		return order;
	}
	
	

	
	public WaitDialog (Frame owner, String title, boolean modal){
		super(owner, title, modal);
		int size = list.size();
		
		this.setLayout(new BorderLayout());
		this.add(dlg_p);
		dlg_p.setLayout(new GridLayout(1,2));
		dlg_p.add(jp_c);
		jp_c.setLayout(new BorderLayout());
		jp_c.add("North",lb_c);	//주문 완료 라벨
		jp_c.add("Center",jp_cp);//주문 완료 패널
		dlg_p.add(jp_w);
		jp_w.setLayout(new BorderLayout());
		jp_w.add("North",lb_w);	//주문 대기 라벨
		jp_w.add("Center",jp_wp);//주문 대기 패널
		
		
		this.setSize(600, 300);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth()/2 - this.getWidth()/2);
		int ypos = (int)(screen.getHeight()/2 - this.getHeight()/2);
		this.setLocation(xpos, ypos);
		this.setResizable(false);
		this.setVisible(false);
	}


	public void payData(Hashtable<String, Integer> ht) {
		list.add(ht);		
	}
	
	public void clearData(Hashtable<String, Integer> ht) {
		list2.add(ht);		
	}


	


			
	}



