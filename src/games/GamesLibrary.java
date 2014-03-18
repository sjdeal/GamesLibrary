package games;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;

public class GamesLibrary extends JPanel implements ListSelectionListener{
  
  private static Library library = new Library();
  
  private static JTable table;
  private static DefaultTableModel tableModel;
  
  private JMenuBar menuBar;
  private JMenu menu, emulatorMenu, genreMenu;
  private ArrayList<JCheckBoxMenuItem> filterEmulators = new ArrayList<JCheckBoxMenuItem>();
  private ArrayList<JCheckBoxMenuItem> filterGenres = new ArrayList<JCheckBoxMenuItem>();
  private ArrayList<String> filterGenresStrings = new ArrayList<String>();
  private JMenuItem menuItem;
  private static JComboBox<Emulator> emulatorComboBox = 
    new JComboBox<Emulator>(library.getEmulatorsList());
  private static JComboBox<Emulator> removeEmulatorComboBox = 
    new JComboBox<Emulator>(library.getEmulatorsList());
  
  private JButton playButton, removeButton;
  
  private static JFileChooser exeFileChooser = new JFileChooser();
  private static JFileChooser glibFileChooser = new JFileChooser();
  
  private JFileChooser generalFileChooser = new JFileChooser();
  
  private boolean loaded = false;
  private static String savePath = "";
  
  
  public GamesLibrary(){
    super(new BorderLayout());
    
    tableModel = new DefaultTableModel() {
    	@Override
    	public boolean isCellEditable(int row, int column){
    		return false;
    	}
    };
    tableModel.addColumn("Game");
    tableModel.addColumn("Console");
    tableModel.addColumn("Genre");
    tableModel.addColumn("Year");
    
    //create and organize the table
    table = new JTable(tableModel);
    table.setAutoCreateRowSorter(true);
    table.setUpdateSelectionOnSort(true);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.addMouseListener(new MouseAdapter() {
    	public void mousePressed(MouseEvent me){
    		JTable table = (JTable) me.getSource();
    		Point p = me.getPoint();
    		int row = table.rowAtPoint(p);
    		if(me.getClickCount() == 2){
    			library.getGame((String) table.getValueAt(row, 0)).launch();
    		}
    	}
    });
    table.getTableHeader().setReorderingAllowed(false);
    table.getColumnModel().getColumn(0).setPreferredWidth(275);
    table.getColumnModel().getColumn(1).setPreferredWidth(150);
    table.getColumnModel().getColumn(2).setPreferredWidth(125);
    table.getColumnModel().getColumn(3).setMinWidth(50);
    DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
      Font font = new Font("Arial", Font.BOLD, 12);
      public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, column);
        setFont(font);
        return this;
      }
    };
    table.getColumnModel().getColumn(0).setCellRenderer(r);
    
    JScrollPane scrollPane = new JScrollPane(table);
    
    //Create buttons
    playButton = new JButton("Play");
    playButton.setActionCommand("Play");
    playButton.addActionListener(new PlayListener());
    
    final JTextField search = new JTextField();
    JButton searchButton = new JButton("Search");
    searchButton.setActionCommand("Search");
    searchButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e){
    		for(int i = 0; i < library.getGamesSize(); i++){
    			Game game = library.getGameAt(i);
    			if(game.getName().toLowerCase().contains(search.getText().toLowerCase())){
    				game.setVisibility(true);
    			}else{
    				game.setVisibility(false);
    			}
    			reload();
    		}
    	}
    });
    
    removeButton = new JButton("Remove");
    removeButton.setActionCommand("Remove");
    removeButton.addActionListener(new RemoveListener());
    removeButton.setEnabled(false);
    
    //Create panel for the buttons
    JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
    buttonPane.add(playButton);
    buttonPane.add(Box.createHorizontalStrut(5));
    buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
    buttonPane.add(Box.createHorizontalStrut(5));
    buttonPane.add(search);
    buttonPane.add(Box.createHorizontalStrut(5));
    buttonPane.add(searchButton);
    buttonPane.add(Box.createHorizontalStrut(5));
    buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
    buttonPane.add(Box.createHorizontalStrut(5));
    buttonPane.add(removeButton);
    buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    
    //Assemble the window
    add(scrollPane, BorderLayout.CENTER);
    add(buttonPane, BorderLayout.PAGE_END);
  }
  
  
  public JMenuBar createMenuBar(){
    //Create the menu bar
    menuBar = new JMenuBar();
    
    //Add the File menu
    menu = new JMenu("File");
    menuBar.add(menu);
    
    menuItem = new JMenuItem("Load");
    menuItem.addActionListener(new LoadListener());
    menu.add(menuItem);
    
    menuItem = new JMenuItem("Save");
    menuItem.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e){
    		save(false);
    	}
    });
    menu.add(menuItem);
    
    menuItem = new JMenuItem("Save As...");
    menuItem.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e){
    		save(true);
    	}
    });
    menu.add(menuItem);
    menu.addSeparator();
    
    menuItem = new JMenuItem("Exit");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        System.exit(0);
      }
    });
    menu.add(menuItem);
    
    //Add the View menu
    menu = new JMenu("Filter");
    menuBar.add(menu);
    menuItem = new JMenuItem("Show All");
    menuItem.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e){
    		for(int i = 0; i < library.getGamesSize(); i++){
    			library.getGameAt(i).setVisibility(true);
    		}
    		reload();
    	}
    });
    menu.add(menuItem);
    menu.addSeparator();
    emulatorMenu = new JMenu("Emulators");
    menu.add(emulatorMenu);
    genreMenu = new JMenu("Genres");
    menu.add(genreMenu);
    
    return menuBar;
  }
  
  public JPanel newEmulatorTab(){
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    
    final JTextField filepath = new JTextField();
    final JTextField console = new JTextField();
    final JTextField arguments = new JTextField();
    JLabel filepathLabel = new JLabel("Filepath:");
    JLabel consoleLabel = new JLabel("Console:");
    JLabel argumentsLabel = new JLabel("Arguments:");
    
    JButton addEmulator = new JButton("Add Emulator");
    addEmulator.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        Emulator emulator = new Emulator(console.getText(), filepath.getText(),
                                         arguments.getText());
        library.addEmulator(emulator);
        if(loaded) emulatorComboBox.addItem(emulator);
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(console.getText());
        item.setState(false);
        item.setMnemonic(KeyEvent.VK_C);
        item.addItemListener(new FilterEmulatorsListener());
        filterEmulators.add(item);
        emulatorMenu.add(item);
      }
    });
    
    JButton pickFile = new JButton("Choose a File");
    pickFile.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){    
        int value = exeFileChooser.showOpenDialog(table);
        if(value == JFileChooser.APPROVE_OPTION){
          File file = exeFileChooser.getSelectedFile();
          filepath.setText(file.getPath());
        }
      }
    });
    
    JButton removeEmulator = new JButton("Remove Emulator");
    removeEmulator.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        library.removeEmulator(removeEmulatorComboBox.getItemAt(removeEmulatorComboBox.getSelectedIndex()).getConsole());
      }
    });
        
    c.weightx = 1.0;
    c.fill = GridBagConstraints.BOTH;
    panel.add(filepathLabel, c);
    panel.add(filepath, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(pickFile, c);
    c.weightx = 0.0;
    c.gridwidth = 1;
    panel.add(consoleLabel, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(console, c);
    c.gridwidth = 1;
    panel.add(argumentsLabel, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(arguments, c);
    panel.add(addEmulator, c);
    c.gridwidth = 1;
    panel.add(removeEmulatorComboBox, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(removeEmulator, c);
    return panel;
  }
  
  public JPanel newPCGameTab(){
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    
    final JTextField filepath = new JTextField();
    final JTextField name = new JTextField();
    final JTextField genre = new JTextField();
    final JTextField year = new JTextField();
    JLabel filepathLabel = new JLabel("Filepath:");
    JLabel nameLabel = new JLabel("Name:");
    JLabel genreLabel = new JLabel("Genre:");
    JLabel yearLabel = new JLabel("Year:");
    
    JButton addGame = new JButton("Add Game");
    addGame.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        library.addPCGame(filepath.getText(), name.getText(),
                          genre.getText(), Integer.parseInt(year.getText()));
        tableModel.addRow(new Object[]{name.getText(), "PC", 
                                       genre.getText(), year.getText()});
        if(!(filterGenresStrings.contains(genre.getText()))){
        	JCheckBoxMenuItem item = new JCheckBoxMenuItem(genre.getText());
            item.setState(false);
            item.setMnemonic(KeyEvent.VK_C);
            item.addItemListener(new FilterGenresListener());
            filterGenres.add(item);
            filterGenresStrings.add(genre.getText());
            genreMenu.add(item);
        }
        removeButton.setEnabled(true);
      }
    });
    
    JButton pickFile = new JButton("Choose a File");
    pickFile.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        int value = exeFileChooser.showOpenDialog(table);
        if(value == JFileChooser.APPROVE_OPTION){
          File file = exeFileChooser.getSelectedFile();
          String fileString = file.getPath();
          filepath.setText(fileString);
          int startIndex = 0;
          int endIndex = 0;
          for(int i = 0; i < fileString.length(); i++){
            if(fileString.charAt(i) == '\\') startIndex = i;
            else if(fileString.charAt(i) == '.') endIndex = i;
          }
          name.setText(fileString.substring(startIndex + 1, endIndex));
        }
      }
    });
    c.weightx = 1.0;
    c.fill = GridBagConstraints.BOTH;
    panel.add(filepathLabel, c);
    panel.add(filepath, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(pickFile, c);
    c.weightx = 0.0;
    c.gridwidth = 1;
    panel.add(nameLabel, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(name, c);
    c.gridwidth = 1;
    panel.add(genreLabel, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(genre, c);
    c.gridwidth = 1;
    panel.add(yearLabel, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(year, c);
    panel.add(addGame, c);
    
    return panel;
  }
  
  public JPanel newEmulatedGameTab(){
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    
    final JTextField filepath = new JTextField();
    final JTextField name = new JTextField();
    final JTextField genre = new JTextField();
    final JTextField year = new JTextField();
    JLabel filepathLabel = new JLabel("Filepath:");
    JLabel emulatorLabel = new JLabel("Emulator:");
    JLabel nameLabel = new JLabel("Name:");
    JLabel genreLabel = new JLabel("Genre:");
    JLabel yearLabel = new JLabel("Year:");
    
    JButton addGame = new JButton("Add Game");
    addGame.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        library.addEmulatedGame(filepath.getText(), name.getText(),
                                emulatorComboBox.getItemAt(emulatorComboBox.getSelectedIndex()),
                                genre.getText(), Integer.parseInt(year.getText()));
        tableModel.addRow(new Object[]{name.getText(), 
          emulatorComboBox.getItemAt(emulatorComboBox.getSelectedIndex()).getConsole(),
          genre.getText(), year.getText()});
        removeButton.setEnabled(true);
        if(!(filterGenresStrings.contains(genre.getText()))){
        	JCheckBoxMenuItem item = new JCheckBoxMenuItem(genre.getText());
            item.setState(false);
            item.setMnemonic(KeyEvent.VK_C);
            item.addItemListener(new FilterGenresListener());
            filterGenres.add(item);
            filterGenresStrings.add(genre.getText());
            genreMenu.add(item);
        }
      }
    });
    
    JButton pickFile = new JButton("Choose a File");
    pickFile.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        int value = generalFileChooser.showOpenDialog(table);
        if(value == JFileChooser.APPROVE_OPTION){
          File file = generalFileChooser.getSelectedFile();
          String fileString = file.getPath();
          filepath.setText(fileString);
          int startIndex = 0;
          int endIndex = 0;
          for(int i = 0; i < fileString.length(); i++){
            if(fileString.charAt(i) == '\\') startIndex = i;
            else if(fileString.charAt(i) == '.') endIndex = i;
          }
          name.setText(fileString.substring(startIndex + 1, endIndex));
        }
      }
    });
    
    c.weightx = 1.0;
    c.fill = GridBagConstraints.BOTH;
    panel.add(filepathLabel, c);
    panel.add(filepath, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(pickFile, c);
    c.weightx = 0.0;
    c.gridwidth = 1;
    panel.add(nameLabel, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(name, c);
    c.gridwidth = 1;
    panel.add(emulatorLabel, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(emulatorComboBox, c);
    c.gridwidth = 1;
    panel.add(genreLabel, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(genre, c);
    c.gridwidth = 1;
    panel.add(yearLabel, c);
    c.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(year, c);
    panel.add(addGame, c);
    return panel;
  }

  public static void save(boolean openFileChooser){
	if(openFileChooser || (!openFileChooser && savePath == "")){
		int value = glibFileChooser.showSaveDialog(table);
	      if(value == JFileChooser.APPROVE_OPTION){
	        try{
	          File file = glibFileChooser.getSelectedFile();
	          ObjectOutputStream out = 
	            new ObjectOutputStream(new FileOutputStream(file));
	          out.writeObject(library);
	          out.close();
	        }catch(IOException ioe){
	          System.out.println(ioe);
	        }
	      }
	}else{
		try{
			File file = new File(savePath);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(library);
			out.close();
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
  }
  
  public static void reload(){
	  int rows = tableModel.getRowCount();
	  for(int i = 0; i < rows; i++){
		  tableModel.removeRow(0);
      }
      for(int i = 0; i < library.getGamesSize(); i++){
        Game game = library.getGameAt(i);
        if(game.isVisible()){
        tableModel.addRow(new Object[]{game.getName(), game.getConsole(),
          game.getGenre(), game.getYear()});
        }
      }
  }
  
  class PlayListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      library.getGame((String) table.getValueAt(table.getSelectedRow(), 0)).launch();
    }
  }
  
  class RemoveListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      table.getRowSorter().setSortKeys(null);
      int index = table.getSelectedRow();
      library.removeGame((String) table.getValueAt(index, 0));
      tableModel.removeRow(index);
      int size = tableModel.getRowCount();
      if(size == 0){
        removeButton.setEnabled(false);
      }else{
        if(index == tableModel.getRowCount()){
          index--;
        }
        table.clearSelection();
      }
    }
  }
  
  class LoadListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      int value = glibFileChooser.showOpenDialog(table);
      if(value == JFileChooser.APPROVE_OPTION){
        try{
          File file = glibFileChooser.getSelectedFile();
          ObjectInputStream in = 
            new ObjectInputStream(new FileInputStream(file));
          library = (Library) in.readObject();
          removeButton.setEnabled(true);
          reload();
          emulatorComboBox.removeAllItems();
          emulatorMenu.removeAll();
          for(int i = 0; i < library.getEmulatorsSize(); i++){
            emulatorComboBox.addItem(library.getEmulatorAt(i));
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(library.getEmulatorAt(i).getConsole());
            item.setState(false);
            item.setMnemonic(KeyEvent.VK_C);
            item.addItemListener(new FilterEmulatorsListener());
            filterEmulators.add(item);
            emulatorMenu.add(item);
          }
          for(int i = 0; i < library.getGamesSize(); i++){
        	  String s = library.getGameAt(i).getGenre();
        	  if(!(filterGenresStrings.contains(s))){
              	JCheckBoxMenuItem item = new JCheckBoxMenuItem(s);
                  item.setState(false);
                  item.setMnemonic(KeyEvent.VK_C);
                  item.addItemListener(new FilterGenresListener());
                  filterGenres.add(item);
                  filterGenresStrings.add(s);
                  genreMenu.add(item);
              }
          }
          in.close();
          loaded = true;
          savePath = file.getAbsolutePath();
        }catch(IOException ioe){
          System.out.println(ioe);
        }catch(ClassNotFoundException cnfe){
          System.out.println(cnfe);
        }
      }
    }
  }

  public void valueChanged(ListSelectionEvent e){
    if(e.getValueIsAdjusting() == false){
      if(table.getSelectedRow() == -1){
        playButton.setEnabled(false);
        removeButton.setEnabled(false);
      }else{
        playButton.setEnabled(true);
        removeButton.setEnabled(true);
      }
    }
  }
  
  class FilterEmulatorsListener implements ItemListener{
	  public void itemStateChanged(ItemEvent e){
		  table.getRowSorter().setSortKeys(null);
		  boolean allOff = true;
		  for(int i = 0; i < filterEmulators.size(); i++){
			  JCheckBoxMenuItem item = filterEmulators.get(i);
			  if(item.getState()) allOff = false;
			  String s = item.getText();
			  for(int j = 0; j < tableModel.getRowCount(); j++){
				  Game game = library.getGame((String) table.getValueAt(j, 0));
				  if(game.getConsole().equals(s)){
					  game.setVisibility(item.getState());
				  }
			  }
		  }
		  if(allOff){
			  for(int i = 0; i < library.getGamesSize(); i++){
	    			library.getGameAt(i).setVisibility(true);
	    		}
		  }
		  reload();
	  }
  }
  
  class FilterGenresListener implements ItemListener{
	  public void itemStateChanged(ItemEvent e){
		  table.getRowSorter().setSortKeys(null);
		  boolean allOff = true;
		  for(int i = 0; i < filterGenres.size(); i++){
			  JCheckBoxMenuItem item = filterGenres.get(i);
			  if(item.getState()) allOff = false;
			  String s = item.getText();
			  for(int j = 0; j < tableModel.getRowCount(); j++){
				  Game game = library.getGame((String) table.getValueAt(j, 0));
				  if(game.getGenre().equals(s)){
					  game.setVisibility(item.getState());
				  }
			  }
		  }
		  if(allOff){
			  for(int i = 0; i < library.getGamesSize(); i++){
	    			library.getGameAt(i).setVisibility(true);
	    		}
		  }
		  reload();
	  }
  }
  
  private static void showGUI(){
    JFrame frame = new JFrame("Games Library");
    JTabbedPane tabbedPane = new JTabbedPane();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    GamesLibrary contentPane = new GamesLibrary();
    contentPane.setOpaque(true);
    tabbedPane.addTab("Library", contentPane);
    tabbedPane.addTab("Add Emulator", contentPane.newEmulatorTab());
    tabbedPane.addTab("Add Emulated Game", contentPane.newEmulatedGameTab());
    tabbedPane.addTab("Add PC Game", contentPane.newPCGameTab());
    frame.setJMenuBar(contentPane.createMenuBar());
    frame.add(tabbedPane);
    frame.pack();
    frame.setVisible(true);
    frame.setSize(600,400);
  }
  
  public static void main(String[] args){
    FileNameExtensionFilter filter = 
      new FileNameExtensionFilter("Executable files (*.exe)", "exe");
    exeFileChooser.setFileFilter(filter);
    filter = new FileNameExtensionFilter("Games Library files (*.glib)", "glib");
    glibFileChooser.setFileFilter(filter);
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        showGUI();
      }
    });
  }
}
