/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ej2;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author alumno
 */
public class CocheTableModel extends AbstractTableModel {

    private List<Coche> listaCoches;

    public CocheTableModel() {
        listaCoches = new ArrayList<>();

        listaCoches.add(new Coche("Toyota", "Corolla", 110));
        listaCoches.add(new Coche("Ford", "Mustang", 450));
        listaCoches.add(new Coche("Seat", "Ibiza", 95));

    }

    @Override
    public int getRowCount() {
        return listaCoches.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Coche coche = listaCoches.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return coche.getMarca();
            case 1:
                return coche.getModelo();
            case 2:
                return coche.getCaballos();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 ->
                "Marca";
            case 1 ->
                "Modelo";
            case 2 ->
                "Caballos";
            default ->
                null;
        };

    }
}
