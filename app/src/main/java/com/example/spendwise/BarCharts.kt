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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//fun <K, V : Comparable<V>> Map<K, V>.sortByValue(): Map<K, V> {
//    return toSortedMap(compareByDescending { this[it] })
//}

@Composable
fun VerticalBarsChart(
    data: Map<String, Float>,
) {

    val context = LocalContext.current

    // BarGraph Dimensions
    val barGraphHeight by remember { mutableStateOf(200.dp) }
    val barGraphWidth by remember { mutableStateOf(25.dp) }
    // Scale Dimensions
    val scaleYAxisWidth by remember { mutableStateOf(40.dp) }

    val maxValue = data.values.max()

    val globalPadding = 25.dp

    Column(
        modifier = Modifier
            .padding(start = globalPadding, end = globalPadding)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.weekly_graph_title),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 25.sp,
            ),
            modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barGraphHeight),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ){
                data.forEach {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("$${String.format("%.1f", it.value)}")
                        Box(
                            modifier = Modifier
                                .width(barGraphWidth)
                                .fillMaxHeight(it.value / maxValue.toFloat())
                                .background(color = MaterialTheme.colorScheme.primary)
                                .clickable {
                                    Toast
                                        .makeText(
                                            context,
                                            "${it.key}: \$${String.format("%.2f", it.value)}",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                        )
                    }

                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.onBackground)
        )

        // Scale X-Axis
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            data.forEach {
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
    data: Map<String, Float>
){
    val context = LocalContext.current

    // Screen width to distribute expenses bars
//    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // BarGraph Dimensions
//    val barGraphHeight by remember { mutableStateOf(200.dp) }
//    val barGraphWidth by remember { mutableStateOf(20.dp) }
//    // Scale Dimensions
//    val scaleYAxisWidth by remember { mutableStateOf(40.dp) }
//    val scaleLineWidth by remember { mutableStateOf(1.dp) }

    val maxValue = data.values.max()
//    val sortedData = data.sortByValue()

    Column {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 25.dp, top = 10.dp, end = 25.dp, bottom = 10.dp)
                .fillMaxWidth()
        ){
            Text(text = stringResource(id = R.string.monthly_graph_title),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 25.sp,
                ),
                modifier = Modifier.padding(10.dp))
            Row{
                Box(){
                    Column(
                        modifier = Modifier.height(120.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        data.forEach{
                            Text(it.key)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .height(120.dp)
                        .padding(start = 8.dp)
                ){
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .height(120.dp)
                            .padding(end = 16.dp)
                    ){
                        data.forEach{
                            Box(
                                modifier = Modifier
                                    .height(20.dp)
                                    .fillMaxWidth(it.value / maxValue)
                                    .background(color = MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        Toast
                                            .makeText(
                                                context,
                                                "${it.key}: \$${String.format("%.2f", it.value)}",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
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
                    Text(String.format("%.0f", (i * range)),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(40.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun DisplayChart()
{
    Column {
        VerticalBarsChart(
            data = mapOf(
                Pair("Groceries", 520.45F),
                Pair("Utilities", 300F),
                Pair("Entertainment", 200F)
            ))
        HorizontalBarsChart(
            data = mapOf(
                Pair("Income", 4000F),
                Pair("Budget", 3200F),
                Pair("Spendings", 2400F),
            )
        )
    }
}