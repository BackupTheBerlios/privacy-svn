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
/**
 (c) by Jan Torben Heuer <jan.heuer@uni-muenster.de

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/
 */
package de.jtheuer.jjcomponents.swing.components;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;

public class LinkButton extends JButton {

	private static final long serialVersionUID = -1374407064168960913L;

	public static final int ALWAYS_UNDERLINE = 0;

	public static final int HOVER_UNDERLINE = 1;

	public static final int NEVER_UNDERLINE = 2;

	public static final int SYSTEM_DEFAULT = 3;

	private int linkBehavior;

	private Color linkColor;

	private Color colorPressed;

	private Color visitedLinkColor;

	private Color disabledLinkColor;

	private URL buttonURL;

	private Action defaultAction;

	private boolean isLinkVisited;

	public LinkButton(URL url) {
		this(url, url.toExternalForm(), null);
	}

	public LinkButton(URL url, String title, Action clickaction) {
		super(title);
		linkBehavior = SYSTEM_DEFAULT;
		linkColor = Color.blue;
		colorPressed = Color.red;
		visitedLinkColor = new Color(128, 0, 128);

		setLinkURL(url);
		setCursor(Cursor.getPredefinedCursor(12));
		setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);
		setRolloverEnabled(true);

		if (clickaction == null) {
			defaultAction = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try {
						Desktop.getDesktop().browse(getLinkURL().toURI());
					} catch (IOException ioe) {
						throw new RuntimeException(ioe);
					} catch (URISyntaxException ioe) {
						throw new RuntimeException(ioe);
					}
				}
			};
		} else {
			defaultAction = clickaction;
		}
		addActionListener(defaultAction);
	}

	public void updateUI() {
		setUI(BasicLinkButtonUI.createUI(this));
	}

	private void setDefault() {
		UIManager.getDefaults().put("LinkButtonUI", "BasicLinkButtonUI");
	}

	public String getUIClassID() {
		return "LinkButtonUI";
	}

	protected void setupToolTipText() {
		String tip = null;
		if (buttonURL != null)
			tip = buttonURL.toExternalForm();
		setToolTipText(tip);
	}

	public void setLinkBehavior(int bnew) {
		checkLinkBehaviour(bnew);
		int old = linkBehavior;
		linkBehavior = bnew;
		firePropertyChange("linkBehavior", old, bnew);
		repaint();
	}

	private void checkLinkBehaviour(int beha) {
		if (beha != ALWAYS_UNDERLINE && beha != HOVER_UNDERLINE && beha != NEVER_UNDERLINE && beha != SYSTEM_DEFAULT)
			throw new IllegalArgumentException("Not a legal LinkBehavior");
		else
			return;
	}

	public int getLinkBehavior() {
		return linkBehavior;
	}

	public void setLinkColor(Color color) {
		Color colorOld = linkColor;
		linkColor = color;
		firePropertyChange("linkColor", colorOld, color);
		repaint();
	}

	public Color getLinkColor() {
		return linkColor;
	}

	public void setActiveLinkColor(Color colorNew) {
		Color colorOld = colorPressed;
		colorPressed = colorNew;
		firePropertyChange("activeLinkColor", colorOld, colorNew);
		repaint();
	}

	public Color getActiveLinkColor() {
		return colorPressed;
	}

	public void setDisabledLinkColor(Color color) {
		Color colorOld = disabledLinkColor;
		disabledLinkColor = color;
		firePropertyChange("disabledLinkColor", colorOld, color);
		if (!isEnabled())
			repaint();
	}

	public Color getDisabledLinkColor() {
		return disabledLinkColor;
	}

	public void setVisitedLinkColor(Color colorNew) {
		Color colorOld = visitedLinkColor;
		visitedLinkColor = colorNew;
		firePropertyChange("visitedLinkColor", colorOld, colorNew);
		repaint();
	}

	public Color getVisitedLinkColor() {
		return visitedLinkColor;
	}

	public URL getLinkURL() {
		return buttonURL;
	}

	public void setLinkURL(URL url) {
		URL urlOld = buttonURL;
		buttonURL = url;
		setupToolTipText();
		firePropertyChange("linkURL", urlOld, url);
		revalidate();
		repaint();
	}

	public void setLinkVisited(boolean flagNew) {
		boolean flagOld = isLinkVisited;
		isLinkVisited = flagNew;
		firePropertyChange("linkVisited", flagOld, flagNew);
		repaint();
	}

	public boolean isLinkVisited() {
		return isLinkVisited;
	}

	public void setDefaultAction(Action actionNew) {
		Action actionOld = defaultAction;
		defaultAction = actionNew;
		firePropertyChange("defaultAction", actionOld, actionNew);
	}

	public Action getDefaultAction() {
		return defaultAction;
	}

	protected String paramString() {
		String str;
		if (linkBehavior == ALWAYS_UNDERLINE)
			str = "ALWAYS_UNDERLINE";
		else if (linkBehavior == HOVER_UNDERLINE)
			str = "HOVER_UNDERLINE";
		else if (linkBehavior == NEVER_UNDERLINE)
			str = "NEVER_UNDERLINE";
		else
			str = "SYSTEM_DEFAULT";
		String colorStr = linkColor == null ? "" : linkColor.toString();
		String colorPressStr = colorPressed == null ? "" : colorPressed.toString();
		String disabledLinkColorStr = disabledLinkColor == null ? "" : disabledLinkColor.toString();
		String visitedLinkColorStr = visitedLinkColor == null ? "" : visitedLinkColor.toString();
		String buttonURLStr = buttonURL == null ? "" : buttonURL.toString();
		String isLinkVisitedStr = isLinkVisited ? "true" : "false";
		return super.paramString() + ",linkBehavior=" + str + ",linkURL=" + buttonURLStr + ",linkColor=" + colorStr + ",activeLinkColor=" + colorPressStr
				+ ",disabledLinkColor=" + disabledLinkColorStr + ",visitedLinkColor=" + visitedLinkColorStr + ",linkvisitedString=" + isLinkVisitedStr;
	}
}

class BasicLinkButtonUI extends MetalButtonUI {
	private static final BasicLinkButtonUI ui = new BasicLinkButtonUI();

	public BasicLinkButtonUI() {}

	public static ComponentUI createUI(JComponent jcomponent) {
		return ui;
	}

	protected void paintText(Graphics g, JComponent com, Rectangle rect, String s) {
		LinkButton bn = (LinkButton) com;
		ButtonModel bnModel = bn.getModel();
		if (bnModel.isEnabled()) {
			if (bnModel.isPressed())
				bn.setForeground(bn.getActiveLinkColor());
			else if (bn.isLinkVisited())
				bn.setForeground(bn.getVisitedLinkColor());

			else
				bn.setForeground(bn.getLinkColor());
		} else {
			if (bn.getDisabledLinkColor() != null)
				bn.setForeground(bn.getDisabledLinkColor());
		}
		super.paintText(g, com, rect, s);
		int behaviour = bn.getLinkBehavior();
		boolean drawLine = false;
		if (behaviour == LinkButton.HOVER_UNDERLINE) {
			if (bnModel.isRollover())
				drawLine = true;
		} else if (behaviour == LinkButton.ALWAYS_UNDERLINE || behaviour == LinkButton.SYSTEM_DEFAULT)
			drawLine = true;
		if (!drawLine)
			return;
		FontMetrics fm = g.getFontMetrics();
		int x = rect.x + getTextShiftOffset();
		int y = (rect.y + fm.getAscent() + fm.getDescent() + getTextShiftOffset()) - 1;
		if (bnModel.isEnabled()) {
			g.setColor(bn.getForeground());
			g.drawLine(x, y, (x + rect.width) - 1, y);
		} else {
			g.setColor(bn.getBackground().brighter());
			g.drawLine(x, y, (x + rect.width) - 1, y);
		}
	}
}
