package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.tree.TreePath;

import analyzer.gui.tree.AsmTreeModel;
import analyzer.gui.tree.JCheckBoxTree;
import analyzer.model.IModel;

public class ResultFrame extends JFrame {
	private IModel model;
	private AsmTreeModel atm;
	private Properties config;
	public ResultFrame(IModel model, Properties config){
		this.model = model;
		this.config = config;
		this.atm = new AsmTreeModel(model);
		
		this.setTitle("Design Parser - Result");
		this.setSize(800,800);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		JScrollPane checkboxPanel = new JScrollPane();
		checkboxPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		checkboxPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		checkboxPanel.setPreferredSize(new Dimension(300,770));
		
		JScrollPane graphPanel = new JScrollPane();
		graphPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graphPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		graphPanel.setPreferredSize(new Dimension(500,770));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setPreferredSize(new Dimension(800, 30));
		
		menuBar.add(new JMenu());
		
		this.setResizable(false);
		
        /* final JCheckBoxTree cbt = new JCheckBoxTree();
        
        cbt.setModel(this.atm);
        checkboxPanel.add(cbt);
        
        cbt.addCheckChangeEventListener(new JCheckBoxTree.CheckChangeEventListener() {
            public void checkStateChanged(JCheckBoxTree.CheckChangeEvent event) {
                System.out.println("event");
                TreePath[] paths = cbt.getCheckedPaths();
                for (TreePath tp : paths) {
                    for (Object pathPart : tp.getPath()) {
                        System.out.print(pathPart + ",");
                    }                   
                    System.out.println();
                }
            }           
        });   */    

		
		this.getContentPane().add(menuBar, BorderLayout.NORTH);
		this.getContentPane().add(checkboxPanel, BorderLayout.WEST);
		this.getContentPane().add(graphPanel, BorderLayout.EAST);
		
		
	}
	
	
}
