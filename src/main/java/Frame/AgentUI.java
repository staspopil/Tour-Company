package Frame;

import DB.AgentMySqlConnection;
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

public class AgentUI extends JFrame implements ActionListener {
    ILogger logger;
        JTable table;
        Map<Integer, ArrayList<String>> result = new HashMap<Integer,ArrayList<String>>();
        boolean isHot = false;
     public AgentMySqlConnection database = AgentMySqlConnection.getInstance();

    private int WIDTH = 800;
    private int HEIGHT = 400;

    //labels LeftPart of Select Panel
    Object columns[] = new String [] { "ID", "Country", "City","Hotel","Start Date","END DATE", "PRICE","IS HOT","CLIENT"};
    Object rows[][] = new String[100][100];

    JLabel lcountry = new JLabel("   Country:");
    JLabel lcity = new JLabel("   City:");
    JLabel lstartdate = new JLabel("   Start Date:");
    JLabel lenddate = new JLabel("   End Date:");
    JLabel lhotel = new JLabel("   HotelName:");
    JLabel lprice = new JLabel("   Price:");
    JCheckBox checkBox = new JCheckBox("isHot");
    JButton selectbutton = new JButton("SELECT");
    JButton reserveButton = new JButton("RESERVE");

    //Right Part of SelectPanels

    JTextArea country = new JTextArea(2,2);
    JTextArea city = new JTextArea();
    JTextArea startdate = new JTextArea();
    JTextArea enddate = new JTextArea();
    JTextArea hotel = new JTextArea();
    JTextArea price = new JTextArea();
    JPanel SelectPanel = new JPanel();
    JPanel ListPanel = new JPanel();
    JPanel ClientPanel = new JPanel();
    JButton clearbutton = new JButton("CLEAR");
    JButton descrButton = new JButton("DESCRIPTION");
    //List

//    JTextArea tourlist = new JTextArea("List of Tours:\n",20,20);
//
//
//    JScrollPane scrollpane = new JScrollPane(tourlist,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


    //Client Panel
    JLabel lClientFName= new JLabel("   First Name:");
    JLabel lClientSName = new JLabel("   Second Name:");
    JLabel lClientPhone = new JLabel("   Phone Number:");
    JTextArea ClientFName = new JTextArea();
    JTextArea ClientSName = new JTextArea();
    JTextArea ClientPhone = new JTextArea();

    public AgentUI(ILogger logger) throws IOException {
        super("Tour Agent v2.0");
        this.logger = logger;
        database.makeJDBCConnection(new ConsoleLogger());
        this.setLayout(new GridLayout(1,3,10,10));
        SelectPanel.setLayout(new GridLayout(9,2,8,8));
        ListPanel.setLayout(new BorderLayout(8,8));
        ClientPanel.setLayout(new GridLayout(9,2,8,8));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        selectbutton.addActionListener(this);
        clearbutton.addActionListener(this);
        reserveButton.addActionListener(this);
        descrButton.addActionListener(this);

        //SelectPanel
        add(SelectPanel);
        SelectPanel.add(lcountry);
        SelectPanel.add(country);
        SelectPanel.add(lcity);
        SelectPanel.add(city);
        SelectPanel.add(lhotel);
        SelectPanel.add(hotel);
        SelectPanel.add(lstartdate);
        SelectPanel.add(startdate);
        SelectPanel.add(lenddate);
        SelectPanel.add(enddate);
        SelectPanel.add(lprice);
        SelectPanel.add(price);
        SelectPanel.add(checkBox);
        SelectPanel.add(selectbutton);
        SelectPanel.add(clearbutton);
        SelectPanel.add(descrButton);

        //ListPanel

        add(ListPanel);
        ListPanel.add(reserveButton,BorderLayout.SOUTH);
        add(ClientPanel);
        ClientPanel.add(lClientFName);
        ClientPanel.add(ClientFName);
        ClientPanel.add(lClientSName);
        ClientPanel.add(ClientSName);
        ClientPanel.add(lClientPhone);
        ClientPanel.add(ClientPhone);

        //Client Panel


        setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {

         if (e.getSource()==selectbutton) {
                    if (country.getText().length()==0|| country.getText().charAt(0)=='%'){
                        country.setText("%");
                    }
                    if (city.getText().length()==0|| city.getText().charAt(0)=='%'){
                        city.setText("%");
                    }
                    if (hotel.getText().length()==0 || hotel.getText().charAt(0)=='%'){
                        hotel.setText("%");
                    }
                    if(startdate.getText().length()==0){
                        startdate.setText("00000000");
                    }
                    if(enddate.getText().length()==0){
                        enddate.setText("99990000");
                    }
                    if(price.getText().length()==0){
                        price.setText("9999999");
                    }
                    if (checkBox.isSelected()){
                        isHot = true;
                    }
                    if (!checkBox.isSelected()){
                        isHot = false;
                    }
                    rows = new Object[100][100];

                    //Перебор результата запроса
                    result = database.getDataFromDB(country.getText(),city.getText(),hotel.getText(),startdate.getText(),enddate.getText(),Integer.valueOf(price.getText()),isHot);
                    //result = database.getDataFromDB();
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

                    //Создание результирующей таблицы

                    setDefaultCloseOperation(EXIT_ON_CLOSE);
                    // Простая таблица

                     table = new JTable(rows, columns);
                    ListPanel.add(new JScrollPane(table));

             try {
                 DecoratorLogger logger = new DBLogger(new LocalLogger(new ConsoleLogger()));
                 logger.log("AGENT GET DATA"+database);
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
                logger.log("AGENET RESERVED TOUR");
                database.addClient(ClientFName.getText(),ClientSName.getText(),ClientPhone.getText());
                database.reserve(ClientSName.getText(),ClientPhone.getText(),idk);
               // ListPanel.remove(1);
               // ListPanel.updateUI();
            } catch (IOException | SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (e.getSource() == descrButton){
            int idk = (Integer)table.getValueAt(table.getSelectedRow(),0);
            JOptionPane.showMessageDialog(this, result.get(idk).get(7),"Description",JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

