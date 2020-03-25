import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class FieldBut extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean isShip = false;
	private boolean shot = false;
	public boolean setup = true;
	int x,y;
	GameField f;
	
	public FieldBut(int px, int py, GameField gF) {	//erstellt den Button mit der gegebenen Position
		super();
		
		x = px;
		y = py;
		
		f = gF;
		
		setBounds(x * 25, y *25, 25, 25);
		setBackground(Color.lightGray);
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				f.setupSet(x, y, isShip);
			}
		});
	}
	
	public void actionPerf(int intendedSize) {	//wenn noch setup modus ist, werden dort wo geklickt wurde schiffe gesetzt, sonst werden die schiffe "geöffnet"
		if (setup) {
			isShip = !isShip;
			if(isShip) {
				setBackground(Color.black);
				f.minusShip(intendedSize);
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
