package com.calvin.tms.service;

import com.calvin.tms.model.Cell;
import com.calvin.tms.model.Raod;
import com.calvin.tms.model.Vehicle;
import com.calvin.tms.model.enums.Direction;
import com.calvin.tms.model.enums.Operation;

import java.awt.*;

public class RoadService {


    private static RoadService ourInstance = new RoadService();

    Raod raod = new Raod();

    public static RoadService getInstance() {
        return ourInstance;
    }

    private RoadService() {
    }

    public void generateRoad(int maxX, int maxy) {

        Cell[][] cells = new Cell[maxX][maxy];
        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxy; j++) {
                cells[i][j] = new Cell(i, j, false);
            }
        }
        raod.setCells(cells);

        enableRaod(cells, Operation.X_PLUS, 0, 10, 5, 6);
        enableRaod(cells, Operation.X_MINUS, 0, 10, 6, 7);
        enableRaod(cells, Operation.Y_PLUS, 5, 6, 0, 10);
        enableRaod(cells, Operation.Y_MINUS, 6, 7, 0, 10);

        printRoad(maxX, maxy);

    }

    public void printRoad(int maxX, int maxy) {
        System.out.println("");
        Cell[][] cells = raod.getCells();
        for (int j = 0; j < maxX; j++) {
            for (int i = 0; i < maxy; i++) {
                Cell cell = cells[i][j];
                if (cell.isEnable()) {

                    if (cell.isOccupied()) {
                        System.out.print("\033[31;1mCR\033[0m");
                    } else {
                        printOp(cell);

                    }
                } else {
                    printOp(cell);
                }
                System.out.print(" ");

            }
            System.out.println("");
        }
    }

    private void printOp(Cell cell) {

        if (cell.getOperation().size() == 0) {
            System.out.print("  ");
        }
         if (cell.getOperation().size() == 1) {
            System.out.print("\033[0;32m" + cell.getOperation().get(0).getOperator() + "\033[0;32m");
            System.out.print("\033[0;32m" + cell.getOperation().get(0).getOperator() + "\033[0;32m");
        } else if (cell.getOperation().size() == 2) {
            System.out.print("\033[0;32m" + cell.getOperation().get(0).getOperator() + "\033[0;32m");
            System.out.print("\033[0;32m" + cell.getOperation().get(1).getOperator() + "\033[0;32m");
        }
    }

    private void enableRaod(Cell[][] cells, Operation operation, int sx, int ex, int sy, int ey) {
        for (int i = sx; i < ex; i++) {
            for (int j = sy; j < ey; j++) {
                Cell cell = cells[i][j];
                cell.setEnable(true);
                cell.getOperation().add(operation);
                if (cell.getOperation().size() > 1) {
                    cell.setDecision(true);
                }
            }
        }
    }

    public Vehicle move(Vehicle vehicle, Operation operation) {
        Operation movement = getMovement(vehicle.getCx(), vehicle.getCy(), operation);
        if (movement != null) {
            Cell cell = getCell(vehicle.getCx(), vehicle.getCy(), operation);
            vehicle.setPx(vehicle.getCx());
            vehicle.setPy(vehicle.getCy());
            vehicle.setCx(cell.getX());
            vehicle.setCy(cell.getY());
            trackMovement(vehicle);
        }
        return vehicle;
    }

    public Operation getMovement(int x, int y, Operation operation) {

        Cell cell = getCell(x, y, operation);
        if (!cell.isEnable()) {
            Cell cell1 = raod.getCells()[x][y];
            return cell1.getOperation().get(0);
        } else if (cell.isOccupied()) {
            return null;
        }
        if (cell.getOperation().contains(operation)) {
            return operation;
        } else {
            return null;
        }

    }

    private Cell getCell(int x, int y, Operation operation) {
        Cell cell = null;
        Cell[][] cells = raod.getCells();
        switch (operation) {
            case Y_MINUS:
                int i = y - 1;
                cell = cells[x][i];
                break;
            case X_MINUS:
                int i1 = x - 1;
                cell = cells[i1][y];
                break;
            case Y_PLUS:
                int i2 = y + 1;
                cell = cells[x][i2];
                break;
            case X_PLUS:
                int i3 = x + 1;
                cell = cells[i3][y];
                break;
            default:
        }
        return cell;
    }

    public void trackMovement(Vehicle vehicle) {
        Cell[][] cells = raod.getCells();
        cells[vehicle.getPx()][vehicle.getPy()].setOccupied(false);
        cells[vehicle.getCx()][vehicle.getCy()].setOccupied(true);
    }


}
