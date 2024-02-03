package com.example.treeapp.presentation

import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.treeapp.data.NodeEntity
import com.example.treeapp.presentation.viewmodel.NodeScreenEvent
import com.example.treeapp.presentation.viewmodel.NodeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(navController: NavController, id: Int, name: String) {

    var title = name
    var nodeId = id
    val viewModel: NodeViewModel = koinViewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()

    //флаг для определения окончания получения данных из бд
    val childrenSetFlag = rememberSaveable { mutableStateOf(false) }

    //флаг, показывающий, имеем ли мы данные для отображения в recyclerView
    val emptyList = rememberSaveable { mutableStateOf(false) }

    // флаг для навигации при добавлении ноды
    val navigationFlag = rememberSaveable { mutableStateOf(false) }

    //сетим имя для родительской ноды (дефолтное)
    if (title == "") {
        title = "Node 1"
    }

    //пускаю прогресс бар, пока данные не засетились
    LoadingScreen()

    // получаем количество node из бд
    viewModel.reducer(NodeScreenEvent.OnLoadAllNodes(0))

    //использую хендлер, потому что delay из корутин не работает в композабл функциях
    //необходимо, потому что без него данные не успеют прогрузиться и не отобразятся на экране

    val handler = android.os.Handler(Looper.getMainLooper())

    //кейсы, возникающие при отображении нод
    handler.postDelayed({
        //первое открытие приложения, создание корневой ноды
        if (title == "" && state.value.count == 0) {
            viewModel.reducer(NodeScreenEvent.OnAddChild("Node 1", null))
            nodeId = 0
        }

        //запуск приложения (уже имеются ноды)
        else if (title == "" && state.value.count != 0) {
            viewModel.reducer(NodeScreenEvent.OnLoadChildrenList(0))
            nodeId = 0
        }
        //переход на ноду в течение работы с приложением
        else {
            viewModel.reducer(NodeScreenEvent.OnLoadChildrenList(nodeId))
        }

    }, 2000)

    //получаем инфу о том, получили ли мы данные из бд и имеются ли элементы в полученном списке
    handler.postDelayed({
        if (state.value.children != null && state.value.children!!.isNotEmpty()) {
            childrenSetFlag.value = true
        }
    }, 2000)

    //отображение для случая, когда есть дочерние ноды
    if (childrenSetFlag.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8D7EF),
                            Color(0xFFC1D5F5)
                        )
                    )
                )
                .padding(start = 20.dp, bottom = 8.dp, top = 10.dp)
        ) {

            //отображаем название родительской ноды
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                color = Color(0xFF626161),
                modifier = Modifier
                    .padding(top = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            //кнопка добавления дочерней ноды
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 20.dp),
                onClick = {
                    //из вью модели прогружаем количество нод
                    viewModel.reducer(NodeScreenEvent.OnLoadAllNodes(0))
                    val handler = android.os.Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        //через вью модель создаем ноду, имя определяем через количество уже существующих нод
                        viewModel.reducer(
                            NodeScreenEvent.OnAddChild(
                                "Node ${state.value.count + 1}",
                                nodeId
                            )
                        )
                    }, 1000)
                    navigationFlag.value = true
                }) {
                Icon(Icons.Default.Add, contentDescription = "add node")
            }
            Spacer(modifier = Modifier.weight(1f))
            RecyclerViewItems(
                children = state.value.children,
                viewModel,
                navController,
                nodeId,
                title
            )

        }
    } else {
        handler.postDelayed({
            emptyList.value = true
        }, 2500)
    }

    // отображение, когда дочерних нод еще нет
    if (emptyList.value && !childrenSetFlag.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8D7EF),
                            Color(0xFFC1D5F5)
                        )
                    )
                )
                .padding(start = 20.dp, bottom = 8.dp)

        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                color = Color(0xFF626161),
                modifier = Modifier.padding(top = 20.dp)
            )

            //создания дочерней ноды (то же самое, что и выше)
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 20.dp, end = 20.dp),
                onClick = {
                    viewModel.reducer(NodeScreenEvent.OnLoadAllNodes(0))
                    val handler = android.os.Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        viewModel.reducer(
                            NodeScreenEvent.OnAddChild(
                                "Node ${state.value.count + 1}",
                                nodeId
                            )
                        )
                        handler.postDelayed({
                            navigationFlag.value = true
                        }, 3000)
                    }, 1000)
                }) {
                Icon(Icons.Default.Add, contentDescription = "add node")
            }
            BasicText(
                text = "This node has no children",
                style = TextStyle(color = Color.Black, fontSize = 20.sp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 50.dp)
            )

        }

    }

    //пересоздание экрана при добавлении новой ноды
    if (navigationFlag.value) {
        navController.navigate("main_screen/${nodeId}/${title}")
        navigationFlag.value = false
    }
}

//создание recyclerView
@Composable
private fun RecyclerViewItems(
    children: List<NodeEntity>?,
    viewModel: NodeViewModel,
    navController: NavController,
    parentId: Int,
    parentName: String,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8D7EF),
                        Color(0xFFC1D5F5)
                    )
                )
            )
            .padding(top = 20.dp)
    ) {
        items(children!!.size) { index ->
            MyListItem(
                detailModel = children[index],
                viewModel = viewModel,
                navController = navController,
                parentId = parentId,
                parentName = parentName
            )
        }
    }
}

//создание элемента списка
@Composable
private fun MyListItem(
    detailModel: NodeEntity,
    viewModel: NodeViewModel,
    navController: NavController,
    parentId: Int,
    parentName: String,
) {
    //флаг для перехода на дочернюю ноду
    val navigationFlag = rememberSaveable { mutableStateOf(false) }

    //флаг для удаления ноды
    val deleteFlag = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigationFlag.value = true
            }
            .padding(8.dp)
            .background(
                color = Color(0xFFFFFFFF),
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = detailModel.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF626161),
                fontSize = 20.sp
            )
            // через вью модель удаляем ноду
            IconButton(onClick = {
                viewModel.reducer(NodeScreenEvent.OnDeleteChild(detailModel.id))
                deleteFlag.value = true
            }) {
                Icon(Icons.Default.Delete, contentDescription = "delete")
            }
        }

        // пересоздание экрана с данными дочерней ноды
        if (navigationFlag.value) {
            navigationFlag.value = false
            navController.navigate("main_screen/${detailModel.id}/${detailModel.name}")
        }

        // пересоздание экрана после удаления ноды
        if (deleteFlag.value) {
            deleteFlag.value = false
            navController.navigate("main_screen/${parentId}/${parentName}")

        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}



