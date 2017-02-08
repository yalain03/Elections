/*************************************************************
* Candidate.java
* John Dean
*
* This class keeps track of votes for a particular candidate.
*************************************************************/

package elections;

public class Candidate
{
  private final int id;  // candidate's identification number
  private int correctCount; // correct number of votes
  private int riggedCount;    // rigged number of votes

  //**********************************************************

  public Candidate(int id, int correctCount)
  {
    this.id = id;
    this.correctCount = correctCount;
    riggedCount = correctCount;
  }

  //**********************************************************

   public int getId()
  {
    return this.id;
  }
   
   //*********************************************************
   
   public void setRiggedCount(int count) {
     riggedCount += count;
   }

   //**********************************************************
   
   public void setCorrectVotes(int votes) {
     correctCount = votes;
   } // end function setCorrectVotes
   
   //**********************************************************
   
   public int getCorrectVotes() {
     return correctCount;
   }
   
   //**********************************************************

   public int getRiggedCount()
  {
    return this.riggedCount;
  }

  //**********************************************************

  // Adjust the candidate's count for the rigged election.

  public void adjustRiggedCount(int adjustment)
  {
    this.riggedCount += adjustment;
  } // end adjustRiggedCount

  //**********************************************************

  // Return the candidate's votes in the rigged election as compared
  // to the candidate's actual, correct number of votes.

  public double getPercentOfCorrectCount()
  {
    return (double) riggedCount / correctCount;
  }
} // end class Candidate