package hBackup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class CurrentTasks
{
	//GUI
	private Timer timer;
	private JFrame frame;
	private JButton btnPause;
	private JButton btnDelete;
	private JButton btnRefresh;
	private JScrollPane scroll;
	private JTable table;
	//Colors
	private Color NeonGreen = new Color(0,255,128);
	private Color NeonBlue = new Color(0,200,255);
	private Color NeonRed = new Color(255,102,102);
	private Color Fade = new Color(45,45,45);
	private Holder held;
	//Variables
	private Object[] titles = {"ID","Title","Source","Destination","Versions","Backup Name","Archive Name","Automated","Frequency","i"};	
	private TableModel model;
	private ArrayList<Timer> timings = new ArrayList<Timer>();
	private ArrayList<int[]> tasks = new ArrayList<int[]>();
	private Backend backend;
	private List<String[]> Empty = new ArrayList<String[]>();

	public CurrentTasks(Holder held, Backend backend)
	{
		this.held = held;
		this.backend = backend;
		initialize();
	}

	private void initialize()
	{
		timings.add(null);
		timer = new Timer(2000, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(held.changed())
				{
					held.setChanged(false);
					model = new DefaultTableModel(held.getTable(), titles);
					table.setModel(model);
					Resize();
					for(Object[] o : held.getTable())
					{
						boolean New = true;
						for(int[] i : tasks)
						{
							if(Integer.parseInt((String) o[0]) == i[0])
							{
								New = false;
							}
						}
						if(New)
						{
							int[] i = new int[3];
							i[0] = Integer.parseInt((String) o[0]);
							tasks.add(i);
						}
					}
				}
			}
		});
		frame = new JFrame();
		frame.setTitle("hBackup - Tasks");
		frame.setBounds(400, 400, 450, 200);
		frame.setDefaultCloseOperation(frame.HIDE_ON_CLOSE);
		frame.getContentPane().setBackground(Color.black);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(448, 200));
		
		btnPause = new JButton("Toggle");
		btnPause.setBounds(10, frame.getHeight()-60, 100, 20);
		MouseBuilder(btnPause);
		
		btnRefresh = new JButton("Run Now");
		btnRefresh.setBounds(120, frame.getHeight()-60, 120, 20);
		MouseBuilder(btnRefresh);
		
		btnDelete = new JButton("");
		btnDelete.setBounds(-100,-100,20,20);
		MouseBuilder(btnDelete);
		
		table = new JTable(50, 10);
		table.setForeground(NeonGreen);
		table.setBackground(Fade);
		table.setBorder(new LineBorder(NeonGreen));
		frame.getContentPane().add(table);
		
		scroll = new JScrollPane(table);
		scroll.setBounds(10, 10, frame.getWidth()-28, frame.getHeight()-100);
		scroll.setBorder(new LineBorder(NeonGreen));
		scroll.setBackground(Fade);
		scroll.setForeground(NeonRed);
		frame.getContentPane().add(scroll);
		
		frame.addComponentListener(new ComponentListener()
		{
			@Override
			public void componentShown(ComponentEvent arg0){}
			@Override
			public void componentMoved(ComponentEvent arg0){}
			@Override
			public void componentHidden(ComponentEvent arg0){}
			@Override
			public void componentResized(ComponentEvent arg0)
			{Resize();}
		});
		
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent arg0)
			{
				frame.dispose();
			}
		});
		
		btnRefresh.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int[] temp = table.getSelectedRows();
				for(int i : temp)
				{
					for(int[] ii : tasks)
					{
						held.runIt(ii[0]);
						addVersion(ii[0]);
					}
				}
			}
		});
		
		btnPause.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int[] temp = table.getSelectedRows();
				for(int i : temp)
				{
					for(int[] ii : tasks)
					{
						if(Integer.parseInt((String) table.getModel().getValueAt(i, 0)) == ii[0])
						{
							if(ii[1] == 0)
							{
								if(ii[2] == 0)
								{
									ii[2] = timings.size();
									timings.add(new Timer(Integer.parseInt((String) table.getModel().getValueAt(i, 8))*60000,
									new ActionListener(){
										@Override
										public void actionPerformed(ActionEvent arg0)
										{
											held.runIt(ii[0]);
											addVersion(ii[0]);
										}
									}));
									timings.get(ii[2]).start();
									ii[1] = 1;
									JOptionPane.showMessageDialog(frame, "Started task: "+table.getModel().getValueAt(i, 1), "STARTING", JOptionPane.PLAIN_MESSAGE);
								}
								else
								{
									timings.get(ii[2]).start();
									ii[1] = 1;
									JOptionPane.showMessageDialog(frame, "Started task: "+table.getModel().getValueAt(i, 1), "STARTING", JOptionPane.PLAIN_MESSAGE);
								}
							}
							else
							{
								timings.get(ii[2]).stop();
								ii[1] = 0;
								JOptionPane.showMessageDialog(frame, "Stopped task: "+table.getModel().getValueAt(i, 1), "STOPPING", JOptionPane.PLAIN_MESSAGE);
							}
						}
					}
				}
			}
		});
		
		timer.start();
	}
	
	private void addVersion(int id)
	{
		for(int i = 0; i < table.getRowCount(); i++)
		{
			if(Integer.parseInt((String) table.getModel().getValueAt(i, 0)) == id)
			{
				backend.edit(id, concat(i));
			}
		}
	}
	
	private String concat(int kp)
	{
		String lp = "";
		lp += table.getModel().getValueAt(kp, 0);
		for(int i = 1; i < 9; i++)
		{
			lp += ","+table.getModel().getValueAt(kp, i);
		}
		lp += ","+(Integer.parseInt((String) table.getModel().getValueAt(kp, 9))+1);
		return lp;
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
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btn);
	}
	
	private void Hover(MouseEvent arg, Color color1, Color color2)
	{
		arg.getComponent().setBackground(color1);
		arg.getComponent().setForeground(color2);
	}
	
	private void Resize()
	{
		btnPause.setBounds(10, frame.getHeight()-60, 100, 20);
		btnRefresh.setBounds(120, frame.getHeight()-60, 120, 20);
		scroll.setBounds(10, 10, frame.getWidth()-28, frame.getHeight()-80);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(((scroll.getWidth()-300)/5)*2);
		table.getColumnModel().getColumn(2).setPreferredWidth((scroll.getWidth()-300)/5);
		table.getColumnModel().getColumn(3).setPreferredWidth((scroll.getWidth()-300)/5);
		table.getColumnModel().getColumn(4).setPreferredWidth(60);
		table.getColumnModel().getColumn(5).setPreferredWidth((scroll.getWidth()-230)/10);
		table.getColumnModel().getColumn(6).setPreferredWidth((scroll.getWidth()-230)/10);
		table.getColumnModel().getColumn(7).setPreferredWidth(70);
		table.getColumnModel().getColumn(8).setPreferredWidth(70);
		table.setFont(new Font("Verdana", Font.PLAIN, 14));
		table.setRowHeight(25);
		scroll.getViewport().setBackground(Fade);
		
		if(frame.getWidth() < 775)
		{
			table.getColumnModel().getColumn(0).setPreferredWidth(0);
			table.getColumnModel().getColumn(1).setPreferredWidth(table.getWidth());
			table.getColumnModel().getColumn(2).setPreferredWidth(0);
			table.getColumnModel().getColumn(3).setPreferredWidth(0);
			table.getColumnModel().getColumn(4).setPreferredWidth(0);
			table.getColumnModel().getColumn(5).setPreferredWidth(0);
			table.getColumnModel().getColumn(6).setPreferredWidth(0);
			table.getColumnModel().getColumn(7).setPreferredWidth(0);
			table.getColumnModel().getColumn(8).setPreferredWidth(0);
			table.getColumnModel().getColumn(9).setPreferredWidth(0);
			table.setFont(new Font("Verdana", Font.PLAIN, 14));
			table.setRowHeight(25);
		}
	}
	
	public void comeBack()
	{
		frame.show();
	}
}
