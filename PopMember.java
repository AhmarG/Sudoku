/**
 * @author Benjamin Clark, Ahmar Gordon, Drew Morton, Alice McRae
 * @version 1.0
 *
 */
public class PopMember
{

    //fields
    private int score;
    private int[] entries;

    /**
     * Initializes a new population member with the given size.
     * 
     * @param size the size of the population member.
     */
    public PopMember(int size)
    {
        score = 0;
        entries = new int[size];
    }

    /**
     * A getter method for the score of this member.
     * 
     * @return score the score of the population member
     */
    public int getScore()
    {
        return score;
    }

    /**
     * A setter method for the score for this member.
     * 
     * @param score the score to set
     */
    public void setScore(int score)
    {
        this.score = score;
    }

    /**
     * A getter method for the entries of this member.
     * 
     * @return the entries
     */
    public int[] getEntries()
    {
        return entries;
    }

    /**
     * A setter method for the entries of this member.
     * 
     * @param entries the entries to set
     */
    public void setEntries(int[] entries)
    {
        this.entries = entries;
    }

    /**
     * When given a specific index of the member, 
     * it returns the value of that entry.
     * 
     * @return the value at the specified index.
     * @param index the index of the entry.
     */
    public int getEntry(int index)
    {
        return entries[index];
    }

    /**
     * @param index the index we want to change
     * @param value the value to change the index to.
     */
    public void setEntry(int index, int value)
    {
        entries[index] = value;
    }

    public int getSize()
    {
        return entries.length;
    }

    public void copy(PopMember mem)
    {
        score = mem.score;
        for(int i = 0; i < mem.entries.length; i++)
        {
            entries[i] = mem.entries[i];
        }
    }

    public void printEntries()
    {
        for(int i = 0; i < entries.length; i++)
        {
            System.out.print(entries[i] + " ");

            if(i % 9 == 8)
            {
                System.out.println();
            }
            else if(i % 3 == 2)
            {
                System.out.print(" ");
            }
            if(i % 27 == 26)
            {
                System.out.println();
            }
        }
        System.out.println("Score: " + score);

    }

    public static void main(String args[])
    {
        PopMember p = new PopMember(81);
        for (int i = 0; i < 81; i++)
        {

            p.setEntry(i, (int) (Math.random() * 9 + 1));

        }

        p.printEntries();

        p.setScore(78);
        System.out.println("The score was " + p.getScore());
    }





}
