package Frame;

// Пример работы с простыми таблицами JTable

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.util.*;

public class SimpleTableTest extends JFrame
{
    private Object[][] array = new String[][] {{ "Сахар" , "кг", "1.5" },
            { "Мука"  , "кг", "4.0" },
            { "Молоко", "л" , "2.2" }};
    // Заголовки столбцов
    private Object[] columnsHeader = new String[] {"Наименование", "Ед.измерения", "Количество"};
    // private static final long serialVersionUID = 1L;
    // Данные для таблиц

    public SimpleTableTest(Object[][] raws,Object[] cols ) {
        super("Простой пример с JTable");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Простая таблица
        JTable table1 = new JTable(raws, cols);
        // Таблица с настройками

        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(table1));

        // Вывод окна на экран
        setContentPane(contents);
        setSize(500, 400);
        setVisible(true);
    }


    public static void main(String[] args) {
     //   new SimpleTableTest(array,columnsHeader);
    }
}
