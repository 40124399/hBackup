package hBackup;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainWindow {
    private JFrame frame;
    private JButton btnRefresh;
    private JButton btnNewTask;
    private JButton btnRunSelectedTasks;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnChange;
    private JTextField txtSRC;
    private TableModel model;
    private JFileChooser fc;
    private Timer timer;

    //Colors
    private Color NeonGreen = new Color(0, 255, 128);
    private Color NeonBlue = new Color(0, 200, 255);
    private Color NeonRed = new Color(255, 102, 102);
    private Color Fade = new Color(45, 45, 45);

    private Backend backend = new Backend();
    private List<String> line = new ArrayList<String>();
    private Object[][] rowData;
    private Object[] columnNames = {"ID", "Title", "Source", "Destination", "Versions", "Backup Name", "Archive Name", "Automated", "Frequency", "i"};
    private JTable table;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindow window = new MainWindow();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainWindow() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 1000, 300);
        frame.setMinimumSize(new Dimension(448, 200));
        frame.setTitle("hBackup");
        frame.getContentPane().setBackground(Color.black);
        frame.getContentPane().setLayout(null);
        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentShown(ComponentEvent arg0) {
            }

            @Override
            public void componentMoved(ComponentEvent arg0) {
            }

            @Override
            public void componentHidden(ComponentEvent arg0) {
            }

            @Override
            public void componentResized(ComponentEvent arg0) {
                Resize();
            }
        });

        btnNewTask = new JButton("New Task");
        btnNewTask.setFocusable(false);
        btnNewTask.setBorder(new LineBorder(NeonGreen));
        btnNewTask.setForeground(NeonGreen);
        btnNewTask.setBackground(Color.black);
        frame.getContentPane().add(btnNewTask);

        btnRunSelectedTasks = new JButton("Run Selected Tasks");
        btnRunSelectedTasks.setBorder(new LineBorder(NeonGreen));
        btnRunSelectedTasks.setFocusable(false);
        btnRunSelectedTasks.setForeground(NeonGreen);
        btnRunSelectedTasks.setBackground(Color.black);
        frame.getContentPane().add(btnRunSelectedTasks);

        Populate();

        model = new DefaultTableModel(rowData, columnNames);

        table = new JTable(model);
        table.setBounds(10, 11, 350, 215);
        table.setForeground(NeonGreen);
        table.setBackground(Fade);
        table.setBorder(new LineBorder(NeonGreen));
        frame.getContentPane().add(table);

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Populate();
                model = new DefaultTableModel(rowData, columnNames);
                table.setModel(model);
            }
        });


        btnRefresh = new JButton("Refresh");
        btnRefresh.setForeground(new Color(0, 255, 128));
        btnRefresh.setFocusable(false);
        btnRefresh.setBorder(new LineBorder(NeonGreen));
        btnRefresh.setBackground(Color.BLACK);
        frame.getContentPane().add(btnRefresh);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnDelete = new JButton("Delete");
        btnDelete.setForeground(new Color(0, 255, 128));
        btnDelete.setFocusable(false);
        btnDelete.setBorder(new LineBorder(NeonGreen));
        btnDelete.setBackground(Color.BLACK);
        frame.getContentPane().add(btnDelete);

        btnEdit = new JButton("Edit");
        btnEdit.setForeground(new Color(0, 255, 128));
        btnEdit.setFocusable(false);
        btnEdit.setBorder(new LineBorder(NeonGreen));
        btnEdit.setBackground(Color.BLACK);
        frame.getContentPane().add(btnEdit);

        btnChange = new JButton("Change");
        btnChange.setForeground(new Color(0, 255, 128));
        btnChange.setFocusable(false);
        btnChange.setBorder(new LineBorder(NeonGreen));
        btnChange.setBackground(Color.BLACK);
        frame.getContentPane().add(btnChange);

        txtSRC = new JTextField();
        txtSRC.setForeground(NeonBlue);
        txtSRC.setText(backend.getPath());
        txtSRC.setBackground(Fade);
        txtSRC.setBorder(new LineBorder(NeonBlue));
        frame.getContentPane().add(txtSRC);

        MouseBuilder(btnNewTask, 271, 100);
        MouseBuilder(btnRefresh, 145, 60);
        MouseBuilder(btnRunSelectedTasks, 10, 150);
        MouseBuilder(btnDelete, 380, 100);
        MouseBuilder(btnEdit, 0, 100);
        MouseBuilder(btnChange, 0, 100);

        btnDelete.setForeground(NeonRed);
        btnDelete.setBorder(new LineBorder(NeonRed));

        table.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_DELETE) {
                }
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int t : table.getSelectedRows()) {
                    backend.edit(Integer.parseInt(table.getModel().getValueAt(t, 0).toString()), concat(t));
                }
            }
        });

        btnNewTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                NewTask task = new NewTask();
                task.setVisible(true);
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        btnRunSelectedTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int t : table.getSelectedRows()) {
                    backend.PassTask(Integer.parseInt(table.getModel().getValueAt(t, 0).toString()));
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int t : table.getSelectedRows()) {
                    backend.delete(Integer.parseInt(table.getModel().getValueAt(t, 0).toString()));
                }
                refresh();
            }
        });

        btnChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fc = new JFileChooser();
                fc.setFileSelectionMode(1);
                Component comp = null;
                File f = new File(backend.getPath());
                fc.setCurrentDirectory(f);
                fc.showOpenDialog(comp);
                txtSRC.setText("" + fc.getSelectedFile());

                backend.setPath(txtSRC.getText().toString());
            }
        });
    }

        private void refresh() {
        Populate();
        model = new DefaultTableModel(rowData, columnNames);
        table.setModel(model);
        Resize();
    }

    private String concat(int kp) {
        String lp = "";
        lp += table.getModel().getValueAt(kp, 0);
        for (int i = 1; i < 10; i++) {
            lp += "," + table.getModel().getValueAt(kp, i);
        }
        return lp;
    }

    private void Populate() {
        backend.Load();
        this.line = backend.getList();
        rowData = new Object[line.size()][];
        int i = 0;
        for (String l : line) {
            rowData[i] = l.split("\\,");
            i++;
        }
    }

    private void Resize() {
        btnNewTask.setBounds(260, frame.getHeight() - 60, 100, 20);
        btnRefresh.setBounds(170, frame.getHeight() - 60, 80, 20);
        btnRunSelectedTasks.setBounds(10, frame.getHeight() - 60, 150, 20);
        btnDelete.setBounds(370, frame.getHeight() - 60, 60, 20);
        btnEdit.setBounds(440, frame.getHeight() - 60, 60, 20);
        btnChange.setBounds(frame.getWidth() - 268, frame.getHeight() - 60, 60, 20);
        txtSRC.setBounds(frame.getWidth() - 198, frame.getHeight() - 60, 180, 20);
        table.setBounds(10, 10, frame.getWidth() - 28, frame.getHeight() - 80);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth((table.getWidth() - 60) / 4);
        table.getColumnModel().getColumn(2).setPreferredWidth((table.getWidth() - 60) / 6);
        table.getColumnModel().getColumn(3).setPreferredWidth((table.getWidth() - 60) / 6);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);
        table.getColumnModel().getColumn(5).setPreferredWidth((table.getWidth() - 60) / 6);
        table.getColumnModel().getColumn(6).setPreferredWidth((table.getWidth() - 60) / 6);
        table.getColumnModel().getColumn(7).setPreferredWidth(50);
        table.getColumnModel().getColumn(8).setPreferredWidth(50);
        table.getColumnModel().getColumn(9).setPreferredWidth(30);
        table.setFont(new Font("Verdana", Font.PLAIN, 14));
        table.setRowHeight(25);
        if (frame.getWidth() < 775) {
            btnEdit.setBounds(-100, -100, 0, 0);
            btnChange.setBounds(-100, -100, 0, 0);
            txtSRC.setBounds(-100, -100, 0, 0);

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

    private void MouseBuilder(JButton btn, int x, int width) {
        btn.setForeground(NeonGreen);
        btn.setFocusable(false);
        btn.setBorder(new LineBorder(NeonGreen));
        btn.setBackground(Color.black);
        btn.addMouseListener(new MouseListener() {
            @Override
            public void mouseExited(MouseEvent arg0) {
                Hover(arg0, arg0.getComponent().getForeground(), arg0.getComponent().getBackground());
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                Hover(arg0, arg0.getComponent().getForeground(), arg0.getComponent().getBackground());
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }
        });
    }

    private void Hover(MouseEvent arg, Color color1, Color color2) {
        arg.getComponent().setBackground(color1);
        arg.getComponent().setForeground(color2);
    }
}
