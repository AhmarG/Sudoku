import java.util.Scanner;
import java.io.FileReader;
/**
 * @author Ahmar Gordon, Ben Clark, Drew Morton, Alice McRae
 *
 */
public class PuzzleSolver 
{
    private static final int LENGTH = 81;
    private static final int DIM = 3;
    private static final int DIGITS = 9;
    private boolean [] [] initialUsedRow;
    private boolean [] [] initialUsedColumn;
    private boolean [] [] initialUsedBlock;
    private boolean [] [] workingUsedRow;
    private boolean [] [] workingUsedColumn;
    private boolean [] [] workingUsedBlock;
    private int [] initialValues;
    private Population population;
    
    private Menu mainMenu = new Menu(this);

    public PuzzleSolver()
    {
        initialUsedRow = new boolean [DIGITS] [DIGITS + 1];
        initialUsedColumn = new boolean [DIGITS] [DIGITS + 1];
        initialUsedBlock = new boolean [DIGITS] [DIGITS + 1];
        workingUsedRow = new boolean [DIGITS] [DIGITS + 1];
        workingUsedColumn = new boolean [DIGITS] [DIGITS + 1];
        workingUsedBlock = new boolean [DIGITS] [DIGITS + 1];
        initialValues = new int [LENGTH];
        population = new Population(false, LENGTH, 30);
    }
    public int getRow(int squareNum)                { return squareNum / DIGITS; }

    public int getColumn(int squareNum)             { return squareNum % DIGITS; }

    public int getBlock(int rowNum, int columnNum)  { return ((rowNum / DIM) * DIM) + columnNum / DIM; }

    public int getSquareNum(int row, int col)       {  return DIGITS * row + col; }

    public void permute(int [] array, int howMany)
    {
        int index;
        int temp;
        for(int i = howMany - 1; i > 0; i--)
        {
            index = (int)(Math.random()* i);
            temp = array [index];
            array [index] = array [i];
            array [i] = temp;
        }
    }
    public void initArray(int [] array, int howMany, boolean startsZero)
    {
        int increment;
        if (startsZero == true)
            increment = 0;
        else
            increment = 1;
        for (int i = 0; i < howMany; i++)
            array [i] = i + increment;
    }
    public void setValue(int row, int column, int block, int value)
    {
        workingUsedRow [row] [value] = true;
        workingUsedColumn [column] [value] = true;
        workingUsedBlock [block] [value] = true;
    }
    public boolean checkValue(int row, int column, int block, int value)
    {
        if(workingUsedRow [row] [value] == true)
            return false;
        if(workingUsedColumn [column] [value] == true)
            return false;
        if(workingUsedBlock [block] [value] == true)
            return false;
        return true;
    }
    public void attemptFill(PopMember child)
    {
        int score = 0;
        //mixes up numbers 0-81
        int [] helper1 = new int[81];
        //mixes up digits 1-9
        int [] helper2 = new int[9];
        initArray(helper1, 81, true);
        initArray(helper2, 9, false);
        permute(helper1, 81);
        int square;
        int value;
        int row;
        int column;
        int block;
        initializeMember(child);
        int []baby = child.getEntries();
        for (int i = 0; i < LENGTH; i++)
        {
            square = helper1[i];
            if (baby[square] != 0)
                score++;
            else
            {
                permute(helper2, 9);
                row = getRow(square);
                column = getColumn(square);
                block = getBlock(row, column);
                for (int j = 0; j < 9; j++)
                {
                    value = helper2[j];
                    if (checkValue(row, column, block, value) == true)
                    {
                        setValue(row, column, block, value);
                        baby[square] = value;
                        score++;
                        break;
                    }

                }
            }
            child.setScore(score);
        }
    }

    /**
     * This method places fixed values in the 
     * child. i.e. The values the sudoku puzzle 
     * already has.
     * 
     * @param papa 
     */
    public void initializeMember(PopMember papa)
    {
        int [] array = papa.getEntries();
        for (int i = 0; i < array.length; i++)
            array [i] = initialValues [i];
        for (int i = 0; i < workingUsedRow.length; i++)
        {
            for (int j =0; j < workingUsedRow[i].length; j++)
            {
                workingUsedRow[i][j] = initialUsedRow[i][j];
                workingUsedColumn[i][j] = initialUsedColumn[i][j];
                workingUsedBlock[i][j] = initialUsedBlock[i][j];
            }
        }
    }
    public void initializePuzzle()
    {
        int row;
        int column;
        int block;
        for(int i = 0; i < initialValues.length; i++)
        {
            if(initialValues[i] != 0)
            {
                row = getRow(i);
                column = getColumn(i);
                block = getBlock(row, column);
                initialUsedRow[row][initialValues [i]] = true;
                initialUsedColumn[column][initialValues [i]] = true;
                initialUsedBlock[block][initialValues [i]] = true;
            }
        }
    }
    public void uglyRead(String filename)
    {
        Scanner scan = null;
        try 
        {
            scan = new  Scanner(new FileReader(filename));
        }
        catch (Exception e)
        {
            System.out.println("Problem with file " + filename);
            System.exit(0);
        }
        int index = 0;
        while (scan.hasNextInt())
        {
            initialValues[index] = scan.nextInt();
            ++index;
        }
    }
    
    public void niceRead()
    {
        initialValues = mainMenu.getNumbers();
    }

    public void initializePopulation()
    {
        PopMember person;
        for (int i = 0; i < population.getSize(); i++)
        {
            person = population.getMember(i);
            initializeMember(person);
            attemptFill(person);
            population.insert(i);
        }
        person = population.getMember(0);
        person.printEntries();
        System.out.println("This is the HIGHEST score in our population...\n");
        person = population.getMember(population.getSize() - 1);
        person.printEntries();
        System.out.println("This is the LOWEST score in our population...\n");
    }

    public void geneticAlgorithm()
    {
        initializePopulation();
        PopMember mama, papa, child = population.getMember(0);
        int position;
        int childNum = 0;
        while (population.getMember(0).getScore() < LENGTH)
        {
            mama = population.selectBiasedParent();

            do 
            { 
                papa = population.selectBiasedParent();
            } while (papa == mama);

            child = population.getMember(population.getSize());
            cross(mama, papa, child);
            position = population.insert(population.getSize());

            if (position == 0)
            {
                child.printEntries();
                System.out.println("Child number " + childNum + " was born! :D");
                if (child.getScore() == LENGTH)
                    break;
            }

            childNum++;


        }
        mainMenu.setNumbers(child.getEntries());
    }

    /**
     * This method is our first version of a cross method. It randomly 
     * inputs parts of mama and papa into child till filled.
     * 
     * @param mama this will be crossed with papa to overwrite child.
     * @param papa this will be crossed with mama to overwrite child.
     * @param child this population member will be overwritten 
     * and reborn as a new product of mama and papa!
     */
    public void cross(PopMember mama, PopMember papa, PopMember child)
    {
        int score = 0;
        //mixes up numbers 0-81
        int [] helper1 = new int[81];
        //mixes up digits 1-9
        int [] helper2 = new int[11];
        initArray(helper1, 81, true);
        initArray(helper2, 9, false);
        permute(helper1, 81);
        int square;
        int value;
        int row;
        int column;
        int block;
        initializeMember(child);
        int []baby = child.getEntries();
        for (int i = 0; i < LENGTH; i++)
        {
            square = helper1[i];
            if (baby[square] != 0)
            {
                score++;
            }
            else
            {
                if (mama.getEntry(square) == papa.getEntry(square) 
                        && mama.getEntry(square) != 0)
                    helper2[9] = mama.getEntry(square);
                else if (Math.random() < 0.33)
                    helper2[9] = mama.getEntry(square);
                else if (Math.random() > 0.66)
                    helper2[9] = papa.getEntry(square);
                permute(helper2, 9);
                row = getRow(square);
                column = getColumn(square);
                block = getBlock(row, column);
                for (int j = 9; j >= 0; j--)
                {
                    if (helper2[j] != 0)
                    {
                        value = helper2[j];
                        if (checkValue(row, column, block, value) == true)
                        {
                            setValue(row, column, block, value);
                            baby[square] = value;
                            score++;
                            break;
                        }
                    }
                }
            }
        }
        child.setScore(score);
    }

    public void clear()
    {

        for(int i = 0; i < DIGITS; i++)
        {
            for(int j = 0; j < DIGITS + 1; j++)
            {
                initialUsedRow[i][j] = false;
                initialUsedColumn[i][j] = false;
                initialUsedBlock[i][j] = false;
            }
        }
        for(int z = 0; z < LENGTH; z++)
        {
            initialValues[z] = 0;
        }
        mainMenu.setNumbers(initialValues);
    }

    public static void main(String[] args)
    {
        
        
        PuzzleSolver p = new PuzzleSolver();
        //reads the puzzle into the initialize values
        //p.uglyRead("PuzzleEvil01.txt");
        
        
        //fills up the initialized used arrays
        
        
        //p.initializePuzzle();
        //YAY GENETIC ALGORITHMS!
        //p.geneticAlgorithm();
    }
}
