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

    private FragmentInGameBinding binding;
    private PlayerViewModel mPlayerViewModel;
    private long playerId = -1, targetPlayerId = -1;

    private int timeRemaining = -1;
    private final int waiting = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        confirmQuit();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInGameBinding.inflate(inflater, container, false);
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerId = InGameFragmentArgs.fromBundle(getArguments()).getPlayerId();
        targetPlayerId = InGameFragmentArgs.fromBundle(getArguments()).getTargetPlayerId();
        mPlayerViewModel.findPlayerById(playerId);
        mPlayerViewModel.findTargetPlayerById(targetPlayerId);

        binding.buttonConfirmShips.setOnClickListener(view -> {
            if (myTurn) buttonConfirmShips(myBoard);
            else buttonConfirmShips(targetBoard);
        });

        binding.buttonPlaceShipsRandomly.setOnClickListener(view -> {
            if (myTurn) buttonPlaceShipsRandomly(myBoard);
            else buttonPlaceShipsRandomly(targetBoard);
        });

        binding.buttonQuitGame.setOnClickListener(view -> {
            confirmQuit();
        });

        binding.buttonResume.setOnClickListener(view -> {
            hidePopups();
        });

        // Кнопка поворота кораблей
        binding.buttonRotateShip.setOnClickListener(view -> {
            if (myDrawableBoardPlacing.getActiveShip() != null) {
                myDrawableBoardPlacing.getActiveShip().rotate();
                myDrawableBoardPlacing.colorShips();
            }
        });

        // Обрабатывает нажатие вне попапа
        binding.popupQuitGame.setOnClickListener(view -> {
            hidePopups();
        });

        binding.buttonQuit.setOnClickListener(view -> {
            NavDirections action = InGameFragmentDirections
                    /* TODO: передавайть айди текущего игрока */
                    .actionInGameFragmentToStartFragment(playerId);
            Navigation.findNavController(view).navigate(action);
        });

        // Предотвращает закрытие попапа при нажатии на него
        binding.popupQuitGameInner.setOnClickListener(view -> {
            //
        });

        startGameSinglePlayer();
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private static final String TAG = "Battleship";

    /**
     * User Interface
     */

    private static final int[] SCREENS = {
            /* TODO: сделать надпись "передайте другому игроку" */
            R.id.screen_please_wait,
            R.id.screen_place_ships,
            R.id.screen_target_board,
            R.id.screen_my_board};

    private static final int[] POPUPS = {
            R.id.popup_quit_game};

    private void switchToScreen(int screen) {
        for (int s : SCREENS) {
            if (s == screen) {
                binding.getRoot().findViewById(s).setVisibility(View.VISIBLE);
            } else {
                binding.getRoot().findViewById(s).setVisibility(View.GONE);
            }
        }
    }

    private void showWaitingScreen() {
        timeRemaining = waiting;
        binding.textTimer.setText(String.valueOf(timeRemaining));
        binding.screenPleaseWait.setVisibility(View.VISIBLE);
    }

    private void showEndGame(boolean win) {
        timeEnd = new Date();
        int time = (int) ((timeEnd.getTime() - timeStart.getTime()) / 1000); //время в секундах

        NavDirections action = InGameFragmentDirections
                .actionInGameFragmentToResultsFragment(win, playerId, targetPlayerId, time);
        Navigation.findNavController(getView()).navigate(action);
    }

    private void hidePopups() {
        for (int p : POPUPS) {
            binding.getRoot().findViewById(p).setVisibility(View.GONE);
        }
    }

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
    public void buttonPlaceShipsRandomly(Board board) {
        board.placeShipsRandom();
        myDrawableBoardPlacing.setNoActiveShip();
        myDrawableBoardPlacing.colorShips();
    }

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

    private void confirmQuit() {
        binding.popupQuitGame.setVisibility(View.VISIBLE);
    }

    private void displayError(String error) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        builder.setTitle(R.string.error_title);
        builder.setMessage(error);

        builder.setNeutralButton("OK", null);
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

    private void timerTick() {
        if (timeRemaining > 0) {
            binding.textTimer.setText(String.valueOf(timeRemaining));
        } else if (timeRemaining == 0) {
            timeRemaining = -1;
            handler.removeCallbacks(timer);
        }
    }

    /* TODO: задержка при переключении экранов игроков */
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
    private DrawableBoardPlacing myDrawableBoardPlacing;
    private DrawableBoard myDrawableBoard;
    private DrawableBoard targetDrawableBoard;
    private boolean gameInProgress = true;
    private boolean myTurn = false;
    private boolean canTarget = false;

    private Date timeStart, timeEnd;

    // Передавать в параметры чью доску надо отрисовать
    private void displayDrawableBoardPlacing(Board board) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int buttonSize = displayWidth / (BoardSize.COLUMNS + 1);

        myDrawableBoardPlacing = new DrawableBoardPlacing(getContext(), board, buttonSize);

        LinearLayout BattleshipGridPlacing = binding.getRoot().findViewById(R.id.battleship_grid_placing);
        BattleshipGridPlacing.removeAllViewsInLayout();
        BattleshipGridPlacing.addView(myDrawableBoardPlacing);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void displayDrawableBoards() {
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
                            targetDrawableBoard.colorCrosshair(squareEnterCoordinate.getX(), squareEnterCoordinate.getY());
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            targetDrawableBoard.colorReset();
                            break;
                        case DragEvent.ACTION_DROP:
                            DrawableSquare touchedSquare = (DrawableSquare) view;

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

        LinearLayout BattleshipMyGrid = binding.myBattleshipGrid;
        BattleshipMyGrid.removeAllViewsInLayout();
        BattleshipMyGrid.addView(myDrawableBoard);
    }

    private void startGameSinglePlayer() {
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
        displayDrawableBoardPlacing(myBoard);
    }

    /**
     * canTarget == true - я могу стрелять
     * canTarget == false - может стрелять противник
     */
    /* TODO: THIS IS GAME LOGIC */
    private void targetCoordinate(DrawableSquare square, DrawableBoard drawableBoard, Board board) {
        canTarget = !canTarget;
        Coordinate coordinate = square.getCoordinate();
        int x = coordinate.getX();
        int y = coordinate.getY();

        drawableBoard.colorCrosshair(x, y);
        square.setClicked(true);

        if (board.getStatus(x, y) == BoardStatus.HIDDEN_SHIP) {
            square.setImage(R.drawable.hit);
            board.setStatus(x, y, BoardStatus.HIT);
            displaySunkShip(board, drawableBoard);
            if (board.allShipsSunk()) {
                gameInProgress = false;
                if (board == targetBoard) showEndGame(true);
                else showEndGame(false);
            } else {
                if (board == targetBoard) myTurn = false;
                else myTurn = true;

                showWaitingScreen();
                handler.postDelayed(timer, 0);
                handler.postDelayed(delayTransition, 5000);
            }
        } else {
            if (board == targetBoard) myTurn = false;
            else myTurn = true;
            square.setImage(R.drawable.miss);
            board.setStatus(x, y, BoardStatus.MISS);

            showWaitingScreen();
            handler.postDelayed(timer, 0);
            handler.postDelayed(delayTransition, 5000);
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

            board.sinkShips();
        }
    }
}