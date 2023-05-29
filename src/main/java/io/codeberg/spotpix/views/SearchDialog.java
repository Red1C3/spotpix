package io.codeberg.spotpix.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class SearchDialog extends JDialog implements ActionListener{
    private final static String COLOR_SRCH_STR="Color Search";

    private JPanel colorPanel;
    private JTextField pathField;
    public SearchDialog(){
        super(ViewerRoot.instance(),"Search",ModalityType.APPLICATION_MODAL);

        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        pathField=new JTextField();
        JButton pathButton=new JButton();
        pathButton.addActionListener(this);
        JPanel pathPanel=new JPanel();
        pathPanel.setLayout(new BorderLayout());
        pathPanel.add(pathField,BorderLayout.CENTER);
        pathPanel.add(pathButton,BorderLayout.EAST);
        setupPanels();

        tabbedPane.add(colorPanel,COLOR_SRCH_STR);
        add(tabbedPane,BorderLayout.CENTER);
        add(pathPanel,BorderLayout.NORTH);
        setSize(300, 100);
        setVisible(true);
    }

    private void setupPanels(){
        colorPanel=new JPanel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
    
}
