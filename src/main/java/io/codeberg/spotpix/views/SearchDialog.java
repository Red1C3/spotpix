package io.codeberg.spotpix.views;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class SearchDialog extends JDialog{
    private final static String COLOR_SRCH_STR="Color Search";

    private JPanel colorPanel;
    public SearchDialog(){
        super(ViewerRoot.instance(),"Search",ModalityType.APPLICATION_MODAL);

        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        setupPanels();

        tabbedPane.add(colorPanel,COLOR_SRCH_STR);
        add(tabbedPane,BorderLayout.CENTER);
        setSize(300, 100);
        setVisible(true);
    }

    private void setupPanels(){
        colorPanel=new JPanel();
    }
    
}
