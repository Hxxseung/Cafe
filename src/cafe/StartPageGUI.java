package cafe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class StartPageGUI extends JFrame implements ActionListener{
	private ImageIcon icon;
	private JScrollPane scrollPane;
	private RoundedButton bt_start = new RoundedButton("주문하기");
	
	public void startPage() {
		icon = new ImageIcon("src/img/background.jpg"); 
		Image img = icon.getImage();
		Image image = img.getScaledInstance(800, 500, Image.SCALE_SMOOTH);
		ImageIcon changeImg = new ImageIcon(image);
		
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(changeImg.getImage(), 0, 0, null);
		        setOpaque(false); //그림을 표시하게 설정,투명하게 조절
		        super.paintComponent(g);
		    }
		};
		this.add(background);
		
		background.setLayout(new FlowLayout());
		bt_start.setPreferredSize(new Dimension(200,150));
		bt_start.setBounds(400, 250, 600, 400);
		bt_start.setFont(new Font("Plain", Font.BOLD , 25));
		bt_start.addActionListener(this);
		bt_start.setBackground(new Color(98, 87, 28));
		bt_start.setBorderPainted(true);
		background.add(bt_start);
		bt_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               dispose();
               new CafeGUI("COFFEE");
            }
        });
		
		scrollPane = new JScrollPane(background);
		setContentPane(scrollPane);
	}
	public StartPageGUI(String title) {
		super(title);
		
		this.startPage();
		
		this.setSize(800, 550);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth()/2 - this.getWidth()/2);
		int ypos = (int)(screen.getHeight()/2 - this.getHeight()/2);
		this.setLocation(xpos, ypos);
		this.setResizable(false);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		new StartPageGUI("Cafe");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
