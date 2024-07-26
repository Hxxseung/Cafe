package cafe;

import java.awt.*;

public class CafeButton extends Button{
	private String name;
	private int price;
	private Image image;
	
	public CafeButton(String name, int price, Image image) {
		this.name = name;
		this.price = price;
		this.image = image;
	}
	
	public String getName() {
		return name;
	}
	public int getPrice() {
		return price;
	}

	public void paint(Graphics g) {
		g.drawImage(image, 5, 5, this.getWidth()-10, this.getHeight()-10, this);
	}
}