package com.ngoctientnt.template.ui.component

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class NotchedBottomBarShape(
    private val notchRadius: Dp = NotchedBottomNavigationBarDefaults.NotchRadius,
    private val notchGap: Dp = NotchedBottomNavigationBarDefaults.NotchGap,
    private val topCornerRadius: Dp = NotchedBottomNavigationBarDefaults.TopCornerRadius,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline = Outline.Generic(buildPath(size, density))

    private fun buildPath(size: Size, density: Density): Path {
        val notchR = with(density) { notchRadius.toPx() }
        val gap = with(density) { notchGap.toPx() }
        val cornerR = with(density) { topCornerRadius.toPx() }
        val cx = size.width / 2f

        val arcRect = Rect(
            left = cx - notchR,
            top = -notchR,
            right = cx + notchR,
            bottom = notchR,
        )

        return Path().apply {
            moveTo(cornerR, 0f)
            lineTo(cx - notchR - gap, 0f)
            arcTo(
                rect = arcRect,
                startAngleDegrees = 180f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false,
            )
            lineTo(cx + notchR + gap, 0f)
            lineTo(size.width - cornerR, 0f)
            arcTo(
                rect = Rect(size.width - 2 * cornerR, 0f, size.width, 2 * cornerR),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            )
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            lineTo(0f, cornerR)
            arcTo(
                rect = Rect(0f, 0f, 2 * cornerR, 2 * cornerR),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            )
            close()
        }
    }
}

object NotchedBottomNavigationBarDefaults {
    val BarContentHeight: Dp = 80.dp
    val FabSize: Dp = 56.dp
    val FabElevation: Dp = 6.dp
    val NotchRadius: Dp = 32.dp
    val NotchGap: Dp = 4.dp
    val TopCornerRadius: Dp = 16.dp
    val FabOverlap: Dp = FabSize / 2
    val TotalHeight: Dp = BarContentHeight + FabOverlap
    val NotchSpacerWidth: Dp = NotchRadius * 2 + NotchGap * 2
}
