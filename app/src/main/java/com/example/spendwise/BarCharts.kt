package com.example.spendwise

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun <K, V : Comparable<V>> Map<K, V>.sortByValue(): Map<K, V> {
    return toSortedMap(compareByDescending { this[it] })
}

@Composable
fun VerticalBarsChart(
    data: Map<String, Int>
) {

    val context = LocalContext.current

    // Screen width to distribute expenses bars
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // BarGraph Dimensions
    val barGraphHeight by remember { mutableStateOf(200.dp) }
    val barGraphWidth by remember { mutableStateOf(20.dp) }
    // Scale Dimensions
    val scaleYAxisWidth by remember { mutableStateOf(40.dp) }

    val maxValue = data.values.max()
    val sortedData = data.sortByValue()

    val globalPadding = 25.dp
    val firstColumnPadding = 35.dp

    Column(
        modifier = Modifier
            .padding(start = globalPadding, end = globalPadding)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This week",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barGraphHeight),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            // scale Y-Axis
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(firstColumnPadding),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = maxValue.toString())
                    Spacer(modifier = Modifier.fillMaxHeight())
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = (maxValue / 2).toString())
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ){
                sortedData.forEach {
                    Box(
                        modifier = Modifier
                            .width(barGraphWidth)
                            .fillMaxHeight(it.value/maxValue.toFloat())
                            .background(color = Color.Blue)
                            .clickable {
                                Toast
                                    .makeText(context, "${it.key.toString()}: \$${it.value.toString()}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )

        // Scale X-Axis
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = firstColumnPadding),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            sortedData.forEach {
                Text(
                    text = it.key,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(60.dp)
                )
            }

        }

    }

}

@Composable
fun HorizontalBarsChart(
    data: Map<String, Int>
){
    val context = LocalContext.current

    // Screen width to distribute expenses bars
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // BarGraph Dimensions
    val barGraphHeight by remember { mutableStateOf(200.dp) }
    val barGraphWidth by remember { mutableStateOf(20.dp) }
    // Scale Dimensions
    val scaleYAxisWidth by remember { mutableStateOf(40.dp) }
    val scaleLineWidth by remember { mutableStateOf(1.dp) }

    val maxValue = data.values.max()
    val sortedData = data.sortByValue()

    Column {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 25.dp, top = 10.dp, end = 25.dp, bottom = 10.dp)
                .fillMaxWidth()
        ){
            Text(text = "This month's projection",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(10.dp))
            Row{
                Box(){
                    Column(
                        modifier = Modifier.height(120.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        sortedData.forEach{
                            Text(it.key.toString())
                        }
                    }
                }
                Box(
                ){
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .height(120.dp)
                            .padding(start = 8.dp, end = 16.dp)
                    ){
                        sortedData.forEach{
                            Box(
                                modifier = Modifier
                                    .height(20.dp)
                                    .fillMaxWidth(it.value/maxValue.toFloat())
                                    .background(color = Color.Blue)
                            )
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 56.dp)
                    .fillMaxWidth()
            ){
                var range = maxValue / 5
                for (i in 0 until 6) {
                    // Code to be executed in each iteration
                    Text((i * range).toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(36.dp))
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun DisplayChart()
//{
//    Column {
//        VerticalBarsChart(
//            data = mapOf(
//                Pair("Groceries", 520),
//                Pair("Takeout", 500),
//                Pair("Utilities", 300),
//                Pair("Entertainment", 200),
//                Pair("Emergency", 600)
//            ))
//        HorizontalBarsChart(
//            data = mapOf(
//                Pair("Income", 4000),
//                Pair("Budget", 3200),
//                Pair("Spendings", 2400),
//            )
//        )
//    }
//}