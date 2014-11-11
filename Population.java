import java.util.*;
/**
 * Population.java
 * 
 * Used by a generic algorithm to manage its population
 * 
 * @author Benjamin Clark, Ahmar Gordon, Drew Morton, Alice McRae
 * @version 1.0
 */
public class Population implements Iterable<PopMember>
{
	private PopMember[] population;
	private int popSize;
	private boolean loToHi;

	public Population(boolean loIsBetter, int memberSize, int popSize)
	{
		loToHi = loIsBetter;
		this.popSize = popSize;
		population = new PopMember[popSize + 1];
		for (int i = 0; i < population.length; i++)
		{
			population[i] = new PopMember(memberSize);
		}
	}

	public int getSize()
	{
		return popSize;
	}

	public PopMember getMember(int i)
	{
		return population[i];
	}

	public PopMember selectParent()
	{
		int x = (int)(popSize *  Math.random());
		return population[x];
	}

	public PopMember selectParent(int low, int high)
	{
		int x = (int)((high - low + 1) *  Math.random()) + low;
		return population[x];
	}

	public PopMember selectBiasedParent()
	{
		int x = (int)(popSize *  Math.random());
		int y = (int)((popSize + 1) *  Math.random());
		if (x < y)
			return population[x];
		else
			return population[y];
	}
	/**
	 * Precondition: population[] has already been sorted up to x.
	 * and member x has been initialized and scored
	 * Postcondition: x is moved to where it belongs in the population 0-x
	 * @param x
	 * @return
	 */
	public int insert(int x)
	{
		if (loToHi)
		{
			PopMember temp = population[x];
			x--;
			while (x >=0 && population[x].getScore() > temp.getScore())
			{
				population[x + 1] = population[x];
				x--;
			}
			population[x + 1] = temp;
		}
		else
		{
			PopMember temp = population[x];
			x--;
			while (x >=0 && population[x].getScore() < temp.getScore())
			{
				population[x + 1] = population[x];
				x--;
			}
			population[x + 1] = temp;
		}
		return x + 1;
	}

	public Iterator<PopMember> iterator()
	{
		return new PopMemberIterator(); 
	}

	private class PopMemberIterator implements Iterator<PopMember>
	{
		int index;

		private PopMemberIterator()
		{
			index = popSize - 1;
		}
		public boolean hasNext()
		{
			return index > -1;
		}
		public PopMember next()
		{
			index--;
			return population[index + 1];
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	public static void main(String[] args)
	{
		Population p = new Population(false, 81, 5);
		for (int i = 0; i < p.getSize(); i++)
		{
			//System.out.println("hello again" + i);
			PopMember x = p.getMember(i);
			x.setScore((int)(Math.random() * 81));
			System.out.println(p.insert(i) + " " + x.getScore());

		}
		//System.out.println(p.getSize());
		for (PopMember pop : p)
		{
			System.out.println(pop.getScore());
		}
	}
}
