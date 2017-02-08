/*************************************************************
* ScanReadyToVote.java
* John Dean
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
import java.util.ArrayList;
import java.util.Random;

public class ScanReadyToVote extends JFrame
{
  private static final int WIDTH = 850;
  private static final int HEIGHT = 390;
  private static final String CHECK = "\u2713";

  private JTable p;    // p table's data 
  private JPanel q;    // displays q table's data
  private JButton[][] qArr; // manipulates q table's data
  private JTable r;    // r table's data 
  private JTable s;    // s table's data
  private JButton reset = new JButton("Display initial setup");
  private String[] aliceCcs = {"WT9", "KMT", "CH7", "WJL", "M39"};
  private String[] bobCcs = {"7LH", "TC3", "3TW", "KWK", "LTM"};
  private String[] carlCcs = {"JNC", "J3K", "9JH", "H7T", "HNN"};
  
  // Candidates stored in array with 1st candidate matching 1st column
  // in tables p & s, 2nd candidate matching 2nd column, etc.
  private Candidate[] candidates;
  
  private JButton[] candButtons;
  private JButton resetButton;
  private JButton countTheVotesButton;
  private JButton fairVote = new JButton("Count the votes fairly");
  private String[][] ptrsArr = new String[15][3];
  private String startPtr;
  private String destPtr;
  
  //**********************************************************

  public ScanReadyToVote()
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
    JPanel topPanel;    // holds table p
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
    
     
    // This is another part of what I did
    // Yves
    // filling th r table with data from file

    l = 0;
    line = null;
    String[][] ptrsArr = new String[15][3];
    String[][] voidArr = new String[15][3];
    for (int a = 0; a < 15; ++a) {
      for (int b = 0; b < 3; ++b)
        voidArr[a][b] = "";
    }
    try {
      BufferedReader br = new BufferedReader(new FileReader("pointersTable.txt"));
      while ((line = br.readLine()) != null) { 
        arr = line.split(":");
        ptrsArr[l] = arr;
        ++l;
      }
      
      // See what you should put here
      r = new JTable(voidArr, new String[] {"Flag", "Q-Pointer", "S-Pointer"});
      br.close();
    }
    catch (Exception e) {
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
    }

    // Yves
    //part ends up here

/*
    r = new JTable(
      new String[][]
      {
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
      },
      new String[] {"Flag", "Q-Pointer", "S-Pointer"});
*/

    s = new JTable(
      new String[][]
      {
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""},
        {"", "", ""}
      },
      new String[] {"Alice", "Bob", "Carl"});

    setTableCellAlignment(p, renderer, JLabel.CENTER);
    tableTitle = new JLabel("Table P", SwingConstants.CENTER);
    tableTitle.setBorder(new EmptyBorder(10, 0, 0, 0));
    pPanel = new JPanel(new BorderLayout());
    pPanel.add(p.getTableHeader(), BorderLayout.NORTH);
    pPanel.add(p, BorderLayout.CENTER);
    pPanel.add(tableTitle, BorderLayout.SOUTH);
    
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

    // Add pPanel to a FlowLayout panel to avoid table expansion
    topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    topPanel.add(pPanel);
//    add(topPanel, BorderLayout.NORTH);

    middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    middlePanel.add(qPanel);
    middlePanel.add(rPanel);
    middlePanel.add(sPanel);
    add(middlePanel, BorderLayout.CENTER);
    
    candButtons = new JButton[s.getColumnCount()];
    buttonListener = new ButtonListener();
    buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

    buttonPanel.add(fairVote);
    
    fairVote.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // reading CCs from file
        String[][] data = new String[qArr.length-1][qArr[0].length-1];
        try {
          int i = 0;
          String line;
          BufferedReader br = new BufferedReader(new FileReader("candidatesCcs.txt"));
          while ((line = br.readLine()) != null) { 
            data[i] = line.split(",");
            ++i;
          } // end try
        } // end while
        catch (Exception ex) {
          System.out.println(ex.getClass());
          System.out.println(ex.getMessage());
        } // end catch
        
        ArrayList<String> audits = new ArrayList<>();
        
        // disabling unused ballot's buttons and showing CCs
        for (int i = 1; i < qArr.length; ++i){
          if ((qArr[i][1]).isEnabled() == true) {  
            audits.add(qArr[i][0].getText());
            for (int j = 1; j < qArr[0].length; ++j) {
              (qArr[i][j]).setText((data[i-1][j-1]));
              (qArr[i][j]).setEnabled(false);
            } // end for j
          } // end if 
        } // end for i
        
        // filling the R table with check marks and other content
        // initializing content first by filling the future table 
        // used for R table
        
        int x = 0;
        String line = null;
        String[] arr;        
        try {
          BufferedReader br = new BufferedReader(new FileReader("pointersTable.txt"));
          while ((line = br.readLine()) != null) { 
            arr = line.split(":");
            ptrsArr[x] = arr;
            ++x;
          } // end while
          br.close();
        } // end try
        catch (Exception ex) {
          System.out.println(ex.getClass());
          System.out.println(ex.getMessage());
        } // end catch
        
        for (int i = 1; i < qArr.length; ++i) {
          for (int j = 1; j < qArr[0].length; ++j) {
            if (!qArr[i][j].getText().isEmpty()) {
              String cd = "(000" + i + "," + j + ")";
              for (int k = 0; k < ptrsArr.length; ++k) {
                if (cd.equals(ptrsArr[k][1])) {
                  ptrsArr[k][0] = CHECK;
                  int n = 0;
                  while (n < audits.size()) {
                    if (cd.substring(1, 5).equals(audits.get(n))) {
                      ptrsArr[k][0] = "audit";
                    }
                    ++n;
                  } // end while                 
                } // end if                
              } // end for k
            } // end if
          } // end for j
        } // end for i
        
        // setting the check marks in S table
        Random rand = new Random();
        for (int i = 0; i < 15; ++i) {
          for (int j = 0; j < 3; ++j) {
            r.setValueAt(ptrsArr[i][j], i, j);            
          } // end for j
          if (r.getValueAt(i, 0).equals(CHECK)) {
            String pointer = r.getValueAt(i, 2).toString();
            s.setValueAt(CHECK, Character.getNumericValue(pointer.charAt(1)) - 1, 
              Character.getNumericValue(pointer.charAt(3)) - 1);
            r.setValueAt("", i, rand.nextInt(2) + 1);
          } // end if
          if (!r.getValueAt(i, 0).equals("audit") && 
              !r.getValueAt(i, 0).equals(CHECK)) {
            r.setValueAt("", i, rand.nextInt(2) + 1);
          } // end if
        } // end for j
        
        // COUNT EACH CANDIDATE'S VOTES HERE
        for (int i = 0; i < s.getColumnCount(); ++i) {
          int votes = 0;
          int j = 0;
          for (j = 0; j < s.getRowCount(); ++j) {
            if (((String)s.getValueAt(j, i)).equals(CHECK)) {
              ++votes;
            }
          }
          candidates[i].setCorrectVotes(votes); 
        }        
        for (int i = 0; i < s.getColumnCount(); ++i) {
          int count = 0;
          for (int j = 0; j < s.getRowCount(); ++j) {
            if (s.getValueAt(j, i).equals(CHECK)) {
              ++count;
            }
          }
          candidates[i].setRiggedCount(count);
        }
      } // end actionPerformed
    });

    for (int i=0; i<s.getColumnCount(); i++)
    {
      candButtons[i] = new JButton("Make candidate " +
        (i + 1) + " win");
      candButtons[i].addActionListener(buttonListener);
      buttonPanel.add(candButtons[i]);
    } // end for

    buttonPanel.add(reset);
    add(buttonPanel, BorderLayout.SOUTH);
    reset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        new ScanInitial();
      }
    });
  } // end createContents
  
  //*********************************************************

  /*
  
  class Listener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      int[] earns = countEarned();
      int index = -1;
      for (int i = 0 ; i < s.getColumnCount(); ++i) {
        if (e.getSource() == candButtons[i]) {
          index = i;
          break;
        } // end if
      } // end for i
      if (isWinner(index, earns)) {
        JOptionPane.showMessageDialog(null, "This candidate is already the winner",
          "Attention", JOptionPane.WARNING_MESSAGE);
      }
    }
  }
  
  private int[] countEarned() {
    int earns[] = {0, 0, 0};
    for (int i = 0; i < s.getRowCount(); ++i) {
      for (int j = 0; j < s.getColumnCount(); ++j) {
        if (s.getValueAt(i, j).equals(CHECK)) {
          earns[i]++;
        } // end if
      } // end for j
    } // end for i
    return earns;
  } // end function countEarned
  
  private boolean isWinner(int index, int[] earns) {
    boolean wins = true;
    for (int i = 0; i < earns.length && i != index; ++i) {
      wins &= earns[index] > earns[i];
    }
    return wins;
  }
  
  */ // This is for tricking the vote
  
  //*************************************************************

  private JPanel createTableQPanel()
  {
    JPanel q;
    JButton cell;
    
    qArr = new JButton[][]
    {
      {new JButton("Ballot ID"), new JButton(),
        new JButton(), new JButton()},
      {new JButton("0001"), new JButton("Bob"),
        new  JButton("Alice"), new JButton("Carl")},
      {new JButton("0002"), new JButton("Carl"),
        new JButton("Bob"), new JButton("Alice")},
      {new JButton("0003"), new JButton("Carl"),
        new JButton("Alice"), new JButton("Bob")},
      {new JButton("0004"), new JButton("Bob"),
        new JButton("Carl"), new JButton("Alice")},
      {new JButton("0005"), new JButton("Alice"),
        new JButton("Carl"), new JButton("Bob")}
    };
    
    // associating event to all of the the buttons
    // in qArr table
    //  \\\\\\start here
    
    for (int i = 1; i < qArr.length; ++i) {
      for (int j = 1; j < qArr[0].length; ++j) {
        qArr[i][j].addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String candidate = ((JButton)e.getSource()).getText();
            boolean found = false; // to check if the button was found
            // getting the row of the button clicked to identify
            // the candidate's cc to be displayed
            JButton source = (JButton)e.getSource();
            int k = 0, l = 0;
            for (k = 1; k < qArr.length; ++k) {
              for (l = 1; l < qArr[0].length; ++l) {                
                if (source == qArr[k][l]) {
                  found = true;
                  if (source.getText().equals("Bob")) {
                    source.setText(bobCcs[k-1]);
                  }
                  if (source.getText().equals("Alice")) {
                    source.setText(aliceCcs[k-1]);
                  }
                  if (source.getText().equals("Carl")) {
                    source.setText(carlCcs[k-1]);
                  }
                  break;
                } // end if 
              } // end for l   
              if (found) {
                break;                
              } // end if found                
            } // end for k
            // disabling the button clicked
            source.setEnabled(false); 
            // making all the other ballot's buttons
            // disappear
            for (int h = 1; h < qArr[0].length; ++h) {
              if (h == l)
                continue;
              else {
                qArr[k][h].setText("");
                qArr[k][h].setEnabled(false);                
              }               
            }           
          } // end function actionPerformed
        });
      } // end for j
    } // end for i
    
    // end of associating event to buttons in qArr

    q = new JPanel(
      new GridLayout(qArr.length, qArr[0].length));
    
    for (int i=0; i<qArr.length; i++)
    {
      for (int j=0; j<qArr[0].length; j++)
      {
        cell = qArr[i][j];
        cell.setMargin(new Insets(1, 2, 1, 2));

        if (i == 0 || j == 0)
        {
          cell.setEnabled(false);
        }
/*        
        if (i >= 1)
        {
          cell.setBackground(Color.WHITE);
        }
*/
        q.add(cell);
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
      boolean finished = false;
      
      for (int i=0; !finished && i<s.getColumnCount(); i++)
      {
        if (e.getSource() == candButtons[i])
        {
          rigTheVote(candidates[i],
            // to keep the index inside of bounds
            candidates[(i + 1) % 3], candidates[(i + 2) % 3]);
          finished = true;
        }
      } // end for
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
    /*
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
    } // end for  */
    while(!(favorite.getRiggedCount() > other1.getRiggedCount() &&
            favorite.getRiggedCount() > other2.getRiggedCount())) {
      if (other1.getRiggedCount() >= 2) {
        moveVote(other1, favorite);      
      } // end if
      else if(other2.getRiggedCount() >= 2) {
        moveVote(other2, favorite);
      } // end else
    } // end while    
  } // end rigTheVote
  
  //**********************************************************

  // In table s, move a check mark from the fromCand's column to the
  // toCand's column, but only if it's possible to hide the movement
  // during the RPC phase.
  
  private boolean moveVote(Candidate fromCand, Candidate toCand)
  {
    /*
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
      toCand.adjustRiggedCount(1);
      
      r.setValueAt("", hideQPtrRow1, 1);
      r.setValueAt("", hideQPtrRow2, 1);
      
      return true;
    }
    */
    String line = "";
    String[] arr;
    int l = 0;
    try {
      BufferedReader br = new BufferedReader(new FileReader("pointersTable.txt"));
      while ((line = br.readLine()) != null) { 
        arr = line.split(":");
        ptrsArr[l] = arr;
        ++l;
      }
      br.close();
    } // end try
    catch (Exception e) {
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
    } // end catch
    
    int i = 0;
            
    while (fromCand.getId() != candidates[i].getId()) {
      ++i;
    } // end while
    
    System.out.println(i);
    
    fromCand.setRiggedCount(-1);
    toCand.setRiggedCount(1);
    Random rand = new Random();    
    int j = 0;
    while (j < s.getColumnCount() && !s.getValueAt(j, i).equals(CHECK)) {
      ++j;
    } // end while
    System.out.println("j = " + j);
    s.setValueAt("", j, i);
    String cd = "(" + (j + 1) + "," + (i + 1) + ")";
    System.out.println(cd);
    for (int a = 0; a < ptrsArr.length; ++a) {
      if (ptrsArr[a][2].equals(cd)) {
        r.setValueAt("", a, 0);
        r.setValueAt("", a, 2);
        if (((String)r.getValueAt(a, 2)).isEmpty()) {
          r.setValueAt(ptrsArr[a][1], a, 1);
        }
      } // end if
    } // end for
    startPtr = cd;
    i = 0;
    
    while (toCand.getId() != candidates[i].getId()) {
      ++i;
    } // end while
    j = rand.nextInt(s.getRowCount());
    while (s.getValueAt(j, i).equals(CHECK)) {
      j = rand.nextInt(s.getRowCount());
    } // end while
    s.setValueAt(CHECK, j, i);
    destPtr = "(" + (j + 1) + "," + (i + 1) + ")";
    for (int a = 0; a < ptrsArr.length; ++a) {
      if (ptrsArr[a][2].equals(destPtr)) {
        r.setValueAt(CHECK, a, 0);        
        r.setValueAt("", a, 2);
        if (((String)r.getValueAt(a, 2)).isEmpty()) {
          r.setValueAt(ptrsArr[a][1], a, 1);
        }
      } // end if
    } // end for
    return true;
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
} // end class ScanReadyToVote