/**
 *     This file is part of Diki.
 *
 *     Copyright (C) 2009 jtheuer
 *     Please refer to the documentation for a complete list of contributors
 *
 *     Diki is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Diki is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Diki.  If not, see <http://www.gnu.org/licenses/>.
 */
	
package de.jtheuer.diki.gui.panels.result;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JPanel;

import de.jtheuer.jjcomponents.swing.LayoutFactory;
import de.jtheuer.jjcomponents.swing.panels.JJPanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * A RatePanel shows 5 Bars of different colors
 */
public class RatePanel extends JJPanel {
	/**
	 */
	private static final long serialVersionUID = -704440319397133275L;
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(RatePanel.class.getName());
	private static final Dimension DIM = new Dimension(20,10);
	ColorButton[] btns = new ColorButton[5];
	private int currentvalue;
	private Action action;
	
	public RatePanel(Action action) {
		this();
		this.action = action;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	/**
	 * 
	 */
	public RatePanel() {
		LayoutFactory.setBoxLayoutV(this);
		setOpaque(false);
//		add(new JComboBox(new String[]{"-2","-1"," 0","+1","+2"}));
		/* create lights */
		btns[0] = new ColorButton(new Color(0,255,0),-2);
		btns[1] = new ColorButton(new Color(130,255,130),-1);
		btns[2] = new ColorButton(new Color(255,255,255),0);
		btns[3] = new ColorButton(new Color(255,130,130),1);
		btns[4] = new ColorButton(new Color(255,0,0),2);
		
		for (Component c : btns) {
			add(c);
		}
	}
	
	public int getCurrentValue() {
		return currentvalue;
	}
	
	public void setCurrentValue(int value) {
		this.currentvalue = value;
		action.actionPerformed(new ActionEvent(this,0,"Value changed"));
		repaint();
	}
	
	class ColorButton extends JPanel {
		private Color color;
		private int value;
		
		public ColorButton(Color color,final int value) {
			this.color = color;
			this.value = value;
			setMinimumSize(DIM);
			setPreferredSize(DIM);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setCurrentValue(value);
				}
			});
		}
		
		@Override
		public void paintComponent(Graphics g) {
			Color framecolor = Color.BLACK;
			if(getCurrentValue() == value) {
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			} else {
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
			}
			g.setColor(color);
			g.fillRect(0, 1, DIM.width-1, DIM.height-2);
			
			g.setColor(framecolor);
			g.drawRect(0, 1, DIM.width-1, DIM.height-2);
		}
		
	}
	
}
