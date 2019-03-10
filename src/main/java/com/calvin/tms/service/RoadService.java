package com.calvin.tms.service;

import com.calvin.tms.Database;
import com.calvin.tms.controller.WebSocket;
import com.calvin.tms.model.*;
import com.calvin.tms.model.enums.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RoadService {

    @Autowired
    private WebSocket webSocket;
    @Autowired
    private Database database;

    private RestTemplate restTemplate = new RestTemplate();

    public Raod getRoad() {
        return database.getRaod();
    }


    public Raod generateRoad(LatLongRequest latLongRequest) {
        DataResponse dataResponse = restTemplate.postForObject("http://localhost:8087/view/generate",
            latLongRequest, DataResponse.class);

        int maxX = 0;
        maxX = getMaxX(dataResponse.getHorizontalCells(), maxX);
        maxX = getMaxX(dataResponse.getVerticalCells(), maxX);
        database.setMax(maxX);
        generateRoad(maxX, maxX, dataResponse);


        return database.getRaod();
    }

    private int getMaxX(VerticalCells[][] horizontalCells, int maxX) {
        for (int i = 0; i < horizontalCells.length; i++) {
            int length = horizontalCells[i].length;
            if (length > maxX) {
                maxX = length;
            }
        }
        return maxX;
    }

    public void generateRoad(int maxX, int maxy, DataResponse data) {

        Cell[][] cells = new Cell[maxX][maxy];
        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxy; j++) {
                cells[i][j] = new Cell(i, j, false);
            }
        }
        database.getRaod().setCells(cells);

        VerticalCells[] postiveX = data.getHorizontalCells()[0];
        VerticalCells[] negativeX = data.getHorizontalCells()[1];

        VerticalCells[] postiveY = data.getVerticalCells()[0];
        VerticalCells[] negativeY = data.getVerticalCells()[1];


        enableRaod(cells, Operation.X_PLUS, 0, postiveX.length, data.getHorizontalY(), data.getHorizontalY() + 1, postiveX);
        enableRaod(cells, Operation.X_MINUS, 0, negativeX.length, data.getHorizontalY() + 2, data.getHorizontalY() + 3, negativeX);
        enableRaod(cells, Operation.Y_PLUS, data.getVerticalX(), data.getVerticalX() + 1, 0, postiveY.length, postiveY);
        enableRaod(cells, Operation.Y_MINUS, data.getVerticalX() + 2, data.getVerticalX() + 3, 0, negativeY.length, negativeY);
        printRoad();

    }

    private void enableRaod(Cell[][] cells, Operation operation, int sx, int ex, int sy, int ey, VerticalCells[] postiveX) {
        for (int i = sx; i < ex; i++) {
            for (int j = sy; j < ey; j++) {
                Cell cell = cells[i][j];
                VerticalCells postiveX1 = postiveX[i];
                cell.setEnable(true);
                cell.setLat(postiveX1.getLat());
                cell.setLon(postiveX1.getLon());
                cell.getOperation().add(operation);
                cell.setExternalDecision(postiveX1.isIntersection());
                if (i == ex - 1) {
                    cell.setLast(true);
                }
                if (!cell.isExternalDecision()) {
                    database.getActiveCells().add(cell);
                }
            }
        }
    }

    public void printRoad() {
        int maxX = database.getMax();
        int maxy = database.getMax();
        System.out.println("");
        Cell[][] cells = getRoad().getCells();
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
            System.out.print("     ");
        }
        String s = cell.getX() + "_" + cell.getY();
        if (cell.getOperation().size() == 1) {
            System.out.print("\033[0;32m" + cell.getOperation().get(0).getOperator() + "\033[0;32m"+s);
            System.out.print("\033[0;32m" + cell.getOperation().get(0).getOperator() + "\033[0;32m"+s);
        } else if (cell.getOperation().size() == 2) {
            System.out.print("\033[0;32m" + cell.getOperation().get(0).getOperator() + "\033[0;32m"+s);
            System.out.print("\033[0;32m" + cell.getOperation().get(1).getOperator() + "\033[0;32m"+s);
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
            Cell cell1 = getRoad().getCells()[x][y];
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
        Cell[][] cells = getRoad().getCells();
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
        Cell[][] cells = getRoad().getCells();
        cells[vehicle.getPx()][vehicle.getPy()].setOccupied(false);
        cells[vehicle.getCx()][vehicle.getCy()].setOccupied(true);
        Message message = new Message();
        message.setContent(vehicle);
        try {
//            webSocket.send(vehicle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deAllocate(Vehicle vehicle) {
        Cell[][] cells = getRoad().getCells();
        cells[vehicle.getCx()][vehicle.getCy()].setOccupied(false);
    }


}
