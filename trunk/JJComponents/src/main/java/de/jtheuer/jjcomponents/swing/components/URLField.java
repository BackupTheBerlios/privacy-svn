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
package de.jtheuer.jjcomponents.swing.components;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * * URLFieldPanel * *
 * 
 * @author FRMEYER *
 * @version 15.05.2008 21:08:46
 */
public class URLField extends JPanel implements ActionListener, DocumentListener {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new URLField());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private Config config;
	private JLabel label;
	private JTextField field;
	private SignIcon icon;

	public URLField(String text) {
		this();
		setText(text);
	}

	public URLField() {

		config = new Config();
		icon = new SignIcon(config);
		label = new JLabel(icon);
		/* show at least a 16x16 icon */
		label.setMinimumSize(new Dimension(18,18));
		field = new JTextField(30);

		buildUI();

		field.addActionListener(this);
		field.getDocument().addDocumentListener(this);

		setText(null);
	}

	private void buildUI() {
		Box.Filler divider = new Box.Filler(new Dimension(1, 1), new Dimension(1, 1), new Dimension(1, 1));
		divider.setBackground(field.getForeground());
		divider.setOpaque(true);

		GridBagConstraints gbcLabel = new GridBagConstraints(1, 1, 1, 1, 0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		GridBagConstraints gbcDivider = new GridBagConstraints(2, 1, 1, 1, 0, 1, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
				new Insets(0, 2, 0, 2), 0, 0);
		GridBagConstraints gbcField = new GridBagConstraints(3, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0);

		setLayout(new GridBagLayout());
		add(label, gbcLabel);
		add(divider, gbcDivider);
		add(field, gbcField);

		setBorder(field.getBorder());
		setBackground(field.getBackground());
		setOpaque(true);
		label.setBackground(field.getBackground());
		label.setOpaque(true);
		field.setBorder(null);
	}

	private void updateIcon() {
		String text = field.getText();
		icon.configure(text);
		label.repaint();
	}

	public void setText(String text) {
		field.setText(text);
		updateIcon();
	}

	public void actionPerformed(ActionEvent e) {
		if (config.updateOnReturn) {
			updateIcon();
		}
	}

	public void insertUpdate(DocumentEvent e) {
		if (config.updateOnDocument) {
			updateIcon();
		}
	}

	public void removeUpdate(DocumentEvent e) {
		if (config.updateOnDocument) {
			updateIcon();
		}
	}

	public void changedUpdate(DocumentEvent e) {
		if (config.updateOnDocument) {
			updateIcon();
		}
	}

	private static class SignIcon extends ImageIcon {

		private Config config;

		private int maxW = 0;
		private int maxH = 0;

		private SignIcon(Config config) {
			setConfig(config);
		}

		void setConfig(Config config) {
			this.config = config;

			BufferedImage[] images = new BufferedImage[] { config.imageWeb, config.imageMail, config.imageInvalid };
			for (int i = 0; i < images.length; i++) {
				BufferedImage image = images[i];
				maxW = Math.max(maxW, image.getWidth());
				maxH = Math.max(maxH, image.getHeight());
			}

			configure(null);
		}

		public int getIconHeight() {
			return maxH;
		}

		public int getIconWidth() {
			return maxW;
		}

		public void configure(String text) {
			if (config.isTextWeb(text)) {
				setImage(config.imageWeb);
			} else if (config.isTextMail(text)) {
				setImage(config.imageMail);
			} else {
				setImage(config.imageInvalid);
			}
		}
	}

	private static class Config {
		Pattern patternWeb = Pattern.compile("(((http|https|ftp):\\/\\/)|www)" + "[a-z0-9\\-\\._]+\\/?[a-z0-9_\\.\\-\\?\\+\\/~=&#;,]*" + "[a-z0-9\\/]",
				Pattern.CASE_INSENSITIVE);
		Pattern patternMail = Pattern
				.compile(

				"^[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*@[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\\.[a-zA-Z]{2,6}$");

		BufferedImage imageWeb = createImage("WWW", true);
		BufferedImage imageMail = createImage("MAIL", true);
		BufferedImage imageInvalid = createImage("?", false);

		boolean updateOnReturn = true;
		boolean updateOnDocument = true;

		boolean isTextWeb(String text) {
			if (text == null) {
				return false;
			}
			return patternWeb.matcher(text).matches();
		}

		boolean isTextMail(String text) {
			if (text == null) {
				return false;
			}
			if (text.toLowerCase().startsWith("mailto:")) {
				text = text.substring("mailto:".length());
			}
			return patternMail.matcher(text).matches();
		}

		private static BufferedImage createImage(String s, boolean valid) {
			FontRenderContext frc = new FontRenderContext(null, true, true);
			Font font = new Font("dialog", Font.BOLD, 12);

			GlyphVector glyphs = font.createGlyphVector(frc, s);
			Shape shape = glyphs.getOutline();
			Rectangle bounds = shape.getBounds();

			int imageW = bounds.width;
			int imageH = bounds.height;
			BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) image.getGraphics();

			g.setColor(valid ? Color.blue : Color.red);
			g.translate(bounds.x, -bounds.y);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.fill(shape);

			return image;
		}
	}

}