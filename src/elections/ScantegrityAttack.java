/*************************************************************
* ScantegrityAttack.java
* Yves A.
*
* This program implements a scaled-down version of the Scantegrity
* voting system and implements an attack on it.
*************************************************************/

package elections;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.table.*;
import javax.swing.border.*;


public class ScantegrityAttack extends JFrame
{
  private static final int WIDTH = 900;
  private static final int HEIGHT = 530;
  private static final String CHECK = "\u2713";

  private JTable p;            // p table's data 
  private JPanel q;            // displays q table's data
  private JButton[][] qArr;    // manipulates q table's data
  private JTable r;            // r table's data 
  private JTable s;            // s table's data
  private JButton allowVoting;
  private JButton initialSetup;
  private JButton countVotesFairly;
  private JPanel topPanel;    // holds table p
	  
  // Candidates stored in array with 1st candidate matching 1st column
  // in tables p & s, 2nd candidate matching 2nd column, etc.
  private Candidate[] candidates;
	  
  private JButton[] candButtons;
  
  //**********************************************************

  public ScantegrityAttack()
  {
    setTitle("Attacking the Scantegrity Voting System");
    setSize(WIDTH, HEIGHT);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    createContents();
    setVisible(true);

    candidates = new Candidate[s.getColumnCount()];
    
    // Assign candidate id values starting with id = 1, so they match
    // the figures in the literature.
    for (int id=1; id<=s.getColumnCount(); id++)
    {
      candidates[id - 1] = createCandidate(id);
    }
  } // end ScantegrityAttack constructor

  //**********************************************************

  private void createContents()
  {
	  
    JLabel tableTitle;  // title underneath each table
	JPanel pPanel;      // holds table p's panel and table name
	JPanel qPanel;      // holds table q's panel and table name
    JPanel rPanel;      // holds table r's panel and table name
	JPanel sPanel;      // holds table s's panel and table name
 
	JPanel middlePanel; // holds tables q, r, and s
	JPanel buttonPanel;
	
	ButtonListener buttonListener;
	    
	DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	    
	setLayout(new BorderLayout(0, 10));
        
    // This is the part I will have done
    // Yves 
    // filling the p table with data from file
    String line = null;
    String[] arr;
    String[][] idsArr = new String[5][5];
    int l = 0;
    
    try {
      BufferedReader br = new BufferedReader(new FileReader("candidatesIDs.txt"));
      while ((line = br.readLine()) != null) { 
        arr = line.split(",");
        idsArr[l] = arr;
        ++l;
      }
      
      // See what you should put here
      p = new JTable(idsArr, new String[] {"Ballot ID", "Alice", "Bob", "Carl"});
      br.close();
    }
    catch (Exception e) {
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
    }    
    // Yves
    // This is where my part stops

  /*
	p = new JTable(
	  new String[][]
	  {
	    {"0001", "WT9", "7LH", "JNC"},
	    {"0002", "KMT", "TC3", "J3K"},
	    {"0003", "CH7", "3TW", "9JH"},
	    {"0004", "WJL", "KWK", "H7T"},
	    {"0005", "M39", "LTM", "HNN"}
	  },
	  new String[] {"Ballot ID", "Alice", "Bob", "Carl"});
  */

	q = createTableQPanel();

	
    l = 0;
    line = null;
    String[][] ptrsArr = new String[15][3];
    try {
      BufferedReader br = new BufferedReader(new FileReader("pointersTable.txt"));
      while ((line = br.readLine()) != null) { 
        arr = line.split(":");
        ptrsArr[l] = arr;
        ++l;
      }
      
      // See what you should put here
      r = new JTable(ptrsArr, new String[] {"Flag", "Q-Pointer", "S-Pointer"});
      br.close();
    }
    catch (Exception e) {
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
    }
    
      s = new JTable(
      new String[5][3],
	  new String[] {"Alice", "Bob", "Carl"});

	setTableCellAlignment(p, renderer, JLabel.CENTER);
	tableTitle = new JLabel("Table P", SwingConstants.CENTER);
	tableTitle.setBorder(new EmptyBorder(10, 0, 0, 0));
	pPanel = new JPanel(new BorderLayout());
	pPanel.add(p.getTableHeader(), BorderLayout.NORTH);
	pPanel.add(p, BorderLayout.CENTER);
	pPanel.add(tableTitle, BorderLayout.SOUTH);
	p.setEnabled(false); // Set the table p to be non-editable
	    
	qPanel = new JPanel(new BorderLayout(0, 10));
	qPanel.add(q, BorderLayout.CENTER);
	qPanel.add(new JLabel("Table Q", SwingConstants.CENTER),
	  BorderLayout.SOUTH);

	r.getColumnModel().getColumn(0).setPreferredWidth(60);
	r.getColumnModel().getColumn(1).setPreferredWidth(80);
    r.getColumnModel().getColumn(2).setPreferredWidth(80);
	setTableCellAlignment(r, renderer, JLabel.CENTER);
	tableTitle = new JLabel("Table R", SwingConstants.CENTER);
	tableTitle.setBorder(new EmptyBorder(10, 0, 0, 0));
	rPanel = new JPanel(new BorderLayout());
	rPanel.add(r.getTableHeader(), BorderLayout.NORTH);
	rPanel.add(r, BorderLayout.CENTER);
	rPanel.add(tableTitle, BorderLayout.SOUTH);
	r.setEnabled(false); // Set the table r to be non-editable

	s.getColumnModel().getColumn(0).setPreferredWidth(60);
	s.getColumnModel().getColumn(1).setPreferredWidth(60);
	s.getColumnModel().getColumn(2).setPreferredWidth(60);
	setTableCellAlignment(s, renderer, JLabel.CENTER);
	tableTitle = new JLabel("Table S", SwingConstants.CENTER);
	tableTitle.setBorder(new EmptyBorder(10, 0, 0, 0));
	sPanel = new JPanel(new BorderLayout());
	sPanel.add(s.getTableHeader(), BorderLayout.NORTH);
	sPanel.add(s, BorderLayout.CENTER);
	sPanel.add(tableTitle, BorderLayout.SOUTH);
	s.setEnabled(false); // Set the table s to be non-editable

	// Add pPanel to a FlowLayout panel to avoid table expansion
	topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	topPanel.add(pPanel);
	add(topPanel, BorderLayout.NORTH);
	//remove(topPanel);

	middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
	middlePanel.add(qPanel);
	middlePanel.add(rPanel);
	middlePanel.add(sPanel);
	add(middlePanel, BorderLayout.CENTER);
	    
	candButtons = new JButton[s.getColumnCount()];
	buttonListener = new ButtonListener();
	buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	
	for (int i=0; i<s.getColumnCount(); i++)
	{
	  candButtons[i] = new JButton("I want candidate " +
	    (i + 1) + " to win");
	  candButtons[i].addActionListener(buttonListener);
	  buttonPanel.add(candButtons[i]);
	  candButtons[i].setVisible(false);
	} // end for
	
    initialSetup = new JButton("Display initial setup");
    countVotesFairly = new JButton("Count the votes fairly");
    
	buttonPanel.add(initialSetup);
	initialSetup.addActionListener(buttonListener);
	buttonPanel.add(countVotesFairly);
	countVotesFairly.addActionListener(buttonListener);
	
	initialSetup.setVisible(false);
	countVotesFairly.setVisible(false);
	
	allowVoting = new JButton("Allow voting to begin");
	buttonPanel.add(allowVoting);
	allowVoting.addActionListener(buttonListener);
	//allowVoting.setActionCommand(Integer.toString(0));
	add(buttonPanel, BorderLayout.SOUTH);
  } // end createContents

  
  //**********************************************************

  private JPanel createTableQPanel()
  {
    JPanel q;
    JButton cell;
    
    qArr = new JButton[][]
    {
      {new JButton("Ballot ID"), new JButton(),
        new JButton(), new JButton()},
      {new JButton("0001"), new JButton("Bob / 7LH"),
        new  JButton("Alice / WT9"), new JButton("Carl / JNC")},
      {new JButton("0002"), new JButton("Carl / J3K"),
        new JButton("Bob / TC3"), new JButton("Alice / KMT")},
      {new JButton("0003"), new JButton("Carl / 9JH"),
        new JButton("Alice / CH7"), new JButton("Bob / 3TW")},
      {new JButton("0004"), new JButton("Bob / KWK"),
        new JButton("Carl / H7T"), new JButton("Alice / WJL")},
      {new JButton("0005"), new JButton("Alice / M39"),
        new JButton("Carl / HNN"), new JButton("Bob / LTM")}
    };
    
    q = new JPanel(
      new GridLayout(qArr.length, qArr[0].length));
    
    // Initially display the table Q with all information and every button disabled
    
    for (int i=0; i<qArr.length; i++)
    {
      for (int j=0; j<qArr[0].length; j++)
      {
        cell = qArr[i][j];
        cell.setEnabled(false);
        cell.setName(Integer.toString(i)); // save the row number as the current button's name
        cell.setActionCommand(cell.getText().substring(cell.getText().indexOf("/") + 1));
        cell.setMargin(new Insets(1, 2, 1, 2));
        
        if (i >= 1)
        {
          cell.setBackground(Color.WHITE);
        }
        
        q.add(cell);
        cell.addActionListener(new ButtonListener());
      }
    } // end for
    
    return q;
  } // end createTableQPanel

  //**********************************************************

  private void setTableCellAlignment(
    JTable table, DefaultTableCellRenderer renderer, int alignment)
  {
    renderer.setHorizontalAlignment(alignment);
    for (int i=0; i<table.getColumnCount();i++)
    {
      table.setDefaultRenderer(table.getColumnClass(i), renderer);
    }
  }

  //**********************************************************

  // Find number of votes cast for the given candidate ID.
  // Instantiate the candidate, store vote counts in it, and return
  // the candidate object.

  private Candidate createCandidate(int id)
  {
    int voteCount = 0;
    Candidate cand;
    
    for (int row=0; row<s.getRowCount(); row++)
    {
      if (s.getValueAt(row, id - 1).equals(CHECK))
      {
        voteCount++;
      }
    } // end for
    
    cand = new Candidate(id, voteCount);
    
    // Initialize rigged vote count to actual vote count.
    cand.adjustRiggedCount(voteCount);
    return cand;
  } // end createCandidate
  
  //**********************************************************

  // Inner class for event handling.

  private class ButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      JButton btn;           // the button that was clicked
      JButton cellToDisable; // the cell that should be disabled
      JButton gridCell;      // represent the enabled grid
      String btnText;        // each button label
      int disable;           // hold the value of cellToDiasable
      
      boolean finished = false;
      btn = (JButton) e.getSource(); // get the current button clicked
      
      // retrieve each candidate voting code
      btnText = btn.getActionCommand();
  
      for (int i=0; !finished && i<s.getColumnCount(); i++)
      {
        if (e.getSource() == candButtons[i])
        {
          rigTheVote(candidates[i],
            candidates[(i + 1) % 3], candidates[(i + 2) % 3]);
          finished = true;
        }
      } // end for
     
      btn.setText(btnText); // after pressing the button, change its text to the candidate code
      
      try 
      {
    	disable = Integer.parseInt(btn.getName());
      }
      catch (NumberFormatException nfe) 
      {
        disable = 0;
      }
      
      // When allow voting is clicked, enable the table Q buttons
      // and after the allow voting is clicked, disable it and retrieve
      // each voting button's clicked value while disabling the row
      // in which it is located.
      
      if (e.getSource() == allowVoting)
      {
    	allowVoting.setVisible(false);
    	initialSetup.setVisible(true);
    	countVotesFairly.setVisible(true);
    	topPanel.setVisible(false);
    	    	
    	// Make all of R's data invisible (set the cell's values to null)
    	for (int i=0; i<r.getColumnCount(); i++)
        {
          for (int j=0; j<r.getRowCount(); j++)
          {
        	  r.setValueAt(null, j, i);
          }
        }
    	
    	// Make the cheating button appear
      	for (int i=0; i<s.getColumnCount(); i++)
      	{
      	  candButtons[i].setVisible(true);
      	} // end for
      	
    	// Enable the candidates voting button and allow voting
    	for (int i=0; i<qArr.length; i++)
        {
          for (int j=0; j<qArr[0].length; j++)
          {
            cellToDisable = qArr[i][j];
            gridCell = qArr[i][j];
            
            // Display only the candidates names
            if (i != 0 && j != 0)
            {
              gridCell.setText(gridCell.getText().substring(0, gridCell.getText().indexOf("/")));
            }
            
            // Retrieve the default button color
            gridCell.setBackground(null);
            gridCell.setEnabled(true);         
              
            if (i == 0 || j == 0)
            {
              if (j == 0 && i > 0)
              {
                gridCell.setBackground(Color.WHITE);
              }  
              gridCell.setEnabled(false);            
            } // end if
          } // end inner for
        } // end outer for	
      } // end if
      
      // Loop through the candidates buttons and disable the row where the current button is clicked
      else  
      {   	  
    	for (int i=0; i<qArr.length; i++)
        {
          for (int j=0; j<qArr[0].length; j++)
          {
            cellToDisable = qArr[i][j];  
        
            if (btn.getText().equals(btnText))
            {   
              if (i == disable)
              {
            	cellToDisable.setEnabled(false);
              }
            } // end if
          } // end inner for
        } // end outer for
      } // end else
      
      if (e.getSource() == countVotesFairly)
      {
      	for (int i=0; i<qArr.length; i++)
        {
          for (int j=0; j<qArr[0].length; j++)
          { 
            if (i > 0 && j > 0)
            {   
              if (!qArr[i][j].getText().equals(qArr[i][j].getActionCommand()))
              {
                qArr[i][j].setText(qArr[i][j].getText() + " / " + qArr[i][j].getActionCommand());
                qArr[i][j].setBackground(Color.WHITE);
              }
            } // end if
          } // end inner for
        } // end outer for  
        countVotesFairly.setVisible(false);
      }
    } // end actionPerformed
  } // end class ButtonListener

  //**********************************************************

  // Move check marks in table s so that the favorite candidate wins.
  // To avoid the appearance of fraud:
  // 1. Don't take away from a candidate who has received just 1 vote.
  // 2. Take away from candidates such that the final rigged count
  // is as close to the correct count in terms of percentage of votes for
  // each candidate.

  private void rigTheVote(
    Candidate favorite, Candidate other1, Candidate other2)
  {
    boolean prevMoveWorked = true;
    
    while (prevMoveWorked &&
      (favorite.getRiggedCount() <= other1.getRiggedCount() ||
       favorite.getRiggedCount() <= other2.getRiggedCount()))
    {
      if (other1.getRiggedCount() <= 1 && other2.getRiggedCount() <= 1)
      {
        prevMoveWorked = false;
      }
      else if (other1.getRiggedCount() <= 1)
      {
        prevMoveWorked = moveVote(other2, favorite);
      }
      else if (other2.getRiggedCount() <= 1)
      {
        prevMoveWorked = moveVote(other1, favorite);
      }
      
      else if (other1.getPercentOfCorrectCount() > other2.getPercentOfCorrectCount())
      {
        prevMoveWorked = moveVote(other1, favorite);
      }
      else if (other2.getPercentOfCorrectCount() > other1.getPercentOfCorrectCount())
      {
        prevMoveWorked = moveVote(other2, favorite);
      }
      
      // Rigged count percentages are equal, so use rigged count totals.
      else if (other1.getRiggedCount() > other2.getRiggedCount())
      {
        prevMoveWorked = moveVote(other1, favorite);
      }
      else if (other2.getRiggedCount() > other1.getRiggedCount())
      {
        prevMoveWorked = moveVote(other2, favorite);
      }
      else
      {
        prevMoveWorked =
          moveVote((Math.random() < .5) ? other1 : other2, favorite);
      }
    } // end while
    
    // Hide pointers for the remaining rows in the RPC process.
    for (int row=0; row<r.getRowCount(); row++)
    {
      if (!((String) r.getValueAt(row, 1)).isEmpty() &&
          !((String) r.getValueAt(row, 2)).isEmpty())
      {
        r.setValueAt("", row, Math.random() < .5 ? 1 : 2);
      }
    } // end for
  } // end rigTheVote
  
  //**********************************************************

  // In table s, move a check mark from the fromCand's column to the
  // toCand's column, but only if it's possible to hide the movement
  // during the RPC phase.
  
  private boolean moveVote(Candidate fromCand, Candidate toCand)
  {
    int fromCandRow;
    int toCandRow;
    int hideQPtrRow1;
    int hideQPtrRow2;
    String ptr;        // table r pointer, in the form "(row,col)"
    
    if (((fromCandRow = getRowForMoving(CHECK, fromCand.getId())) == -1) ||
      ((toCandRow = getRowForMoving("", toCand.getId())) == -1) ||
      ((hideQPtrRow1 =
        getRowForBalance(toCand.getId(), fromCandRow, toCandRow, -1)) == -1) ||
      ((hideQPtrRow2 =
        getRowForBalance(toCand.getId(), fromCandRow, toCandRow, hideQPtrRow1)) == -1))
    {
      return false;
    }
    else
    {
      // For other candidate, hide check in table s and s-pointer in table r.
      ptr = (String) r.getValueAt(fromCandRow, 2);
      s.setValueAt("", getPtrX(ptr)-1, getPtrY(ptr)-1);
      r.setValueAt("", fromCandRow, 2);
      fromCand.adjustRiggedCount(-1);
      
      // For favorite, display check in table s and hide s-pointer in table r.
      ptr = (String) r.getValueAt(toCandRow, 2);
      s.setValueAt(CHECK, getPtrX(ptr)-1, getPtrY(ptr)-1);
      r.setValueAt("", toCandRow, 2);
      toCand.adjustRiggedCount(+1);
      
      r.setValueAt("", hideQPtrRow1, 1);
      r.setValueAt("", hideQPtrRow2, 1);
      
      return true;
    }
  } // end moveVote

  //**********************************************************

  // In table r, search for a row with both pointers displayed where
  // flag = searchSymbol and s-pointer's second value = targetId.
  
  private int getRowForMoving(String searchSymbol, int targetId)
  {
    boolean found = false;
    int row = 0;
    int foundRow = -1;
    
    while (!found && row < r.getRowCount())
    {
      if (!((String) r.getValueAt(row, 1)).isEmpty() &&
        !((String) r.getValueAt(row, 2)).isEmpty() &&
        r.getValueAt(row, 0).equals(searchSymbol) &&
        getPtrY((String) r.getValueAt(row, 2)) == targetId)
      {
        found = true;
        foundRow = row;
      }
      else
      {
        row++;
      }
    } // end while
    
    return foundRow;
  } // end getRowForMoving

  //**********************************************************

  // This method is intended to balance out the left-right distribution
  // of hidden pointers in table r during RPC. It offsets
  // getRowForMoving, which hides s-pointers exclusively.
  //
  // In table r, search for a row with both pointers displayed, not
  // including the 3 passed-in skipRow parameters, with this order of
  // preference:
  // 1. a flagged row for the favorite candidate
  // 2. an unflagged row for one of the other candidates
  // 3. another row (the first one encountered)
  
  private int getRowForBalance(
    int favoriteId, int skipRow1, int skipRow2, int skipRow3)
  {
    boolean found = false;
    int row;
    int foundRow = -1;
    
    // Search for a flagged row for the favorite candidate.
    row = 0;
    while (!found && row < r.getRowCount())
    {
      if ((row != skipRow1) && (row != skipRow2) && (row != skipRow3) &&
        !((String) r.getValueAt(row, 1)).isEmpty() &&
        !((String) r.getValueAt(row, 2)).isEmpty() &&
        r.getValueAt(row, 0).equals(CHECK) &&
        getPtrY((String) r.getValueAt(row, 2)) == favoriteId)
           
      {
        found = true;
        foundRow = row;
      }
      else
      {
        row++;
      }
    } // end while
    
    // Search for an unflagged row for one of the other candidates.
    row = 0;
    while (!found && row < r.getRowCount())
    {
      if ((row != skipRow1) && (row != skipRow2) && (row != skipRow3) &&
        !((String) r.getValueAt(row, 1)).isEmpty() &&
        !((String) r.getValueAt(row, 2)).isEmpty() &&
        r.getValueAt(row, 0).equals("") &&
        getPtrY((String) r.getValueAt(row, 2)) != favoriteId)
           
      {
        found = true;
        foundRow = row;
      }
      else
      {
        row++;
      }
    } // end while
    
    // Search for another row (the first one encountered).
    row = 0;
    while (!found && row < r.getRowCount())
    {
      if ((row != skipRow1) && (row != skipRow2) && (row != skipRow3) &&
        !((String) r.getValueAt(row, 1)).isEmpty() &&
        !((String) r.getValueAt(row, 2)).isEmpty())
      {
        found = true;
        foundRow = row;
      }
      else
      {
        row++;
      }
    } // end while
    
    return foundRow;
  } // end getRowForBalance

  //**********************************************************

  // Extract the x value from a given "(x,y)" pointer string.

  private int getPtrX(String ptr)
  {
    int commaIndex;
    String numStr;
    
    commaIndex = ptr.indexOf(',');
    numStr = ptr.substring(1, commaIndex);
    try
    {
      return Integer.parseInt(numStr);
    }
    catch (NumberFormatException e)
    {
      System.out.println("Unable to parse " + numStr +
        " to form an integer row value.");
      return -1;
    }
  } // end getRow
  
  //**********************************************************

  // Extract the y value from a given "(x,y)" pointer string.

  private int getPtrY(String ptr)
  {
    int commaIndex;
    String numStr;
    
    commaIndex = ptr.indexOf(',');
    numStr = ptr.substring(commaIndex + 1, ptr.length() - 1);
    try
    {
      return Integer.parseInt(numStr);
    }
    catch (NumberFormatException e)
    {
      System.out.println("Unable to parse " + numStr +
        " to form an integer column value.");
      return -1;
    }
  } // end getCol
  
  //**********************************************************
} // end class ScantegrityAttack
