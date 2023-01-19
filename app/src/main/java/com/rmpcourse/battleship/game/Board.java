package com.rmpcourse.battleship.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Board {
    // Матрица, в каждой ячейке которой находится текущий статус этой ячейки
    private BoardStatus[][] statuses;
    // Список кораблей
    private List<Ship> ships;
    // генератор случайных чисел
    private Random random = new Random();

    // Флаг для отслеживания все ли корабли расставлены
    private boolean shipsPlaced;


    // Класс доски для отслеживания статуса ячеек и отрисовки на основе этой ниформации
    // Кноструктор класса
    public Board() {
        this.statuses = new BoardStatus[BoardSize.COLUMNS][BoardSize.ROWS];

        // Заполняем все строки матрицы статусом "Спряран_Пусто"
        for (BoardStatus[] row : statuses) {
            Arrays.fill(row, BoardStatus.HIDDEN_EMPTY);
        }

        // Создание списка кораблей
        ships = new ArrayList<>();

        ships.add(new Ship(new Coordinate(0, 0), ShipDirection.VERTICAL, ShipType.CARRIER));
        ships.add(new Ship(new Coordinate(2, 0), ShipDirection.VERTICAL, ShipType.BATTLESHIP));
        ships.add(new Ship(new Coordinate(4, 0), ShipDirection.VERTICAL, ShipType.CRUISER));
        ships.add(new Ship(new Coordinate(6, 0), ShipDirection.VERTICAL, ShipType.SUBMARINE));
        ships.add(new Ship(new Coordinate(8, 0), ShipDirection.VERTICAL, ShipType.DESTROYER));

        shipsPlaced = false;
    }

    // Получить статус ячейки
    public BoardStatus getStatus(int x, int y) {
        return statuses[x][y];
    }

    // Установить статус ячейки
    public void setStatus(int x, int y, BoardStatus status) {
        statuses[x][y] = status;
    }

    // Получить список кораблей
    public List<Ship> getShips() {
        return ships;
    }

    // Получить флаг расстановки кораблей
    public boolean areShipsPlaced() {
        return shipsPlaced;
    }

    // Установить, что все корабли расставлены
    public void setShipsPlaced(boolean shipsPlaced) {
        this.shipsPlaced = shipsPlaced;
    }

    // Растановка кораблей случайным образом
    public void placeShipsRandom() {
        for (Ship ship : ships) {
            boolean valid = false;

            while (!valid) {
                int direction = random.nextInt(2);

                if (direction == 0) {
                    int x = random.nextInt(BoardSize.COLUMNS - ship.getType().getLength());
                    int y = random.nextInt(BoardSize.ROWS);
                    ship.setDirection(ShipDirection.HORIZONTAL);
                    ship.setCoordinate(new Coordinate(x, y));
                } else if (direction == 1) {
                    int x = random.nextInt(BoardSize.COLUMNS);
                    int y = random.nextInt(BoardSize.ROWS - ship.getType().getLength());
                    ship.setDirection(ShipDirection.VERTICAL);
                    ship.setCoordinate(new Coordinate(x, y));
                }

                if (!isCollidingWithAny(ship)) {
                    valid = true;
                }
            }
        }
    }

    // Проверка на пересечение двух кораблей
    private boolean isColliding(Ship ship1, Ship ship2) {
        if (ship1 != ship2) {
            for (Coordinate coordinate : ship1.getListCoordinates()) {
                for (Coordinate coordinate2 : ship2.getListCoordinates()) {
                    if (coordinate.equals(coordinate2)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Проверка на пересечение корабля с чем-нибудь
    public boolean isCollidingWithAny(Ship ship) {
        for (Ship ship2 : ships) {
            if (isColliding(ship, ship2)) {
                return true;
            }
        }

        return false;
    }

    // Проверка доски на валидность, нет ли каких-либо пересечений
    public boolean isValidBoard() {
        for (Ship ship : ships) {
            if (isCollidingWithAny(ship)) {
                return false;
            }
        }

        return true;
    }

    // Подвердить расстановку кораблей
    public void confirmShipLocations() {
        if (isValidBoard()) {
            for (Ship ship : ships) {
                for (Coordinate coordinate : ship.getListCoordinates()) {
                    int x = coordinate.getX();
                    int y = coordinate.getY();

                    setStatus(x, y, BoardStatus.HIDDEN_SHIP);
                }
            }
        }
    }

    // Поиск корабля, который нужно затопить по результатам попаданий
    public Ship shipToSink() {
        for (Ship ship : ships) {
            boolean alive = false;

            for (Coordinate coordinate : ship.getListCoordinates()) {
                int x = coordinate.getX();
                int y = coordinate.getY();

                if (statuses[x][y] != BoardStatus.HIT) {
                    alive = true;
                    break;
                }
            }

            if (!alive) {
                return ship;
            }
        }

        return null;
    }

    // Утопить все корабли
    public void sinkShips() {
        if (shipToSink() != null) {
            Ship ship = shipToSink();

            for (Coordinate coordinate : ship.getListCoordinates()) {
                int x = coordinate.getX();
                int y = coordinate.getY();

                statuses[x][y] = BoardStatus.SUNK;
                ship.setAlive(false);
            }
        }
    }

    // Проверить все ли корабли затонули
    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (ship.isAlive()) {
                return false;
            }
        }

        return true;
    }
}

// Положение корабля в пространстве
enum ShipDirection {
    HORIZONTAL, VERTICAL
}


