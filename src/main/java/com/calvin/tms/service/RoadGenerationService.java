package com.calvin.tms.service;

import com.calvin.tms.model.Cell;
import com.calvin.tms.model.Raod;
import com.calvin.tms.model.enums.Operation;

public class RoadGenerationService {


    public void generateRoad() {
        int minx = 0, miny = 0;
        int maxX = 10;
        int maxy = 10;

        Cell[][] cells = new Cell[1000][1000];
        for (int i = 0; i < maxX; i++) {

            for (int j = 0; j < maxy; j++) {
                cells[i][j] = new Cell(i, j, false);
            }
        }

        /*enableRaod(cells, Operation.X_PLUS, 0, 500, 98, 100);
        enableRaod(cells, Operation.X_MINUS, 0, 500, 96, 98);
        enableRaod(cells, Operation.Y_PLUS, 250, 252, 500, 0);
        enableRaod(cells, Operation.Y_MINUS, 253, 255, 0, 500);
*/
        enableRaod(cells, Operation.X_PLUS, 0, 10, 5, 6);
        enableRaod(cells, Operation.X_MINUS, 0, 10, 6, 7);
        enableRaod(cells, Operation.Y_PLUS, 5, 6, 0, 10);
        enableRaod(cells, Operation.Y_MINUS, 6, 7, 0, 10);

        for (int i = 0; i < maxX; i++) {

            for (int j = 0; j < maxy; j++) {
                Cell cell = cells[i][j];
                if (cell.isEnable()) {
                    System.out.print("[");
                    printOp(cell);
                    System.out.print("]");
                } else {
                    System.out.print("[");
                    printOp(cell);
                    System.out.print("]");
                }
                System.out.print(" ");

            }
            System.out.println("");
        }

    }

    private void printOp(Cell cell) {
        for (int k = 0; k < 4; k++) {
            if (cell.getOperation().size() > k) {
                System.out.print(cell.getOperation().get(k).getOperator());
            } else {
                System.out.print("X");
            }

        }
    }

    public static void main(String[] args) {


        RoadGenerationService
            roadGenerationService = new RoadGenerationService();
        roadGenerationService.generateRoad();
    }

    private void enableRaod(Cell[][] cells, Operation operation, int sx, int ex, int sy, int ey) {
        for (int i = sy; i < ey; i++) {
            for (int j = sx; j < ex; j++) {
                Cell cell = cells[i][j];
                cell.setEnable(true);
                cell.getOperation().add(operation);
            }
        }
    }
}
