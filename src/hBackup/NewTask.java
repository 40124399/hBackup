package hBackup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class NewTask extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	//Colors
	private Color NeonGreen = new Color(0,255,128);
	private Color NeonBlue = new Color(0,200,255);
	private Color NeonRed = new Color(255,102,102);
	private Color Fade = new Color(45,45,45);
	private JTextField textField;
	private JTextField textField_1;
	private JTextField txtVersions;
	private JButton btnBrowse;
	private JButton btnDestination;
	private JButton btnSaveTask;
	private JFileChooser fc;
	private File temp;
	private int versions;
	private JTextField txtBackup;
	private JTextField txtArchive;
	private JTextField txtFreq;
	private static NewTask dialog;
	private JTextField textField_2;
	private JLabel lblTitle;
	private boolean automated;
	private String freq;
	
	public static void main(String[] args)
	{
		try
		{
			dialog = new NewTask();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.setTitle("hBackup - New Task");
			dialog.setAlwaysOnTop(true);
			dialog.setResizable(false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public NewTask()
	{
		setBounds(100, 100, 450, 239);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(Color.black);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblSource = new JLabel("Source");
		lblSource.setBounds(10, 11, 46, 14);
		lblSource.setForeground(NeonGreen);
		contentPanel.add(lblSource);
		
		textField = new JTextField();
		textField.setBounds(93, 8, 237, 20);
		contentPanel.add(textField);
		textField.setEditable(false);
		textField.setFocusable(false);
		textField.setForeground(NeonGreen);
		textField.setBackground(Fade);
		textField.setBorder(new LineBorder(NeonGreen));
		textField.setColumns(10);
		
		JLabel lblDestination = new JLabel("Destination");
		lblDestination.setBounds(10, 36, 85, 14);
		lblDestination.setForeground(NeonGreen);
		contentPanel.add(lblDestination);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(93, 33, 237, 20);
		textField_1.setForeground(NeonGreen);
		textField_1.setBackground(Fade);
		textField_1.setBorder(new LineBorder(NeonGreen));
		contentPanel.add(textField_1);

		JLabel lblVersions = new JLabel("Versions");
		lblVersions.setForeground(new Color(0, 255, 128));
		lblVersions.setBounds(241, 136, 64, 14);
		contentPanel.add(lblVersions);
		
		txtVersions = new JTextField();
		txtVersions.setText("5");
		txtVersions.setForeground(new Color(0, 255, 128));
		txtVersions.setColumns(10);
		txtVersions.setBorder(new LineBorder(NeonGreen));
		txtVersions.setBackground(new Color(45, 45, 45));
		txtVersions.setBounds(298, 133, 32, 20);
		contentPanel.add(txtVersions);
		
		//Buttons
		btnBrowse = new JButton("Source");
		btnBrowse.setBounds(340, 7, 89, 23);
		contentPanel.add(btnBrowse);
		
		btnDestination = new JButton("Destination");
		btnDestination.setBounds(340, 32, 89, 23);
		contentPanel.add(btnDestination);
		
		btnSaveTask = new JButton("Save Task");
		btnSaveTask.setBounds(241, 175, 89, 23);
		contentPanel.add(btnSaveTask);
		
		MouseBuilder(btnBrowse);
		MouseBuilder(btnDestination);
		MouseBuilder(btnSaveTask);
		
		txtBackup = new JTextField();
		txtBackup.setForeground(new Color(0, 255, 128));
		txtBackup.setColumns(10);
		txtBackup.setBorder(new LineBorder(NeonGreen));
		txtBackup.setBackground(new Color(45, 45, 45));
		txtBackup.setBounds(93, 83, 237, 20);
		contentPanel.add(txtBackup);
		
		txtArchive = new JTextField();
		txtArchive.setForeground(new Color(0, 255, 128));
		txtArchive.setColumns(10);
		txtArchive.setBorder(new LineBorder(NeonGreen));
		txtArchive.setBackground(new Color(45, 45, 45));
		txtArchive.setBounds(93, 108, 237, 20);
		contentPanel.add(txtArchive);
		
		JCheckBox checkBox_1 = new JCheckBox("");
		checkBox_1.setForeground(Color.BLACK);
		checkBox_1.setBorder(new LineBorder(NeonGreen));
		checkBox_1.setBackground(Color.BLACK);
		checkBox_1.setBounds(93, 133, 15, 23);
		contentPanel.add(checkBox_1);
		
		JLabel lblFrequency = new JLabel("Frequency");
		lblFrequency.setForeground(new Color(0, 255, 128));
		lblFrequency.setBounds(116, 136, 64, 14);
		contentPanel.add(lblFrequency);
		
		txtFreq = new JTextField();
		txtFreq.setForeground(new Color(0, 255, 128));
		txtFreq.setColumns(10);
		txtFreq.setBorder(new LineBorder(NeonGreen));
		txtFreq.setBackground(new Color(45, 45, 45));
		txtFreq.setBounds(184, 133, 46, 20);
		contentPanel.add(txtFreq);
		
		JLabel lblBackupName = new JLabel("Backup Name");
		lblBackupName.setForeground(new Color(0, 255, 128));
		lblBackupName.setBounds(10, 86, 85, 14);
		contentPanel.add(lblBackupName);
		
		JLabel lblArchiveName = new JLabel("Archive Name");
		lblArchiveName.setForeground(new Color(0, 255, 128));
		lblArchiveName.setBounds(10, 111, 85, 14);
		contentPanel.add(lblArchiveName);
		
		JLabel lblAutomated = new JLabel("Automated");
		lblAutomated.setForeground(new Color(0, 255, 128));
		lblAutomated.setBounds(10, 136, 73, 14);
		contentPanel.add(lblAutomated);
		
		textField_2 = new JTextField();
		textField_2.setForeground(new Color(0, 255, 128));
		textField_2.setColumns(10);
		textField_2.setBorder(new LineBorder(NeonGreen));
		textField_2.setBackground(new Color(45, 45, 45));
		textField_2.setBounds(93, 58, 237, 20);
		contentPanel.add(textField_2);
		
		lblTitle = new JLabel("Title");
		lblTitle.setForeground(new Color(0, 255, 128));
		lblTitle.setBounds(10, 61, 85, 14);
		contentPanel.add(lblTitle);

		ToggleActive(false, txtFreq);
		
		checkBox_1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{ToggleActive(checkBox_1.isSelected(), txtFreq);}
		});
		
		btnBrowse.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fc = new JFileChooser();
				fc.setFileSelectionMode(1);
				Component comp = null;
				File f = new File("C:/");
				fc.setCurrentDirectory(f);
				fc.showOpenDialog(comp);
				temp = fc.getSelectedFile();
				textField.setText(""+temp);
			}
		});
		
		btnDestination.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fc = new JFileChooser();
				fc.setFileSelectionMode(1);
				Component comp = null;
				File f = new File("C:/");
				fc.setCurrentDirectory(f);
				fc.showOpenDialog(comp);
				temp = fc.getSelectedFile();
				textField_1.setText(""+temp);
			}
		});
		
		btnSaveTask.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(!textField.getText().toString().isEmpty() && !textField_1.getText().toString().isEmpty())
				{
					if(!txtVersions.getText().toString().isEmpty() && txtVersions.getText().toString().matches("[0-9]+") && txtVersions.getText().toString().length() < 3)
					{versions = Integer.parseInt(txtVersions.getText().toString());}
					else{versions = 0;}
					if(!checkBox_1.isSelected() && !txtFreq.getText().isEmpty()){freq = txtFreq.getText().toString();}
					else{freq = "N/A";}
					if(textField_2.getText().isEmpty()){JOptionPane.showMessageDialog(dialog, "Title Field Empty!", "ERROR", JOptionPane.ERROR_MESSAGE);}
					else
					{
						if(txtBackup.getText().isEmpty() || txtArchive.getText().isEmpty()){JOptionPane.showMessageDialog(dialog, "Archive or Backup Field(s) Empty!", "ERROR", JOptionPane.ERROR_MESSAGE);}
						else
						{
							if(checkBox_1.isSelected())automated = true;
							else automated = false;
							Backend backend = new Backend();
							backend.CreateTask(new String[] {g(textField_2), g(textField), g(textField_1), ""+versions, g(txtBackup), g(txtArchive), automated+"", g(txtFreq)});
						}
					}
					dispose();
				}
				else{JOptionPane.showMessageDialog(dialog, "Source or Destination Empty!", "ERROR", JOptionPane.ERROR_MESSAGE);}
			}
		});
	}
	
	private void MouseBuilder(JButton btn)
	{

		btn.setForeground(NeonGreen);
		btn.setFocusable(false);
		btn.setBorder(new LineBorder(NeonGreen));
		btn.setBackground(Color.black);
		btn.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseExited(MouseEvent arg0)
			{Hover(arg0, arg0.getComponent().getForeground(), arg0.getComponent().getBackground());}
			@Override
			public void mouseEntered(MouseEvent arg0)
			{Hover(arg0, arg0.getComponent().getForeground(), arg0.getComponent().getBackground());}
			@Override
			public void mouseClicked(MouseEvent arg0){}
			@Override
			public void mousePressed(MouseEvent arg0){}
			@Override
			public void mouseReleased(MouseEvent arg0){}
		});
	}
	
	private void Hover(MouseEvent arg, Color color1, Color color2)
	{
		arg.getComponent().setBackground(color1);
		arg.getComponent().setForeground(color2);
	}
	
	private void ToggleActive(boolean on, JTextField t)
	{
		if(on)
		{
			t.setEnabled(true);
			t.setBorder(new LineBorder(NeonGreen));
			t.setForeground(NeonGreen);
		}
		else
		{
			t.setEnabled(false);
			t.setBorder(new LineBorder(NeonRed));
			t.setForeground(NeonRed);
		}
	}
	
	private String g(JTextField j)
	{
		String s;
		s = j.getText().toString();
		return s;
	}
}
