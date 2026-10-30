package Ej3;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 *
 * @author alumno
 */
public class ContactoTableModel extends AbstractTableModel {

    private List<Contacto> listacontactos;

    public ContactoTableModel() {
        listacontactos = new ArrayList<>();

    }

    @Override
    public int getRowCount() {
        return listacontactos.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contacto contacos = listacontactos.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return contacos.getNombre();
            case 1:
               return contacos.getTlf();
            default:
                return null;
        }
    }

    void addContacto(Contacto c) {
        listacontactos.add(c);
        this.fireTableDataChanged();
    }

}
