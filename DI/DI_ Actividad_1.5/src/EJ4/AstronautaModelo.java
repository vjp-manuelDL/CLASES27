package EJ4;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class AstronautaModelo extends AbstractTableModel {

    // 1. Lista interna para guardar los datos y array con las cabeceras
    private List<Astronauta> listaAstronautas = new ArrayList<>();
    private final String[] columnas = {"Nombre", "Misiones", "Categoría"};

    // 2. Método público para añadir el objeto y avisar a la tabla del cambio
    public void addAstronauta(Astronauta c) {
        listaAstronautas.add(c);
        // Notifica a la vista (JTable) que se ha insertado una fila nueva al final
        fireTableRowsInserted(listaAstronautas.size() - 1, listaAstronautas.size() - 1);
    }
    
    // 3. Métodos sobreescritos obligatorios del modelo
    @Override
    public int getRowCount() {
        return listaAstronautas.size(); // Número de filas
    }

    @Override
    public int getColumnCount() {
        return columnas.length; // Número de columnas (3)
    }

    // Sobreescribimos este método extra para que la tabla muestre los nombres de las cabeceras
    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Astronauta a = listaAstronautas.get(rowIndex); // Obtenemos el astronauta de esa fila
        
        switch (columnIndex) {
            case 0:
                return a.getNombre();
            case 1:
                return a.getMisiones();
            case 2:
                // La categoría se calcula al vuelo, cumpliendo el requisito: "Categoría no es un atributo de la clase Astronauta"
                if (a.getMisiones() >= 3) {
                    return "Veterano";
                } else {
                    return "Aprendiz";
                }
            default:
                return null;
        }
    }
}