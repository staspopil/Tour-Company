package Frame;

import DB.OperatorMySqlConnection;
import Loggers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OperatorUI extends JFrame implements ActionListener{
    ILogger logger;
    JTable table;
    Map<Integer, ArrayList<String>> result = new HashMap<Integer,ArrayList<String>>();
    boolean isHot = false;
    public OperatorMySqlConnection database = OperatorMySqlConnection.getInstance();

    private int WIDTH = 800;
    private int HEIGHT = 400;

    //labels LeftPart
    Object columns[] = new String [] { "ID", "Country", "City","Hotel","Start Date","END DATE", "PRICE","IS HOT","CLIENT"};
    Object rows[][] = new String[100][100];

    JLabel lcountry = new JLabel("   Country:");
    JLabel lcity = new JLabel("   City:");
    JLabel lstartdate = new JLabel("   Start Date:");
    JLabel lenddate = new JLabel("   End Date:");
    JLabel lhotel = new JLabel("   HotelName:");
    JLabel lprice = new JLabel("   Price:");
    JLabel ldescription = new JLabel("   Desctiption");
    JCheckBox checkBox = new JCheckBox("isHot");
    JButton insertbutton = new JButton("INSERT");
    JButton reserveButton = new JButton("DELETE");
    JButton selectButton = new JButton("SELECT");
    //Right Part

    JTextArea country = new JTextArea(2,2);
    JTextArea city = new JTextArea();
    JTextArea startdate = new JTextArea();
    JTextArea enddate = new JTextArea();
    JTextArea hotel = new JTextArea();
    JTextArea price = new JTextArea();
    JTextArea description = new JTextArea();


    JPanel InsertPanel = new JPanel();
    JPanel ListPanel = new JPanel();
    JButton clearbutton = new JButton("CLEAR");
    //List

    JTextArea tourlist = new JTextArea("List of Tours:\n",20,20);


    JScrollPane scrollpane = new JScrollPane(tourlist,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


    public OperatorUI(ILogger logger) throws IOException {
        super("Tour Operator v2.0");
        this.logger = logger;
        database.makeJDBCConnection(new ConsoleLogger());
        this.setLayout(new GridLayout(1,2,10,10));
        InsertPanel.setLayout(new GridLayout(9,2,8,8));
        ListPanel.setLayout(new BorderLayout(8,8));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        insertbutton.addActionListener(this);
        clearbutton.addActionListener(this);
        reserveButton.addActionListener(this);
        selectButton.addActionListener(this);

        //InsertPanel
        add(InsertPanel);
        InsertPanel.add(lcountry);
        InsertPanel.add(country);
        InsertPanel.add(lcity);
        InsertPanel.add(city);
        InsertPanel.add(lhotel);
        InsertPanel.add(hotel);
        InsertPanel.add(lstartdate);
        InsertPanel.add(startdate);
        InsertPanel.add(lenddate);
        InsertPanel.add(enddate);
        InsertPanel.add(lprice);
        InsertPanel.add(price);
        InsertPanel.add(ldescription);
        InsertPanel.add(description);
        InsertPanel.add(checkBox);
        InsertPanel.add(insertbutton);
        InsertPanel.add(clearbutton);
        InsertPanel.add(selectButton);
        //ListPanel

        add(ListPanel);
        ListPanel.add(reserveButton,BorderLayout.SOUTH);
        // ListPanel.add(scrollpane,BorderLayout.CENTER);

        //TextPanel.add(scrollpane,BorderLayout.CENTER);
        //TextPanel.add(NameSign,BorderLayout.PAGE_START);
        // TextPanel.add(label);

        setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==insertbutton) {

            if (checkBox.isSelected()){
                isHot = true;
            }
            if (!checkBox.isSelected()){
                isHot = false;
            }
            rows = new Object[9][100];
            tourlist.setText("List of Tours:\n");

            //Добавление тура
            database.addDataToDB(0,country.getText(),city.getText(),hotel.getText(),
                    startdate.getText(),enddate.getText(),Integer.valueOf(price.getText()),
                    description.getText(),isHot);
            try {
                logger.log("OPERATOR ADD NEW TOUR");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

        if (e.getSource()==clearbutton){
            //   System.out.println(table.getValueAt(table.getSelectedRow(),0));
            ListPanel.remove(1);
            ListPanel.updateUI();
        }

        if (e.getSource()==reserveButton){
            int idk = (Integer)table.getValueAt(table.getSelectedRow(),0);
            System.out.println(idk);
            try {
                database.deleteData(idk);
                ListPanel.remove(1);
                ListPanel.updateUI();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (e.getSource()==selectButton){

                rows = new Object[100][100];

                //Перебор результата запроса
                result = database.getDataFromDB();
                int k = 0;
                for (Map.Entry<Integer, ArrayList<String>> entry : result.entrySet()) {
                    rows[k][0] = entry.getKey();
                    rows[k][1] = entry.getValue().get(0);
                    rows[k][2] = entry.getValue().get(1);
                    rows[k][3] = entry.getValue().get(2);
                    rows[k][4] = entry.getValue().get(4);
                    rows[k][5] = entry.getValue().get(5);
                    rows[k][6] = entry.getValue().get(3);
                    rows[k][7] = entry.getValue().get(6);
                    rows[k][8] = entry.getValue().get(8);
                    k++;
                }

                setDefaultCloseOperation(EXIT_ON_CLOSE);
                // Простая таблица

                table = new JTable(rows, columns);
                ListPanel.add(new JScrollPane(table));
                 ListPanel.updateUI();
                //System.out.println(result.get(6).get(0));
                try {
                    logger.log("OPERATOR GET DATA");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
        }
    }
}
