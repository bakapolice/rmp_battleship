package com.rmpcourse.battleship.ui.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.databinding.FragmentInGameBinding;
import com.rmpcourse.battleship.game.Board;
import com.rmpcourse.battleship.game.BoardSize;
import com.rmpcourse.battleship.game.BoardStatus;
import com.rmpcourse.battleship.game.Coordinate;
import com.rmpcourse.battleship.game.DrawableBoard;
import com.rmpcourse.battleship.game.DrawableBoardPlacing;
import com.rmpcourse.battleship.game.DrawableSquare;
import com.rmpcourse.battleship.game.MyDragShadowBuilder;
import com.rmpcourse.battleship.game.Ship;
import com.rmpcourse.battleship.ui.viewmodel.PlayerViewModel;

import java.util.Date;

public class InGameFragment extends Fragment {

    // инструмент, который позволяет проще писать код для взаимодейтсвия с view
    // используя декларативный формат,
    private FragmentInGameBinding binding;
    private PlayerViewModel mPlayerViewModel;
    private long playerId = -1, targetPlayerId = -1;
    private int timeRemaining = -1;
    private final int waiting = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Переопределяем поведение кнопки "Назад"
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(new OnBackPressedCallback(false) {
                    @Override
                    public void handleOnBackPressed() {
                        confirmQuit();
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Создаем объект представления
        binding = FragmentInGameBinding.inflate(inflater, container, false);
        // Получаем экземпляры моделей данных
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        // Получаем аргументы
        playerId = InGameFragmentArgs.fromBundle(getArguments()).getPlayerId();
        targetPlayerId = InGameFragmentArgs.fromBundle(getArguments()).getTargetPlayerId();

        // Находим нужные данные в БД с помощью модели данных
        mPlayerViewModel.findPlayerById(playerId);
        mPlayerViewModel.findTargetPlayerById(targetPlayerId);

        // Кнопка "Подтвердить расположение кораблей"
        binding.buttonConfirmShips.setOnClickListener(view -> {
            if (myTurn) buttonConfirmShips(myBoard);
            else buttonConfirmShips(targetBoard);
        });

        // Кнопка "Расставить корбали случайно"
        binding.buttonPlaceShipsRandomly.setOnClickListener(view -> {
            if (myTurn) buttonPlaceShipsRandomly(myBoard);
            else buttonPlaceShipsRandomly(targetBoard);
        });

        // Кнопка "Выйти из игры"
        binding.buttonQuitGame.setOnClickListener(view -> confirmQuit());

        // Кнопка "Продолжить"
        binding.buttonResume.setOnClickListener(view -> hidePopups());

        // Кнопка поворота кораблей
        binding.buttonRotateShip.setOnClickListener(view -> {
            if (drawableBoardPlacing.getActiveShip() != null) {
                drawableBoardPlacing.getActiveShip().rotate();
                drawableBoardPlacing.colorShips();
            }
        });

        // Обрабатывает нажатие вне попапа
        binding.popupQuitGame.setOnClickListener(view -> hidePopups());

        // Кнопка "Выйти"
        binding.buttonQuit.setOnClickListener(view -> {
            NavDirections action = InGameFragmentDirections
                    .actionInGameFragmentToStartFragment(playerId);
            Navigation.findNavController(view).navigate(action);
        });

        // Предотвращает закрытие попапа при нажатии на него
        binding.popupQuitGameInner.setOnClickListener(view -> {
            //
        });

        // Начало игры
        startGameSinglePlayer();
        // Устанавливаем представление для фрагмента
        return binding.getRoot();
    }


    private static final String TAG = "Battleship";

    /**
     * User Interface
     */

    // Список экранов на макете
    private static final int[] SCREENS = {
            R.id.screen_please_wait,
            R.id.screen_place_ships,
            R.id.screen_target_board,
            R.id.screen_my_board};

    // Спислк всплывающих окон
    private static final int[] POPUPS = {
            R.id.popup_quit_game};


    // Метод переключения на указанный экран
    private void switchToScreen(int screen) {
        for (int s : SCREENS) {
            if (s == screen) {
                binding.getRoot().findViewById(s).setVisibility(View.VISIBLE);
            } else {
                binding.getRoot().findViewById(s).setVisibility(View.GONE);
            }
        }
    }

    // Показать экран ожидания
    private void showWaitingScreen() {
        timeRemaining = waiting;
        binding.textTimer.setText(String.valueOf(timeRemaining));
        binding.screenPleaseWait.setVisibility(View.VISIBLE);
    }

    // Подсчитать время игры и перейти на страницу результатов
    private void showEndGame(boolean win) {
        timeEnd = new Date();
        int time = (int) ((timeEnd.getTime() - timeStart.getTime()) / 1000); //время в секундах

        NavDirections action = InGameFragmentDirections
                .actionInGameFragmentToResultsFragment(win, playerId, targetPlayerId, time);
        Navigation.findNavController(getView()).navigate(action);
    }


    // Скрыть все всплывающие окна
    private void hidePopups() {
        for (int p : POPUPS) {
            binding.getRoot().findViewById(p).setVisibility(View.GONE);
        }
    }

    // Сбросить отображение оставшихся кораблей в исходное состояние
    private void resetShipIcons() {
        binding.imageMyCarrier.setImageResource(R.drawable.carrier);
        binding.imageMyBattleship.setImageResource(R.drawable.battleship);
        binding.imageMyCruiser.setImageResource(R.drawable.cruiser);
        binding.imageMySubmarine.setImageResource(R.drawable.submarine);
        binding.imageMyDestroyer.setImageResource(R.drawable.destroyer);

        binding.imageTargetCarrier.setImageResource(R.drawable.carrier);
        binding.imageTargetBattleship.setImageResource(R.drawable.battleship);
        binding.imageTargetCruiser.setImageResource(R.drawable.cruiser);
        binding.imageTargetSubmarine.setImageResource(R.drawable.submarine);
        binding.imageTargetDestroyer.setImageResource(R.drawable.destroyer);
    }


    /**
     * Button Click Events
     */
    // Расстановка кораблей случайным образом
    public void buttonPlaceShipsRandomly(Board board) {
        board.placeShipsRandom();
        drawableBoardPlacing.setNoActiveShip();
        drawableBoardPlacing.colorShips();
    }

    // Подвтердить расположение кораблей
    public void buttonConfirmShips(Board board) {
        if (board.isValidBoard()) {
            board.confirmShipLocations();
            if (board == myBoard) {
                myBoard.setShipsPlaced(true);
                myTurn = false;
                if (!targetBoard.areShipsPlaced()) {
                    binding.textPlaceShips.setText(getString(R.string.place_ships, mPlayerViewModel.getTargetPlayer().username));
                    displayDrawableBoardPlacing(targetBoard);
                } else {
                    switchToScreen(R.id.screen_target_board);
                    displayDrawableBoards();
                }
            }
            if (board == targetBoard) {
                targetBoard.setShipsPlaced(true);
                myTurn = true;
                if (!myBoard.areShipsPlaced()) {
                    displayDrawableBoardPlacing(myBoard);
                } else {
                    switchToScreen(R.id.screen_target_board);
                    displayDrawableBoards();
                }
            }
        } else {
            displayError(getString(R.string.invalid_ships_error));
        }
    }


    /**
     * Alert Dialogs
     */

    // Всплывающее окно подтверждения выхода из игры
    private void confirmQuit() {
        binding.popupQuitGame.setVisibility(View.VISIBLE);
    }


    // Показ всплывающего окна с ошибкой, если корабли расставлены с колизией
    private void displayError(String error) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        builder.setTitle(R.string.error_title);
        builder.setMessage(error);

        builder.setNeutralButton(getString(R.string.kk), null);
        builder.show();
    }


    /**
     * Handler and Runnables
     */
    private final Handler handler = new Handler();

    // Таймер
    private final Runnable timer = new Runnable() {
        @Override
        public void run() {
            if (timeRemaining < 0) {
                handler.removeCallbacks(this);
                return;
            }

            timerTick();
            timeRemaining -= 1;
            handler.postDelayed(this, 1000);
        }
    };

    // Тик таймера
    private void timerTick() {
        if (timeRemaining > 0) {
            binding.textTimer.setText(String.valueOf(timeRemaining));
        } else if (timeRemaining == 0) {
            timeRemaining = -1;
            handler.removeCallbacks(timer);
        }
    }

    /* задержка при переключении экранов игроков */
    final Runnable delayTransition = new Runnable() {
        @Override
        public void run() {
            if (myTurn) {
                switchToScreen(R.id.screen_target_board);
                myDrawableBoard.colorReset();
            } else {
                switchToScreen(R.id.screen_my_board);
                targetDrawableBoard.colorReset();
            }
        }
    };

    /**
     * Game Logic
     */
    private Board myBoard;
    private Board targetBoard;
    private DrawableBoardPlacing drawableBoardPlacing;
    private DrawableBoard myDrawableBoard;
    private DrawableBoard targetDrawableBoard;
    private boolean gameInProgress = true;
    private boolean myTurn = false;
    private boolean canTarget = false;

    private Date timeStart, timeEnd;

    // Отрсовка поля для расстановки кораблей
    private void displayDrawableBoardPlacing(Board board) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int buttonSize = displayWidth / (BoardSize.COLUMNS + 1);

        drawableBoardPlacing = new DrawableBoardPlacing(getContext(), board, buttonSize);

        LinearLayout BattleshipGridPlacing = binding.getRoot().findViewById(R.id.battleship_grid_placing);
        BattleshipGridPlacing.removeAllViewsInLayout();
        BattleshipGridPlacing.addView(drawableBoardPlacing);
    }

    // Создание и отрисовка игровых полей
    @SuppressLint("ClickableViewAccessibility")
    private void displayDrawableBoards() {

        // Получаем размеры экрана, чтобы вывести игровое поле по центру
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int buttonSize = displayWidth / (BoardSize.COLUMNS + 1);

        // Создаем доску противника
        targetDrawableBoard = new DrawableBoard(getContext(), buttonSize);

        // Заполняем доску противника
        for (int i = 0; i < BoardSize.ROWS; i++) {
            for (int j = 0; j < BoardSize.COLUMNS; j++) {
                final DrawableSquare square = targetDrawableBoard.squares[i][j];

                // Добавляем слушатели на каждый квадрат доски
                square.setOnTouchListener((view, motionEvent) -> {
                    if (gameInProgress && myTurn && canTarget) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            // Отображаем "тень" для перетаскивания кораблей по полю
                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new MyDragShadowBuilder();
                            view.startDrag(data, shadowBuilder, view, 0);
                            return true;
                        }
                    }
                    return false;
                });

                // Обработка передвижения кораблей по полю
                square.setOnDragListener((view, dragEvent) -> {
                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            DrawableSquare squareEnter = (DrawableSquare) view;
                            Coordinate squareEnterCoordinate = squareEnter.getCoordinate();
                            targetDrawableBoard.colorCrosshair(squareEnterCoordinate.getX(), squareEnterCoordinate.getY());
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            targetDrawableBoard.colorReset();
                            break;
                        case DragEvent.ACTION_DROP:
                            DrawableSquare touchedSquare = (DrawableSquare) view;

                            // Обработка выстрела
                            if (!touchedSquare.isClicked()) {
                                targetCoordinate(touchedSquare, targetDrawableBoard, targetBoard);
                            }
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            break;
                        default:
                            break;
                    }
                    return true;
                });
            }
        }

        // Отрисовка игрового поля в интерфейста
        LinearLayout BattleshipTargetGrid = binding.targetBattleshipGrid;
        BattleshipTargetGrid.removeAllViewsInLayout();
        BattleshipTargetGrid.addView(targetDrawableBoard);

        // Создаем свою доску
        myDrawableBoard = new DrawableBoard(getContext(), buttonSize);

        // Заполняем свою доску квадратами
        for (int i = 0; i < BoardSize.ROWS; i++) {
            for (int j = 0; j < BoardSize.COLUMNS; j++) {
                final DrawableSquare square = myDrawableBoard.squares[i][j];

                // Назначаем слушатели каждому квадрату на нашей доске
                square.setOnTouchListener((view, motionEvent) -> {
                    if (gameInProgress && !myTurn && !canTarget) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new MyDragShadowBuilder();
                            view.startDrag(data, shadowBuilder, view, 0);
                            return true;
                        }
                    }
                    return false;
                });

                square.setOnDragListener((view, dragEvent) -> {
                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            DrawableSquare squareEnter = (DrawableSquare) view;
                            Coordinate squareEnterCoordinate = squareEnter.getCoordinate();
                            myDrawableBoard.colorCrosshair(squareEnterCoordinate.getX(), squareEnterCoordinate.getY());
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            myDrawableBoard.colorReset();
                            break;
                        case DragEvent.ACTION_DROP:
                            DrawableSquare touchedSquare = (DrawableSquare) view;

                            if (!touchedSquare.isClicked()) {
                                targetCoordinate(touchedSquare, myDrawableBoard, myBoard);
                            }
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            break;
                        default:
                            break;
                    }
                    return true;
                });
            }
        }

        // Отрисовка игрового поля в интерфейста
        LinearLayout BattleshipMyGrid = binding.myBattleshipGrid;
        BattleshipMyGrid.removeAllViewsInLayout();
        BattleshipMyGrid.addView(myDrawableBoard);
    }


    // Начало игры
    private void startGameSinglePlayer() {

        // Устанавливаем значения переменных и текстовых компнентов
        resetShipIcons();
        gameInProgress = true;
        myTurn = true;
        canTarget = true;
        switchToScreen(R.id.screen_place_ships);
        binding.textMyTurn.setText(getString(R.string.player_turn, mPlayerViewModel.getPlayer().username));
        binding.textOtherPlayerTurn.setText(getString(R.string.player_turn, mPlayerViewModel.getTargetPlayer().username));
        binding.buttonConfirmShips.setVisibility(View.VISIBLE);
        // Моя доска
        myBoard = new Board();
        // Доска второго игрока
        targetBoard = new Board();

        timeRemaining = waiting;
        timeStart = new Date();
        binding.textPlaceShips.setText(getString(R.string.place_ships, mPlayerViewModel.getPlayer().username));

        // Вызов метода отрисовки поля для расстановки кораблей
        displayDrawableBoardPlacing(myBoard);
    }

    /**
     * canTarget == true - я могу стрелять
     * canTarget == false - может стрелять противник
     */

    /**
     * THIS IS GAME LOGIC
     */
    // Обработка выстрела
    private void targetCoordinate(DrawableSquare square, DrawableBoard drawableBoard, Board board) {
        canTarget = !canTarget;
        // Получение координат выстрела
        Coordinate coordinate = square.getCoordinate();
        int x = coordinate.getX();
        int y = coordinate.getY();

        // Отризовка "прицела"
        drawableBoard.colorCrosshair(x, y);
        square.setClicked(true);

        // Если коррдинаты выстрела такие же, как координаты части кораблся
        // то рисуем попадание
        if (board.getStatus(x, y) == BoardStatus.HIDDEN_SHIP) {
            square.setImage(R.drawable.hit);
            board.setStatus(x, y, BoardStatus.HIT);
            // Отрисовываем затонувшие корабли, если они есть
            displaySunkShip(board, drawableBoard);
            // Если все корабли затоплены, то игра завершена, переходим на страницу с резулльтатами
            if (board.allShipsSunk()) {
                gameInProgress = false;
                showEndGame(board == targetBoard);
            } else {
                myTurn = board != targetBoard;

                // Если не все корабли затоплены, показываем экран ожидания, теперь очередь другого игрока делать выстрел
                showWaitingScreen();
                handler.postDelayed(timer, 0);
                handler.postDelayed(delayTransition, waiting * 1000);
            }
        } else {
            myTurn = board != targetBoard;
            // Если координаты выстрела не совпадают с координатами ни одного корабля
            // отрисовываем попадание мимо
            square.setImage(R.drawable.miss);
            board.setStatus(x, y, BoardStatus.MISS);

            // Показываем экран ожидания. теперь очередь другого игрока
            showWaitingScreen();
            handler.postDelayed(timer, 0);
            handler.postDelayed(delayTransition, waiting * 1000);
        }
    }

    // Отрисовка затонувших кораблей
    private void displaySunkShip(Board board, DrawableBoard dBoard) {
        // Проверяем есть ли на доске потопленные корабли
        if (board.shipToSink() != null) {
            // Если есть, то берем этот корабль
            Ship ship = board.shipToSink();

            // Проходим по координатам потопленного корабля
            // и рисуем на нем значки затопления
            for (Coordinate c : ship.getListCoordinates()) {
                dBoard.squares[c.getX()][c.getY()].setImage(R.drawable.sunk);
            }

            // Меняем отображение кораблей на доске игрока
            if (board == myBoard) {
                switch (ship.getType()) {
                    case CARRIER:
                        binding.imageMyCarrier.setImageResource(R.drawable.carrier_sunk);
                        break;
                    case BATTLESHIP:
                        binding.imageMyBattleship.setImageResource(R.drawable.battleship_sunk);
                        break;
                    case CRUISER:
                        binding.imageMyCruiser.setImageResource(R.drawable.cruiser_sunk);
                        break;
                    case SUBMARINE:
                        binding.imageMySubmarine.setImageResource(R.drawable.submarine_sunk);
                        break;
                    case DESTROYER:
                        binding.imageMyDestroyer.setImageResource(R.drawable.destroyer_sunk);
                        break;
                }
            }
            // Меняем отображение кораблей на доске противника
            else if (board == targetBoard) {
                switch (ship.getType()) {
                    case CARRIER:
                        binding.imageTargetCarrier.setImageResource(R.drawable.carrier_sunk);
                        break;
                    case BATTLESHIP:
                        binding.imageTargetBattleship.setImageResource(R.drawable.battleship_sunk);
                        break;
                    case CRUISER:
                        binding.imageTargetCruiser.setImageResource(R.drawable.cruiser_sunk);
                        break;
                    case SUBMARINE:
                        binding.imageTargetSubmarine.setImageResource(R.drawable.submarine_sunk);
                        break;
                    case DESTROYER:
                        binding.imageTargetDestroyer.setImageResource(R.drawable.destroyer_sunk);
                        break;
                }
            }

            // Топим все корабли на доске
            board.sinkShips();
        }
    }
}