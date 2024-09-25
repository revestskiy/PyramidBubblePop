package com.skyflygame.pyramidbubblepop

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skyflygame.pyramidbubblepop.ui.theme.nujnoefont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

class GameViewModelFactory(private val lvl: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(lvl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

enum class ElementType(val drawableId: Int) {
    BLUE(R.drawable.game_blue),
    DARK_BLUE(R.drawable.game_dark_blue),
    PINK(R.drawable.game_pink),
    DARK_PINK(R.drawable.game_dark_pink),
    DARK_PINK2(R.drawable.game_dark_pink2),
    RED(R.drawable.game_red),
    BRIGHT_RED(R.drawable.game_red_bright),
    WHITE(R.drawable.game_white),
    YELLOW(R.drawable.game_dark_yellow),
    DARK_YELLOW(R.drawable.game_dark_yellow),
    EMPTY(R.drawable.game_white)
}

fun createRandomGrid(rows: Int, cols: Int): Array<Array<ElementType>> {
    val elements = ElementType.entries.toList() - ElementType.EMPTY

    // Создаем пустую сетку
    val grid = Array(rows) { Array(cols) { ElementType.EMPTY } }

    // Шаг 1: Заполняем сетку случайными элементами
    for (row in 0 until rows) {
        for (col in 0 until cols) {
            grid[row][col] = elements.random()
        }
    }

    // Шаг 2: Убедимся, что хотя бы одна тройка существует
    ensureAtLeastOneTripleMatch(grid)

    return grid
}

// Функция для обеспечения хотя бы одной тройки элементов
fun ensureAtLeastOneTripleMatch(grid: Array<Array<ElementType>>) {
    val rows = grid.size
    val cols = grid[0].size

    // Выбираем случайную позицию для гарантированного матча
    val randomRow = (0 until rows - 2).random()  // С учётом места для тройки по вертикали
    val randomCol = (0 until cols - 2).random()  // С учётом места для тройки по горизонтали

    // Выбираем случайный элемент
    val selectedElement = ElementType.values().random()

    // Создаем горизонтальный матч
    grid[randomRow][randomCol] = selectedElement
    grid[randomRow][randomCol + 1] = selectedElement
    grid[randomRow][randomCol + 2] = selectedElement

    // Также создаем вертикальный матч для увеличения вероятности
    grid[randomRow][randomCol] = selectedElement
    grid[randomRow + 1][randomCol] = selectedElement
    grid[randomRow + 2][randomCol] = selectedElement
}


@Composable
fun GameScreen(
    lvl: Int,
    viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(lvl)),
    onReturn: () -> Unit
) {
    val grid by viewModel.grid
    val timeRemaining by viewModel.timeRemaining
    val isWin by viewModel.isWin
    var isSettings by remember {
        mutableStateOf(false)
    }
    val isPaused by viewModel.isPaused

    if (isWin != null) {
        if (isWin == true) {
            GameResultScreen(title = "WIN", buttonText = "Next Level", level = lvl, time = timeRemaining.millisToFormatted) {
                onReturn()
            }
        }
        else {
            GameResultScreen(title = "Defeat", buttonText = "Retry", level = lvl, time = timeRemaining.millisToFormatted) {
                onReturn()
            }
        }
    }
    else if (isSettings) {
        BackHandler {
            isSettings = false
            viewModel.switchPause()
        }
        OptionsScreen(
            onReturn = {
                isSettings = false
                viewModel.switchPause()
            }
        )
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.backgroundmain),
                    contentScale = ContentScale.Crop
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display Score
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.switchPause() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pause),  // Pause icon resource
                            contentDescription = "Pause",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .alpha(
                                    if (isPaused) 0.5f else 1f
                                )
                        )
                    }
                    Image(
                        painter = painterResource(id = drawables[lvl - 1]),
                        contentDescription = "level",
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(horizontal = 4.dp)
                            .height(50.dp),
                        contentScale = ContentScale.Fit
                    )

                    IconButton(
                        onClick = {
                            isSettings = true
                            viewModel.switchPause()
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),  // Settings icon resource
                            contentDescription = "Settings",
                            tint = Color.Unspecified
                        )
                    }
                }
            }
            // Game Grid

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .paint(
                            painter = painterResource(id = R.drawable.red_bg),
                            contentScale = ContentScale.Crop
                        )
                        .padding(8.dp)
                ) {
                    grid.forEachIndexed { rowIndex, row ->
                        Row {
                            row.forEachIndexed { colIndex, element ->
                                ElementTile(
                                    element = element,
                                    isVisible = viewModel.isElementVisible(rowIndex, colIndex),
                                    modifier = Modifier
                                        .size(34.dp)
                                        .padding(2.dp)
                                        .alpha(
                                            if (isPaused) 0.5f else 1f
                                        )
                                        .clickable {
                                            if (!isPaused) viewModel.onElementClick(
                                                rowIndex,
                                                colIndex
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .paint(
                        painterResource(id = R.drawable.bg_stone),
                        contentScale = ContentScale.Crop
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    timeRemaining.millisToFormatted,
                    color = Color(0xFFFFDC00),
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(top = 7.dp),
                    fontFamily = nujnoefont,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ElementTile(
    element: ElementType,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    // Анимация для масштаба (с пружинным эффектом при появлении)
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800, // Длительность анимации
            easing = {
                // Эффект пружины при появлении
                overshootInterpolator(it)
            }
        )
    )

    // Анимация для вращения
    val rotation by animateFloatAsState(
        targetValue = if (isVisible) 0f else 360f, // Поворот на 360 градусов при исчезновении
        animationSpec = tween(
            durationMillis = 600 // Длительность анимации
        )
    )

    // Анимация прозрачности (для появления и исчезновения)
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400 // Длительность анимации
        )
    )

    // Комбинация анимаций для элемента
    AnimatedVisibility(
        visible = isVisible && element != ElementType.EMPTY,
        exit = fadeOut(tween(400)) + scaleOut(tween(600)),
        enter = fadeIn(tween(400)) + scaleIn(tween(800, easing = { overshootInterpolator(it) })),
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotation,
                alpha = alpha
            )
            .size(38.dp) // Размер с учётом анимации масштаба
    ) {
        Image(
            painter = painterResource(id = element.drawableId),
            contentDescription = "",
            modifier = Modifier
        )
    }
}

// Добавим функцию для эффекта "пружины" при увеличении
fun overshootInterpolator(input: Float): Float {
    return (input * input * ((2.0f + 1) * input - 2.0f))
}


class GameViewModel(lvl: Int) : ViewModel() {
    private var gridRows: Int = when (lvl) {
        1 -> 8
        2 -> 9
        3 -> 10
        4 -> 11
        5 -> 12
        else -> 12
    }
    private var gridCols: Int = when (lvl) {
        1 -> 5
        2 -> 6
        3 -> 7
        4 -> 8
        5 -> 9
        else -> 9
    }
    private var timerDuration: Long = when (lvl) {
        1 -> 50000L
        2 -> 50000L
        3 -> 50000L
        4 -> 50000L
        5 -> 60000L
        else -> 100000L
    }

    private val _grid = mutableStateOf(createRandomGrid(gridRows, gridCols))
    val grid: State<Array<Array<ElementType>>> get() = _grid

    private val _score = mutableIntStateOf(0)
    val score: State<Int> get() = _score

    private val _timeRemaining = mutableLongStateOf(timerDuration)
    val timeRemaining: State<Long> get() = _timeRemaining

    private var visibilityGrid = mutableStateOf(Array(gridRows) { Array(gridCols) { true } })

    private val _isPaused = mutableStateOf(false)
    val isPaused: State<Boolean> get() = _isPaused

    private val _isWin = mutableStateOf(null as Boolean?)

    val isWin: State<Boolean?> get() = _isWin

    init {
        startTimer(timerDuration)
    }

    fun onElementClick(row: Int, col: Int) {
        if (!_isPaused.value) {
            removeMatchingElements(row, col)
        }
    }

    fun removeMatchingElements(row: Int, col: Int) = viewModelScope.launch {
        val selectedType = _grid.value[row][col]
        val toRemove = mutableListOf<Pair<Int, Int>>()
        val visited = mutableSetOf<Pair<Int, Int>>()  // Хранит уже проверенные элементы

        // Найдем все совпадающие элементы, начиная с нажатого
        findMatchingElements(row, col, selectedType, toRemove, visited)
        if (toRemove.size == 1) {
            val nearest = findNearestMatchingElement(row, col, selectedType)
            nearest?.let { toRemove.add(it) }  // Добавляем ближайший элемент в toRemove
        }
        if (toRemove.size >= 2) {  // Только удаляем, если 3 или больше
            // Обновляем видимость элементов
            val newVisibilityGrid = visibilityGrid.value.map { it.copyOf() }.toTypedArray()

            toRemove.forEach { (r, c) ->
                newVisibilityGrid[r][c] = false  // Элементы исчезают
            }
            visibilityGrid.value = newVisibilityGrid
            delay(500)  // Задержка перед исчезновением

            val newGrid = _grid.value.map { it.copyOf() }.toTypedArray()

            toRemove.forEach { (r, c) ->
                newGrid[r][c] = ElementType.EMPTY  // Помечаем как пустой
            }

            _grid.value = newGrid
            visibilityGrid.value = newVisibilityGrid

            // Обновляем счёт
            _score.intValue += toRemove.size

            // Проверяем победу (все элементы удалены или набрано достаточно очков)
            if (_grid.value.flatten().all { it == ElementType.EMPTY } || _score.intValue >= gridCols * gridRows / 2) {
                _isWin.value = true
                _isPaused.value = true
            }
        }
    }
    // Функция для поиска ближайшего элемента того же типа в четырёх направлениях
    private fun findNearestMatchingElement(row: Int, col: Int, type: ElementType): Pair<Int, Int>? {
        // Размеры сетки
        val numRows = _grid.value.size
        val numCols = _grid.value[0].size

        // Проверяем направления: вверх, вниз, влево, вправо
        val directions = listOf(
            Pair(-1, 0), // Вверх
            Pair(1, 0),  // Вниз
            Pair(0, -1), // Влево
            Pair(0, 1)   // Вправо
        )

        for ((dr, dc) in directions) {
            var r = row + dr
            var c = col + dc

            // Пока мы в пределах сетки
            while (r in 0 until numRows && c in 0 until numCols) {
                if (_grid.value[r][c] == type) {
                    return Pair(r, c)  // Возвращаем координаты ближайшего элемента
                }
                r += dr
                c += dc
            }
        }

        // Если не найдено совпадений, возвращаем null
        return null
    }

    // Модифицированная функция нахождения совпадений
    private fun findMatchingElements(
        row: Int, col: Int, type: ElementType,
        toRemove: MutableList<Pair<Int, Int>>,
        visited: MutableSet<Pair<Int, Int>>
    ) {
        if (row !in _grid.value.indices || col !in _grid.value[0].indices || visited.contains(row to col)) return
        if (_grid.value[row][col] != type) return

        visited.add(row to col)
        toRemove.add(row to col)

        // Проверяем все 4 направления: вверх, вниз, влево, вправо
        findMatchingElements(row - 1, col, type, toRemove, visited)  // Вверх
        findMatchingElements(row + 1, col, type, toRemove, visited)  // Вниз
        findMatchingElements(row, col - 1, type, toRemove, visited)  // Влево
        findMatchingElements(row, col + 1, type, toRemove, visited)  // Вправо
    }

    fun isElementVisible(row: Int, col: Int): Boolean {
        return visibilityGrid.value.getOrNull(row)?.getOrNull(col) ?: false
    }

    private fun startTimer(duration: Long) {
        _timeRemaining.longValue = duration
        viewModelScope.launch {
            while (_timeRemaining.longValue > 0) {
                delay(1000)
                if (!_isPaused.value) {
                    _timeRemaining.longValue -= 1000
                }
            }

            _isWin.value = _grid.value.flatten()
                .all { it == ElementType.EMPTY } || _score.intValue >= gridCols * gridRows / 2
            _isPaused.value = true
        }
    }

    fun switchPause() {
        _isPaused.value = _isPaused.value.not()
    }
}



val Long.millisToFormatted: String
    get() = String.format(
        Locale.getDefault(),
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this) % 60,
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )