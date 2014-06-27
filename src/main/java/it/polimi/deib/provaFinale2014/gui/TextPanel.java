package it.polimi.deib.provaFinale2014.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Panel che contiene la TextArea che include i messaggi visualizzabili
 */
public class TextPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextArea textArea;
	private ImageIcon sheeplandLogo;
	
	/**
	 * Costruisce un TextPanel inizializzando la sua TextArea
	 */
	public TextPanel() {
		this.setLayout(new FlowLayout());
		
		sheeplandLogo = new ImageIcon(Default.sheeplandLogoUrl);
		
		textArea = new JTextArea();
		textArea.setBackground(Default.getBlue());
		textArea.setForeground(Color.WHITE);
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension(300, Default.getBoardDimension().height - 140));
		textArea.setMargin(new Insets(0, 20, 0, 10));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JButton sheeplandButton = new JButton(sheeplandLogo);
		sheeplandButton.setBorder(null);
		sheeplandButton.setPreferredSize(new Dimension(300, 100));
		sheeplandButton.setSelectedIcon(sheeplandLogo);
		
		this.add(sheeplandButton);
		this.add(textArea);
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	/**
	 * Aggiunge un testo in cima al TextPanel
	 * @param text testo da aggiungere
	 */
	public void appendText(String text) {
		textArea.setText(text + "\n\n" + textArea.getText());
	}
	
	@Override
	public Dimension getSize() {
		return Default.getTextPanelDimension();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return Default.getTextPanelDimension();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return Default.getTextPanelDimension();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return Default.getTextPanelDimension();
	}
}
