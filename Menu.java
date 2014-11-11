/**
 * 
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Ahmar Gordon, Ben Clark, Drew Morton, Alice McRae
 *
 */

public class Menu implements ActionListener
{
    public static final int TEXTBOXSIZE = 2;
    public static final int BOARD_SIZE = 81;
    
    JTextField[] jTFInputArray = new JTextField[BOARD_SIZE];
    
    PuzzleSolver p;
    


    /**
     * @param args
     */
    public Menu(PuzzleSolver p)
    {
        JFrame sudokuFrame = new JFrame();
        sudokuFrame.setLocation(0, 0);
        sudokuFrame.setSize(500,500);
        sudokuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sudokuFrame.setTitle("Sudoku Interface");

        sudokuFrame.add(initializeButtonPanel(), BorderLayout.LINE_END);
        sudokuFrame.add(initializeSudokuPanel(), BorderLayout.CENTER);
        //sudokuFrame.add(initializeResultPanel(), BorderLayout.LINE_START);
        //sudokuFrame.add(initializeTextFieldPanel(), BorderLayout.PAGE_START);
        sudokuFrame.pack();
        sudokuFrame.setVisible(true);
        this.p = p;
    }

    public JPanel initializeButtonPanel()
    {
        JPanel buttonPanel = new JPanel();
        JButton jbGo = new JButton("Solve!");
        JButton jbClear = new JButton("Clear");

        jbGo.setName("calculateButton");
        jbClear.setName("clearButton");

        jbGo.addActionListener(this);
        jbClear.addActionListener(this);

        buttonPanel.add(jbGo);
        buttonPanel.add(jbClear);

        return buttonPanel;
    }
    
    public JPanel initializeSudokuPanel()
    {
        JPanel sudokuBoardPanel = new JPanel();
        sudokuBoardPanel.setLayout(new GridLayout(9,9)); 
        
        for (int i = 0; i < jTFInputArray.length; i++)
        {
            jTFInputArray[i] = new JTextField(TEXTBOXSIZE);
            sudokuBoardPanel.add(jTFInputArray[i]);
        }
        
        
        
        return sudokuBoardPanel;
    }

    /**
     * A program designed to practice working with Graphic User Interfaces.
     * 
     * @param jbTemp the temporary button object.
     */
    public void updateClicked(JButton jbTemp)
    {
        
        try
        {
           
            if (jbTemp.getName().equals("calculateButton"))
            {
                p.niceRead();
                p.initializePuzzle();
                p.geneticAlgorithm();
            }
            else if (jbTemp.getName().equals("clearButton"))
            {
                p.clear();
            }
        } 
        catch (Exception e)
        {

        }
    }
    
    public void setNumbers(int[] numbers)
    {
        
        for (int i = 0; i < jTFInputArray.length; i++)
        {
            if (numbers[i] == 0)
                jTFInputArray[i].setText("");
            else
                jTFInputArray[i].setText(numbers[i] + "");
        }
    }
    
    public int[] getNumbers()
    {
        int[] numbers = new int[BOARD_SIZE];
        
        for (int i = 0; i < jTFInputArray.length; i++)
        {
            numbers[i] = 0;
            if (jTFInputArray[i].getText().length() == 0)
                numbers[i] = 0;
            else
                numbers[i] = Integer.parseInt(jTFInputArray[i].getText());
        }
        
        return numbers;
    }

    public static void main(String[] args)
    {
        //Menu menu1 = new Menu();
    }

    public void actionPerformed(ActionEvent e)
    {
        JButton jbTemp = (JButton) e.getSource();
        
        updateClicked(jbTemp);
    }

}
