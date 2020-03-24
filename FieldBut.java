import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class FieldBut extends JButton{

	private boolean isShip = false;
	private boolean shot = false;
	public boolean setup = true;
	int x,y;
	GameField f;
	
	public FieldBut(int px, int py, GameField gF) {
		super();
		
		x = px;
		y = py;
		
		f = gF;
		
		setBounds(x * 25, y *25, 25, 25);
		setBackground(Color.lightGray);
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionPerf();
			}
		});
	}
	
	public void actionPerf() {
		if (setup) {
			f.setupSet(x, y);
			isShip = !isShip;
			if(isShip) {
				setBackground(Color.black);
			}else {
				setBackground(Color.lightGray);
			}
		} else if(!shot){
			if(isShip) {
				//send(hit this)
				setEnabled(false);
				setBackground(Color.red);
			}else {
				//send(noHit this)
				setEnabled(false);
				setBackground(Color.blue);
			}
			shot = true;
		}	
	}
	
	
	
	
}
