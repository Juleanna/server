/*
 * Copyright © 2019 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gui.editor;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.FIRST_LINE_START;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.DIRECTORIES_ONLY;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import com.l2jserver.gui.editor.ConfigUserInterface.ConfigFile.ConfigComment;
import com.l2jserver.gui.editor.ConfigUserInterface.ConfigFile.ConfigProperty;

/**
 * Configuration Editor GUI.
 * @author KenM
 * @author Zoey76
 * @version 1.0.0
 */
public class ConfigUserInterface extends JFrame implements ActionListener {
	private static final long serialVersionUID = 2609592249095305857L;
	
	private static final String IMAGE_PATH = "/images/";
	
	private static final String EOL = System.lineSeparator();
	
	private final JTabbedPane _tabPane = new JTabbedPane();
	
	private final JFileChooser fileChooser;
	
	private List<ConfigFile> _configs = new ArrayList<>();
	
	public ConfigUserInterface() {
		setTitle("Server Configuration Tool");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(750, 500);
		setLayout(new GridBagLayout());
		
		setDefaultLookAndFeelDecorated(true);
		setIconImage(new ImageIcon(getClass().getResource(IMAGE_PATH + "l2j.png")).getImage());
		
		fileChooser = new JFileChooser("Open");
		fileChooser.setFileSelectionMode(DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(new File("."));
		
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = HORIZONTAL;
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weighty = 0;
		cons.weightx = 1;
		
		JMenuBar menubar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setActionCommand("exit");
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);
		
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.setIcon(new ImageIcon(getClass().getResource(IMAGE_PATH + "help.png")));
		aboutItem.setActionCommand("about");
		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);
		
		menubar.add(fileMenu);
		menubar.add(helpMenu);
		
		setJMenuBar(menubar);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.add(createToolButton("disk.png", "Save", "save"));
		this.add(toolBar, cons);
		
		cons.gridy++;
		cons.fill = BOTH;
		cons.weighty = 1;
		loadConfigs();
		buildInterface();
		this.add(_tabPane, cons);
	}
	
	private JButton createToolButton(String image, String text, String action) {
		JButton button = new JButton(text, new ImageIcon(getClass().getResource(IMAGE_PATH + image)));
		button.setActionCommand(action);
		button.addActionListener(this);
		return button;
	}
	
	private void buildInterface() {
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setReshowDelay(0);
		
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = NONE;
		cons.anchor = FIRST_LINE_START;
		cons.insets = new Insets(2, 2, 2, 2);
		for (ConfigFile cf : getConfigs()) {
			JPanel panel = new JPanel() {
				private static final long serialVersionUID = -323928678804839054L;
				
				@Override
				public void scrollRectToVisible(Rectangle r) {
				}
			};
			panel.setLayout(new GridBagLayout());
			
			cons.gridy = 0;
			cons.weighty = 0;
			for (ConfigComment cc : cf.getConfigProperties()) {
				if (!(cc instanceof ConfigProperty cp)) {
					continue;
				}
				
				cons.gridx = 0;
				
				JLabel keyLabel = new JLabel(cp.getDisplayName() + ':', new ImageIcon(getClass().getResource(IMAGE_PATH + "help.png")), SwingConstants.LEFT);
				String comments = "<b>" + cp.getName() + ":</b><br>" + cp.getComments();
				comments = comments.replace(EOL, "<br>");
				comments = "<html>" + comments + "</html>";
				keyLabel.setToolTipText(comments);
				cons.weightx = 0;
				panel.add(keyLabel, cons);
				cons.gridx++;
				
				JComponent valueComponent = cp.getValueComponent();
				valueComponent.setToolTipText(comments);
				cons.weightx = 1;
				panel.add(valueComponent, cons);
				cons.gridx++;
				cons.gridy++;
			}
			cons.gridy++;
			cons.weighty = 1;
			panel.add(new JLabel(), cons); // filler
			_tabPane.addTab(cf.getName(), new JScrollPane(panel));
		}
	}
	
	private void loadConfigs() {
		final var returnVal = fileChooser.showOpenDialog(this);
		if (returnVal != APPROVE_OPTION) {
			JOptionPane.showMessageDialog(ConfigUserInterface.this, "Must select a configuration folder!", "Error", ERROR_MESSAGE);
			return;
		}
		
		final File configsDir = fileChooser.getSelectedFile();
		final File[] files = configsDir.listFiles();
		if (files == null) {
			JOptionPane.showMessageDialog(ConfigUserInterface.this, "Error reading " + "config", "Error", ERROR_MESSAGE);
			return;
		}
		
		for (File file : files) {
			if (file.getName().endsWith(".properties") && file.isFile() && file.canWrite()) {
				try {
					parsePropertiesFile(file);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(ConfigUserInterface.this, "Error reading " + file.getName(), "Error", ERROR_MESSAGE);
					System.exit(3);
				}
			}
		}
	}
	
	private void parsePropertiesFile(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			LineNumberReader lnr = new LineNumberReader(isr)) {
			String line;
			StringBuilder commentBuffer = new StringBuilder();
			ConfigFile cf = new ConfigFile(file);
			while ((line = lnr.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) {
					// blank line, reset comments
					if (commentBuffer.length() > 0) {
						cf.addConfigComment(commentBuffer.toString());
					}
					commentBuffer.setLength(0);
				} else if (line.charAt(0) == '#') {
					if (commentBuffer.length() > 0) {
						commentBuffer.append(EOL);
					}
					commentBuffer.append(line.substring(1));
				} else if (line.indexOf('=') >= 0) {
					String[] kv = line.split("=", 2);
					String key = kv[0].trim();
					StringBuilder value = new StringBuilder();
					if (kv.length > 1) {
						value.append(kv[1].trim());
					}
					
					if (line.indexOf('\\') >= 0) {
						while (((line = lnr.readLine()) != null) && (line.indexOf('\\') >= 0)) {
							value.append(EOL + line);
						}
						value.append(EOL + line);
					}
					
					String comments = commentBuffer.toString();
					commentBuffer.setLength(0); // reset
					
					cf.addConfigProperty(key, parseValue(value.toString()), comments);
				}
			}
			getConfigs().add(cf);
		}
	}
	
	private Object parseValue(String value) {
		if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")) {
			return Boolean.parseBoolean(value);
		}
		
		if (value.equals("localhost")) {
			value = "127.0.0.1";
		}
		
		String[] parts = value.split("\\.");
		if (parts.length == 4) {
			boolean ok = true;
			for (int i = 0; (i < 4) && ok; i++) {
				try {
					int parseInt = Integer.parseInt(parts[i]);
					if ((parseInt < 0) || (parseInt > 255)) {
						ok = false;
					}
				} catch (NumberFormatException e) {
					ok = false;
				}
			}
			
			if (ok) {
				try {
					InetAddress address = InetAddress.getByName(value);
					return address;
				} catch (UnknownHostException e) {
					// ignore
				}
			}
		}
		
		return value;
	}
	
	static class ConfigFile {
		private final File _file;
		private String _name;
		private final List<ConfigComment> _configs = new ArrayList<>();
		
		public ConfigFile(File file) {
			_file = file;
			int lastIndex = file.getName().lastIndexOf('.');
			setName(file.getName().substring(0, lastIndex));
		}
		
		public void addConfigProperty(String name, Object value, ValueType type, String comments) {
			_configs.add(new ConfigProperty(name, value, type, comments));
		}
		
		public void addConfigComment(String comment) {
			_configs.add(new ConfigComment(comment));
		}
		
		public void addConfigProperty(String name, Object value, String comments) {
			this.addConfigProperty(name, value, ValueType.firstTypeMatch(value), comments);
		}
		
		public List<ConfigComment> getConfigProperties() {
			return _configs;
		}
		
		public void setName(String name) {
			_name = name;
		}
		
		public String getName() {
			return _name;
		}
		
		public void save() throws IOException {
			try (FileOutputStream fos = new FileOutputStream(_file);
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				BufferedWriter bufWriter = new BufferedWriter(osw)) {
				for (ConfigComment cc : _configs) {
					cc.save(bufWriter);
				}
			}
		}
		
		class ConfigComment {
			
			private String _comments;
			
			public ConfigComment(String comments) {
				_comments = comments;
			}
			
			public String getComments() {
				return _comments;
			}
			
			public void setComments(String comments) {
				_comments = comments;
			}
			
			public void save(Writer writer) throws IOException {
				StringBuilder sb = new StringBuilder();
				sb.append('#');
				sb.append(getComments().replace(EOL, EOL + "#"));
				sb.append(EOL + EOL);
				writer.write(sb.toString());
			}
		}
		
		class ConfigProperty extends ConfigComment {
			private String _propname;
			private Object _value;
			private ValueType _type;
			private JComponent _component;
			
			public ConfigProperty(String name, Object value, ValueType type, String comments) {
				super(comments);
				if (!type.getType().isAssignableFrom(value.getClass())) {
					throw new IllegalArgumentException("Value Instance Type doesn't match the type argument.");
				}
				_propname = name;
				_type = type;
				_value = value;
			}
			
			public String getName() {
				return _propname;
			}
			
			public String getDisplayName() {
				return unCamelize(_propname);
			}
			
			public void setName(String name) {
				_propname = name;
			}
			
			public Object getValue() {
				return _value;
			}
			
			public void setValue(String value) {
				_value = value;
			}
			
			public ValueType getType() {
				return _type;
			}
			
			public void setType(ValueType type) {
				_type = type;
			}
			
			public JComponent getValueComponent() {
				if (_component == null) {
					_component = createValueComponent();
				}
				return _component;
			}
			
			public JComponent createValueComponent() {
				switch (getType()) {
					case BOOLEAN:
						boolean bool = (Boolean) getValue();
						JCheckBox checkBox = new JCheckBox();
						checkBox.setSelected(bool);
						return checkBox;
					case IPv4:
						return new JIPTextField((Inet4Address) getValue());
					case DOUBLE:
					case INTEGER:
					case STRING:
					default:
						String val = getValue().toString();
						JTextArea textArea = new JTextArea(val);
						textArea.setFont(UIManager.getFont("TextField.font"));
						int rows = 1;
						for (int i = 0; i < val.length(); i++) {
							if (val.charAt(i) == '\\') {
								rows++;
							}
						}
						textArea.setRows(rows);
						textArea.setColumns(Math.max(val.length() / rows, 20));
						return textArea;
				}
			}
			
			@Override
			public void save(Writer writer) throws IOException {
				String value;
				if (getValueComponent() instanceof JCheckBox) {
					value = Boolean.toString(((JCheckBox) getValueComponent()).isSelected());
					value = value.substring(0, 1).toUpperCase() + value.substring(1);
				} else if (getValueComponent() instanceof JIPTextField) {
					value = ((JIPTextField) getValueComponent()).getText();
				} else if (getValueComponent() instanceof JTextArea) {
					value = ((JTextArea) getValueComponent()).getText();
				} else {
					throw new IllegalStateException("Unhandled component value");
				}
				
				StringBuilder sb = new StringBuilder();
				sb.append('#');
				sb.append(getComments().replace(EOL, EOL + "#"));
				sb.append(EOL);
				sb.append(getName());
				sb.append(" = ");
				sb.append(value);
				sb.append(EOL);
				sb.append(EOL);
				writer.write(sb.toString());
			}
		}
	}
	
	public enum ValueType {
		BOOLEAN(Boolean.class),
		DOUBLE(Double.class),
		INTEGER(Integer.class),
		IPv4(Inet4Address.class),
		STRING(String.class);
		
		private final Class<?> _type;
		
		ValueType(Class<?> type) {
			_type = type;
		}
		
		public Class<?> getType() {
			return _type;
		}
		
		public static ValueType firstTypeMatch(Object value) {
			for (ValueType vt : ValueType.values()) {
				if (vt.getType() == value.getClass()) {
					return vt;
				}
			}
			throw new NoSuchElementException("No match for: " + value.getClass().getName());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		StringBuilder errors = new StringBuilder();
		
		if (cmd.equals("save")) {
			for (ConfigFile cf : ConfigUserInterface.this.getConfigs()) {
				try {
					cf.save();
				} catch (Exception e1) {
					e1.printStackTrace();
					errors.append("Error saving" + cf.getName() + ".properties. " + "Reason:" + e1.getLocalizedMessage() + EOL);
				}
			}
			if (errors.length() == 0) {
				JOptionPane.showMessageDialog(ConfigUserInterface.this, "Configuration saved successfully", "OK", INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(ConfigUserInterface.this, errors, "Error", ERROR_MESSAGE);
				System.exit(2);
			}
		} else if (cmd.equals("exit")) {
			System.exit(0);
		} else if (cmd.equals("about")) {
			JOptionPane.showMessageDialog(ConfigUserInterface.this, "© 2019 L2J Team. All rights reserved." + EOL + //
				"http://www.l2jserver.com" + EOL + EOL + "Icons by http://www.famfamfam.com" + EOL + EOL + "Language: English" + EOL //
				+ "Translation: L2J Team", "About", INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(IMAGE_PATH + "l2jserverlogo.png")));
		}
	}
	
	public void setConfigs(List<ConfigFile> configs) {
		_configs = configs;
	}
	
	public List<ConfigFile> getConfigs() {
		return _configs;
	}
	
	/**
	 * @param keyName
	 * @return Returns the configuration setting name in a human readable form.
	 */
	public static String unCamelize(final String keyName) {
		Pattern p = Pattern.compile("\\p{Lu}");
		Matcher m = p.matcher(keyName);
		StringBuffer sb = new StringBuffer();
		int last = 0;
		while (m.find()) {
			if (m.start() != (last + 1)) {
				m.appendReplacement(sb, " " + m.group());
			}
			last = m.start();
		}
		m.appendTail(sb);
		return sb.toString().trim();
	}
}
