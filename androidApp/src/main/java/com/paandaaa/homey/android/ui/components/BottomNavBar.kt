package com.paandaaa.homey.android.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.paandaaa.homey.android.ui.navigation.BottomNavItem

@Composable
fun BottomNavBar() {
    val list = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Wishlist,
        BottomNavItem.Profile,
        BottomNavItem.Setting,
    )

    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        list.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {

                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )

                },
                label = {

                        Text(text = item.title)

                },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}