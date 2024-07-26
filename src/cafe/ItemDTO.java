package cafe;

import java.awt.*;

/*
MENU                                               VARCHAR2(22)
PRICE                                              NUMBER
*/

public class ItemDTO extends Button{

	private int no;
	private String menu;
	private int price;
	private Image image;
	
	public ItemDTO(int no,String menu, int price, Image image) {
		this.no = no;
		this.menu=menu;
		this.price = price;
		this.image = image;
	}
	

	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}

	public void paint(Graphics g) {
		g.drawImage(image, 5, 5, this.getWidth()-10, this.getHeight()-10, this);
	}


	public String getMenu() {
		return menu;
	}


	public void setMenu(String menu) {
		this.menu = menu;
	}
}
